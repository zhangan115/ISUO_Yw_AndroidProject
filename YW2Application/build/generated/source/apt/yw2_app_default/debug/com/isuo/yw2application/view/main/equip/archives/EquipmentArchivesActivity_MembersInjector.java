// Generated by dagger.internal.codegen.ComponentProcessor (https://google.github.io/dagger).
package com.isuo.yw2application.view.main.equip.archives;

import dagger.MembersInjector;
import javax.inject.Provider;

public final class EquipmentArchivesActivity_MembersInjector
    implements MembersInjector<EquipmentArchivesActivity> {
  private final Provider<EquipmentArchivesPresenter> presenterProvider;

  public EquipmentArchivesActivity_MembersInjector(
      Provider<EquipmentArchivesPresenter> presenterProvider) {
    assert presenterProvider != null;
    this.presenterProvider = presenterProvider;
  }

  public static MembersInjector<EquipmentArchivesActivity> create(
      Provider<EquipmentArchivesPresenter> presenterProvider) {
    return new EquipmentArchivesActivity_MembersInjector(presenterProvider);
  }

  @Override
  public void injectMembers(EquipmentArchivesActivity instance) {
    if (instance == null) {
      throw new NullPointerException("Cannot inject members into a null reference");
    }
    instance.presenter = presenterProvider.get();
  }

  public static void injectPresenter(
      EquipmentArchivesActivity instance, Provider<EquipmentArchivesPresenter> presenterProvider) {
    instance.presenter = presenterProvider.get();
  }
}