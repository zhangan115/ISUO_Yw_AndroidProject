# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

A multi-module Android project (Gradle 6.1.1, Android Gradle Plugin 4.0.0) that builds several related field-inspection / asset-management apps for the Chinese market. It is a **legacy Android Support Library** codebase (NOT AndroidX): `compileSdkVersion 28`, `minSdkVersion 21`, `targetSdkVersion 28`, Java 8. Dependencies are versioned in the root `build.gradle` `ext { }` block and pulled from Aliyun Maven mirrors (falling back to `google()`/`mavenCentral()`).

## Build Commands

There is no lint/test infrastructure configured — build only. Use the Gradle wrapper (Windows):

```bash
# assemble a specific app (debug/release)
./gradlew.bat :YW2Application:assembleDebug
./gradlew.bat :YW2Application:assembleRelease

# build everything
./gradlew.bat assembleDebug
```

- Each application module is a separate installable app with its own `applicationId`, so build per-module rather than expecting one aggregated APK.
- APK output filenames are customized in each app's `applicationVariants` block.
- Signing is shared across apps via `../sito_check.jks` (debug + release both use the same keystore; alias `sito`, password `123456`).
- All four apps plus libraries are declared in `settings.gradle`. **`YwInspection/` is a standalone app not included in `settings.gradle`** — it builds as its own project.

## Module Layout

**Application modules** (each a full app with its own package, `applicationId`, and `HOST` backend URL defined as a `buildConfigField` on a product flavor):

| Module | App name (resValue) | applicationId | Java package |
|---|---|---|---|
| `YwApplication` | 优维+ | `com.isuo.yw` | `com.sito.customer` |
| `YW2Application` | 变压器状评 | `com.isuo.yw2application` | `com.isuo.yw2application` |
| `YW3Application` | 智能灭火管理系统 | `com.isuo.yw3application` | `com.isuo.yw3application` |
| `ShuHeApplication` | 智能任务管理 | `com.isuo.shuhe` | `com.isuo.shuhe` |
| `YwInspection` | 巡检 (standalone) | `com.sito.evpro.inspection` | `com.sito.evpro.inspection` |

**Shared libraries** (all `com.android.library`):
- `Library` — the core shared module. Provides `AbsBaseApp`, `AbsBaseActivity`/`Fragment`, `BasePresenter`/`BaseView`, adapters, utils, chart helpers, widgets. Re-exports the networking/UI stack via `api` deps (Retrofit, OkHttp, RxJava/RxAndroid, Glide, GreenDao, WeChat SDK, multidex).
- `DialogLib`, `popupLibrary`, `AACRecordLibrary` (audio recording), `DateTimePicker`, `MPChartLib`, `FilePickLibrary` — UI/util libraries.
- `easyadapter` and `materialprogressbar` — vendored `.aar` files wrapped as modules via `configurations.maybeCreate("default")` (not real source modules).

## Architecture

Each application module is a self-contained **MVP** app following the same package layout. `YW2Application` is the most complete/active reference implementation; the others mirror it (YwApplication uses a slightly older variant).

```
<app>/src/main/java/<package>/
├── api/        Retrofit API interfaces. Api.java is the central hub holding many
│               nested interfaces (Login, Equip, Count, Check, File, …) + createRetrofit().
├── mode/       Model/data layer (the "M" in MVP).
│   ├── bean/           data models; bean/db/ holds GreenDao entities + generated DAO/session
│   ├── <domain>/       e.g. count/, work/, fault/, equipment/, inject/, overhaul/
│   │     ├── XxxDataSource.java        interface of data operations
│   │     ├── XxxRepository.java        @Singleton impl (wraps RxJava + ApiCallBack)
│   │     └── XxxRepositoryComponent.java  Dagger @Component exposing getRepository()
│   ├── Injection.java  static helper to obtain repositories (not fully used everywhere)
│   └── IListCallBack / IObjectCallBack  async callback contracts
├── view/       UI + presentation (V and P in MVP).
│   ├── base/           BaseActivity, BaseFragment, MvpFragment, SpeechActivity, WebActivity, …
│   ├── <feature>/       e.g. contact/, main/, login/, register/
│   │     ├── XxxContract.java   nested View + Presenter interfaces
│   │     ├── XxxPresenter.java  presenter impl (holds CompositeSubscription)
│   │     └── XxxActivity / XxxFragment
│   └── ApplicationModule.java   Dagger @Module providing Context
├── app/        Application class extending AbsBaseApp; holds Dagger components, DaoSession,
│              current-user session, and host URL override.
├── common/     ConstantStr (string keys, third-party app ids/secrets)
├── db/         CustomerOpenHelp (SQLiteOpenHelper used to obtain the GreenDao writable DB)
├── push/       GeTui (个推) push service/intent-service
└── wxapi/      WeChat SDK callback entry point (WXEntryActivity)
```

### Data flow (per feature)

`Activity/Fragment` → `XxxPresenter` (injected with `XxxRepository`) → `XxxRepository` calls `Api.createRetrofit().create(...)` returning RxJava `Observable<Bean<T>>` → wrapped in `ApiCallBack` (from `api/`), which dispatches `onSuccess`/`onError`/`onFinish` to the presenter via `IListCallBack`/`IObjectCallBack` → presenter calls `View` callbacks.

### Key wiring facts

- **Dependency injection**: Dagger 2. The `Application` builds one `XxxRepositoryComponent` per domain in `onCreate()` (e.g. `DaggerWorkRepositoryComponent.builder().applicationModule(applicationModule).build()`). Repositories are `@Singleton` and `@Inject` their `Context`.
- **Networking**: Retrofit + OkHttp, single shared `Retrofit` in `Api.createRetrofit()`. Base URL comes from `Yw2Application.AppHost()`, which returns the flavor's `BuildConfig.HOST` unless overridden in `SharedPreferences`. Session is cookie-based (persisted via `SPHelper`). Endpoints use `.json` suffix POST/GET, responses wrapped in `Bean<T>`.
- **Local storage**: GreenDao (`schemaVersion` configured per module in its `greendao { }` block — bump it when entity schema changes). `SPHelper` (in `Library`) wraps `SharedPreferences`.
- **Base classes**: all Activities/Fragments extend `Library`'s `AbsBaseActivity`/`AbsBaseFragment` (which manage the activity stack and provide common helpers like `AppHost()`).
- **Third-party SDKs** initialized in the Application class: GeTui push, Umeng analytics, WeChat, iFlytek speech; IDs/keys live in `common/ConstantStr` and `manifestPlaceholders` per flavor.

## Conventions & Gotchas

- **No AndroidX / no Kotlin** — everything is Java 8 with `android.support.*` packages. Do not introduce `androidx.*` or Kotlin without a deliberate migration.
- Adding a new API endpoint means editing the relevant nested interface in `api/Api.java` (or a domain `XxxApi.java`), then exposing it through a repository method + callback.
- Every app module duplicates its own copy of the network/config plumbing rather than sharing beyond `Library`; changes to one app do not automatically apply to the others.
- `local.properties` (SDK path) and the two `.jks` keystores are environment-specific; don't rely on them being present on other machines.
