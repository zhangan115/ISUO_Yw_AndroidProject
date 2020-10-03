// Generated by dagger.internal.codegen.ComponentProcessor (https://google.github.io/dagger).
package com.isuo.yw2application.view.main.equip.archives;

import com.isuo.yw2application.mode.customer.CustomerRepository;
import com.isuo.yw2application.mode.customer.CustomerRepositoryComponent;
import dagger.MembersInjector;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class DaggerEquipmentArchivesComponent implements EquipmentArchivesComponent {
  private MembersInjector<EquipmentArchivesPresenter> equipmentArchivesPresenterMembersInjector;

  private Provider<CustomerRepository> getRepositoryProvider;

  private Provider<EquipmentArchivesContract.View> provideCreateEquipContractViewProvider;

  private Provider<EquipmentArchivesPresenter> equipmentArchivesPresenterProvider;

  private MembersInjector<EquipmentArchivesActivity> equipmentArchivesActivityMembersInjector;

  private DaggerEquipmentArchivesComponent(Builder builder) {
    assert builder != null;
    initialize(builder);
  }

  public static Builder builder() {
    return new Builder();
  }

  @SuppressWarnings("unchecked")
  private void initialize(final Builder builder) {

    this.equipmentArchivesPresenterMembersInjector =
        EquipmentArchivesPresenter_MembersInjector.create();

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

    this.provideCreateEquipContractViewProvider =
        EquipmentArchivesModule_ProvideCreateEquipContractViewFactory.create(
            builder.equipmentArchivesModule);

    this.equipmentArchivesPresenterProvider =
        EquipmentArchivesPresenter_Factory.create(
            equipmentArchivesPresenterMembersInjector,
            getRepositoryProvider,
            provideCreateEquipContractViewProvider);

    this.equipmentArchivesActivityMembersInjector =
        EquipmentArchivesActivity_MembersInjector.create(equipmentArchivesPresenterProvider);
  }

  @Override
  public void inject(EquipmentArchivesActivity activity) {
    equipmentArchivesActivityMembersInjector.injectMembers(activity);
  }

  public static final class Builder {
    private EquipmentArchivesModule equipmentArchivesModule;

    private CustomerRepositoryComponent customerRepositoryComponent;

    private Builder() {}

    public EquipmentArchivesComponent build() {
      if (equipmentArchivesModule == null) {
        throw new IllegalStateException(
            EquipmentArchivesModule.class.getCanonicalName() + " must be set");
      }
      if (customerRepositoryComponent == null) {
        throw new IllegalStateException(
            CustomerRepositoryComponent.class.getCanonicalName() + " must be set");
      }
      return new DaggerEquipmentArchivesComponent(this);
    }

    public Builder equipmentArchivesModule(EquipmentArchivesModule equipmentArchivesModule) {
      this.equipmentArchivesModule = Preconditions.checkNotNull(equipmentArchivesModule);
      return this;
    }

    public Builder customerRepositoryComponent(
        CustomerRepositoryComponent customerRepositoryComponent) {
      this.customerRepositoryComponent = Preconditions.checkNotNull(customerRepositoryComponent);
      return this;
    }
  }
}
