// Generated by dagger.internal.codegen.ComponentProcessor (https://google.github.io/dagger).
package com.isuo.yw2application.view.pass;

import dagger.MembersInjector;

public final class ForgePassPresenter_MembersInjector
    implements MembersInjector<ForgePassPresenter> {
  public ForgePassPresenter_MembersInjector() {}

  public static MembersInjector<ForgePassPresenter> create() {
    return new ForgePassPresenter_MembersInjector();
  }

  @Override
  public void injectMembers(ForgePassPresenter instance) {
    if (instance == null) {
      throw new NullPointerException("Cannot inject members into a null reference");
    }
    instance.setUpListeners();
  }

  public static void injectSetUpListeners(ForgePassPresenter instance) {
    instance.setUpListeners();
  }
}
