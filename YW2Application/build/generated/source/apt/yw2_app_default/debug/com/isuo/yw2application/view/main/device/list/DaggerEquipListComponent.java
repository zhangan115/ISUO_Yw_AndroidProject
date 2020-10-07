// Generated by dagger.internal.codegen.ComponentProcessor (https://google.github.io/dagger).
package com.isuo.yw2application.view.main.device.list;

import com.isuo.yw2application.mode.customer.CustomerRepository;
import com.isuo.yw2application.mode.customer.CustomerRepositoryComponent;
import dagger.MembersInjector;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class DaggerEquipListComponent implements EquipListComponent {
  private MembersInjector<EquipListPresenter> equipListPresenterMembersInjector;

  private Provider<CustomerRepository> getRepositoryProvider;

  private Provider<EquipListContract.View> provideEquipListContractViewProvider;

  private Provider<EquipListPresenter> equipListPresenterProvider;

  private MembersInjector<EquipListActivity> equipListActivityMembersInjector;

  private DaggerEquipListComponent(Builder builder) {
    assert builder != null;
    initialize(builder);
  }

  public static Builder builder() {
    return new Builder();
  }

  @SuppressWarnings("unchecked")
  private void initialize(final Builder builder) {

    this.equipListPresenterMembersInjector = EquipListPresenter_MembersInjector.create();

    this.getRepositoryProvider =
        new Factory<CustomerRepository>() {
          private final CustomerRepositoryComponent customerRepositoryComponent =
              builder.customerRepositoryComponent;

          @Override
          public CustomerRepository get() {
            return Preconditions.checkNotNull(
                customerRepositoryComponent.getRepository(),
                "Cannot return null from a non-@Nullable component method");
          }
        };

    this.provideEquipListContractViewProvider =
        EquipListModule_ProvideEquipListContractViewFactory.create(builder.equipListModule);

    this.equipListPresenterProvider =
        EquipListPresenter_Factory.create(
            equipListPresenterMembersInjector,
            getRepositoryProvider,
            provideEquipListContractViewProvider);

    this.equipListActivityMembersInjector =
        EquipListActivity_MembersInjector.create(equipListPresenterProvider);
  }

  @Override
  public void inject(EquipListActivity activity) {
    equipListActivityMembersInjector.injectMembers(activity);
  }

  public static final class Builder {
    private EquipListModule equipListModule;

    private CustomerRepositoryComponent customerRepositoryComponent;

    private Builder() {}

    public EquipListComponent build() {
      if (equipListModule == null) {
        throw new IllegalStateException(EquipListModule.class.getCanonicalName() + " must be set");
      }
      if (customerRepositoryComponent == null) {
        throw new IllegalStateException(
            CustomerRepositoryComponent.class.getCanonicalName() + " must be set");
      }
      return new DaggerEquipListComponent(this);
    }

    public Builder equipListModule(EquipListModule equipListModule) {
      this.equipListModule = Preconditions.checkNotNull(equipListModule);
      return this;
    }

    public Builder customerRepositoryComponent(
        CustomerRepositoryComponent customerRepositoryComponent) {
      this.customerRepositoryComponent = Preconditions.checkNotNull(customerRepositoryComponent);
      return this;
    }
  }
}