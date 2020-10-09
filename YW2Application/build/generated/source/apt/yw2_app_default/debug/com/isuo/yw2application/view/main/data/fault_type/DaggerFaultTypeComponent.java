// Generated by dagger.internal.codegen.ComponentProcessor (https://google.github.io/dagger).
package com.isuo.yw2application.view.main.data.fault_type;

import com.isuo.yw2application.mode.customer.CustomerRepository;
import com.isuo.yw2application.mode.customer.CustomerRepositoryComponent;
import dagger.MembersInjector;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class DaggerFaultTypeComponent implements FaultTypeComponent {
  private MembersInjector<FaultTypePresenter> faultTypePresenterMembersInjector;

  private Provider<CustomerRepository> getRepositoryProvider;

  private Provider<FaultTypeContract.View> provideFaultTypeContractViewProvider;

  private Provider<FaultTypePresenter> faultTypePresenterProvider;

  private MembersInjector<FaultTypeActivity> faultTypeActivityMembersInjector;

  private DaggerFaultTypeComponent(Builder builder) {
    assert builder != null;
    initialize(builder);
  }

  public static Builder builder() {
    return new Builder();
  }

  @SuppressWarnings("unchecked")
  private void initialize(final Builder builder) {

    this.faultTypePresenterMembersInjector = FaultTypePresenter_MembersInjector.create();

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

    this.provideFaultTypeContractViewProvider =
        FaultTypeModule_ProvideFaultTypeContractViewFactory.create(builder.faultTypeModule);

    this.faultTypePresenterProvider =
        FaultTypePresenter_Factory.create(
            faultTypePresenterMembersInjector,
            getRepositoryProvider,
            provideFaultTypeContractViewProvider);

    this.faultTypeActivityMembersInjector =
        FaultTypeActivity_MembersInjector.create(faultTypePresenterProvider);
  }

  @Override
  public void inject(FaultTypeActivity activity) {
    faultTypeActivityMembersInjector.injectMembers(activity);
  }

  public static final class Builder {
    private FaultTypeModule faultTypeModule;

    private CustomerRepositoryComponent customerRepositoryComponent;

    private Builder() {}

    public FaultTypeComponent build() {
      if (faultTypeModule == null) {
        throw new IllegalStateException(FaultTypeModule.class.getCanonicalName() + " must be set");
      }
      if (customerRepositoryComponent == null) {
        throw new IllegalStateException(
            CustomerRepositoryComponent.class.getCanonicalName() + " must be set");
      }
      return new DaggerFaultTypeComponent(this);
    }

    public Builder faultTypeModule(FaultTypeModule faultTypeModule) {
      this.faultTypeModule = Preconditions.checkNotNull(faultTypeModule);
      return this;
    }

    public Builder customerRepositoryComponent(
        CustomerRepositoryComponent customerRepositoryComponent) {
      this.customerRepositoryComponent = Preconditions.checkNotNull(customerRepositoryComponent);
      return this;
    }
  }
}