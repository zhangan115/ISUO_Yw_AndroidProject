// Generated by dagger.internal.codegen.ComponentProcessor (https://google.github.io/dagger).
package com.isuo.yw2application.view.pass;

import dagger.MembersInjector;
import javax.inject.Provider;

public final class ForgePassActivity_MembersInjector implements MembersInjector<ForgePassActivity> {
  private final Provider<ForgePassPresenter> registerPresenterProvider;

  public ForgePassActivity_MembersInjector(Provider<ForgePassPresenter> registerPresenterProvider) {
    assert registerPresenterProvider != null;
    this.registerPresenterProvider = registerPresenterProvider;
  }

  public static MembersInjector<ForgePassActivity> create(
      Provider<ForgePassPresenter> registerPresenterProvider) {
    return new ForgePassActivity_MembersInjector(registerPresenterProvider);
  }

  @Override
  public void injectMembers(ForgePassActivity instance) {
    if (instance == null) {
      throw new NullPointerException("Cannot inject members into a null reference");
    }
    instance.registerPresenter = registerPresenterProvider.get();
  }

  public static void injectRegisterPresenter(
      ForgePassActivity instance, Provider<ForgePassPresenter> registerPresenterProvider) {
    instance.registerPresenter = registerPresenterProvider.get();
  }
}
