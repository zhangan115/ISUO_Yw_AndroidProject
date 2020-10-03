// Generated by dagger.internal.codegen.ComponentProcessor (https://google.github.io/dagger).
package com.isuo.yw2application.view.main.device.list;

import com.isuo.yw2application.mode.customer.CustomerRepository;
import dagger.MembersInjector;
import dagger.internal.Factory;
import dagger.internal.MembersInjectors;
import javax.inject.Provider;

public final class EquipListPresenter_Factory implements Factory<EquipListPresenter> {
  private final MembersInjector<EquipListPresenter> equipListPresenterMembersInjector;

  private final Provider<CustomerRepository> repositoryProvider;

  private final Provider<EquipListContract.View> viewProvider;

  public EquipListPresenter_Factory(
      MembersInjector<EquipListPresenter> equipListPresenterMembersInjector,
      Provider<CustomerRepository> repositoryProvider,
      Provider<EquipListContract.View> viewProvider) {
    assert equipListPresenterMembersInjector != null;
    this.equipListPresenterMembersInjector = equipListPresenterMembersInjector;
    assert repositoryProvider != null;
    this.repositoryProvider = repositoryProvider;
    assert viewProvider != null;
    this.viewProvider = viewProvider;
  }

  @Override
  public EquipListPresenter get() {
    return MembersInjectors.injectMembers(
        equipListPresenterMembersInjector,
        new EquipListPresenter(repositoryProvider.get(), viewProvider.get()));
  }

  public static Factory<EquipListPresenter> create(
      MembersInjector<EquipListPresenter> equipListPresenterMembersInjector,
      Provider<CustomerRepository> repositoryProvider,
      Provider<EquipListContract.View> viewProvider) {
    return new EquipListPresenter_Factory(
        equipListPresenterMembersInjector, repositoryProvider, viewProvider);
  }
}
