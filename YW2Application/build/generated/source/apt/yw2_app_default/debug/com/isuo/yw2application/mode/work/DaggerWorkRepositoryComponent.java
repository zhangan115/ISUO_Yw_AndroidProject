// Generated by dagger.internal.codegen.ComponentProcessor (https://google.github.io/dagger).
package com.isuo.yw2application.mode.work;

import android.content.Context;
import com.isuo.yw2application.view.ApplicationModule;
import com.isuo.yw2application.view.ApplicationModule_ProvideContextFactory;
import dagger.internal.DoubleCheck;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class DaggerWorkRepositoryComponent implements WorkRepositoryComponent {
  private Provider<Context> provideContextProvider;

  private Provider<WorkRepository> workRepositoryProvider;

  private DaggerWorkRepositoryComponent(Builder builder) {
    assert builder != null;
    initialize(builder);
  }

  public static Builder builder() {
    return new Builder();
  }

  @SuppressWarnings("unchecked")
  private void initialize(final Builder builder) {

    this.provideContextProvider =
        ApplicationModule_ProvideContextFactory.create(builder.applicationModule);

    this.workRepositoryProvider =
        DoubleCheck.provider(WorkRepository_Factory.create(provideContextProvider));
  }

  @Override
  public WorkRepository getRepository() {
    return workRepositoryProvider.get();
  }

  public static final class Builder {
    private ApplicationModule applicationModule;

    private Builder() {}

    public WorkRepositoryComponent build() {
      if (applicationModule == null) {
        throw new IllegalStateException(
            ApplicationModule.class.getCanonicalName() + " must be set");
      }
      return new DaggerWorkRepositoryComponent(this);
    }

    public Builder applicationModule(ApplicationModule applicationModule) {
      this.applicationModule = Preconditions.checkNotNull(applicationModule);
      return this;
    }
  }
}