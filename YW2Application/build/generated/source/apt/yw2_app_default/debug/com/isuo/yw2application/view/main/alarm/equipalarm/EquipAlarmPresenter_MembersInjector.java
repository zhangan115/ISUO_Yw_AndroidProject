// Generated by dagger.internal.codegen.ComponentProcessor (https://google.github.io/dagger).
package com.isuo.yw2application.view.main.alarm.equipalarm;

import dagger.MembersInjector;

public final class EquipAlarmPresenter_MembersInjector
    implements MembersInjector<EquipAlarmPresenter> {
  public EquipAlarmPresenter_MembersInjector() {}

  public static MembersInjector<EquipAlarmPresenter> create() {
    return new EquipAlarmPresenter_MembersInjector();
  }

  @Override
  public void injectMembers(EquipAlarmPresenter instance) {
    if (instance == null) {
      throw new NullPointerException("Cannot inject members into a null reference");
    }
    instance.setUpListeners();
  }

  public static void injectSetUpListeners(EquipAlarmPresenter instance) {
    instance.setUpListeners();
  }
}
