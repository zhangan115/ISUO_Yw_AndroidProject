// Generated by dagger.internal.codegen.ComponentProcessor (https://google.github.io/dagger).
package com.isuo.yw2application.view.main.data.fault_time;

import dagger.MembersInjector;

public final class FaultTimePresenter_MembersInjector
    implements MembersInjector<FaultTimePresenter> {
  public FaultTimePresenter_MembersInjector() {}

  public static MembersInjector<FaultTimePresenter> create() {
    return new FaultTimePresenter_MembersInjector();
  }

  @Override
  public void injectMembers(FaultTimePresenter instance) {
    if (instance == null) {
      throw new NullPointerException("Cannot inject members into a null reference");
    }
    instance.setUpListeners();
  }

  public static void injectSetUpListeners(FaultTimePresenter instance) {
    instance.setUpListeners();
  }
}
