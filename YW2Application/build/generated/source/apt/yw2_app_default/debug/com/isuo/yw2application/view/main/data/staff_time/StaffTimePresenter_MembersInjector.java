// Generated by dagger.internal.codegen.ComponentProcessor (https://google.github.io/dagger).
package com.isuo.yw2application.view.main.data.staff_time;

import dagger.MembersInjector;

public final class StaffTimePresenter_MembersInjector
    implements MembersInjector<StaffTimePresenter> {
  public StaffTimePresenter_MembersInjector() {}

  public static MembersInjector<StaffTimePresenter> create() {
    return new StaffTimePresenter_MembersInjector();
  }

  @Override
  public void injectMembers(StaffTimePresenter instance) {
    if (instance == null) {
      throw new NullPointerException("Cannot inject members into a null reference");
    }
    instance.setUpListeners();
  }

  public static void injectSetUpListeners(StaffTimePresenter instance) {
    instance.setUpListeners();
  }
}