// Generated by dagger.internal.codegen.ComponentProcessor (https://google.github.io/dagger).
package com.isuo.yw2application.view.main.alarm.equipalarm;

import com.isuo.yw2application.mode.customer.CustomerRepository;
import dagger.MembersInjector;
import dagger.internal.Factory;
import dagger.internal.MembersInjectors;
import javax.inject.Provider;

public final class EquipAlarmPresenter_Factory implements Factory<EquipAlarmPresenter> {
  private final MembersInjector<EquipAlarmPresenter> equipAlarmPresenterMembersInjector;

  private final Provider<CustomerRepository> repositoryProvider;

  private final Provider<EquipAlarmContract.View> viewProvider;

  public EquipAlarmPresenter_Factory(
      MembersInjector<EquipAlarmPresenter> equipAlarmPresenterMembersInjector,
      Provider<CustomerRepository> repositoryProvider,
      Provider<EquipAlarmContract.View> viewProvider) {
    assert equipAlarmPresenterMembersInjector != null;
    this.equipAlarmPresenterMembersInjector = equipAlarmPresenterMembersInjector;
    assert repositoryProvider != null;
    this.repositoryProvider = repositoryProvider;
    assert viewProvider != null;
    this.viewProvider = viewProvider;
  }

  @Override
  public EquipAlarmPresenter get() {
    return MembersInjectors.injectMembers(
        equipAlarmPresenterMembersInjector,
        new EquipAlarmPresenter(repositoryProvider.get(), viewProvider.get()));
  }

  public static Factory<EquipAlarmPresenter> create(
      MembersInjector<EquipAlarmPresenter> equipAlarmPresenterMembersInjector,
      Provider<CustomerRepository> repositoryProvider,
      Provider<EquipAlarmContract.View> viewProvider) {
    return new EquipAlarmPresenter_Factory(
        equipAlarmPresenterMembersInjector, repositoryProvider, viewProvider);
  }
}