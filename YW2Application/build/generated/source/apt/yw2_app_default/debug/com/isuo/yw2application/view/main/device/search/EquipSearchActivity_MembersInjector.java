// Generated by dagger.internal.codegen.ComponentProcessor (https://google.github.io/dagger).
package com.isuo.yw2application.view.main.device.search;

import dagger.MembersInjector;
import javax.inject.Provider;

public final class EquipSearchActivity_MembersInjector
    implements MembersInjector<EquipSearchActivity> {
  private final Provider<EquipSearchPresenter> mEquipSearchPresenterProvider;

  public EquipSearchActivity_MembersInjector(
      Provider<EquipSearchPresenter> mEquipSearchPresenterProvider) {
    assert mEquipSearchPresenterProvider != null;
    this.mEquipSearchPresenterProvider = mEquipSearchPresenterProvider;
  }

  public static MembersInjector<EquipSearchActivity> create(
      Provider<EquipSearchPresenter> mEquipSearchPresenterProvider) {
    return new EquipSearchActivity_MembersInjector(mEquipSearchPresenterProvider);
  }

  @Override
  public void injectMembers(EquipSearchActivity instance) {
    if (instance == null) {
      throw new NullPointerException("Cannot inject members into a null reference");
    }
    instance.mEquipSearchPresenter = mEquipSearchPresenterProvider.get();
  }

  public static void injectMEquipSearchPresenter(
      EquipSearchActivity instance, Provider<EquipSearchPresenter> mEquipSearchPresenterProvider) {
    instance.mEquipSearchPresenter = mEquipSearchPresenterProvider.get();
  }
}