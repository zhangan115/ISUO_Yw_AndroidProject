// Generated by dagger.internal.codegen.ComponentProcessor (https://google.github.io/dagger).
package com.isuo.yw2application.view.main.equip.archives;

import dagger.MembersInjector;

public final class EquipmentArchivesPresenter_MembersInjector
    implements MembersInjector<EquipmentArchivesPresenter> {
  public EquipmentArchivesPresenter_MembersInjector() {}

  public static MembersInjector<EquipmentArchivesPresenter> create() {
    return new EquipmentArchivesPresenter_MembersInjector();
  }

  @Override
  public void injectMembers(EquipmentArchivesPresenter instance) {
    if (instance == null) {
      throw new NullPointerException("Cannot inject members into a null reference");
    }
    instance.setupListeners();
  }

  public static void injectSetupListeners(EquipmentArchivesPresenter instance) {
    instance.setupListeners();
  }
}