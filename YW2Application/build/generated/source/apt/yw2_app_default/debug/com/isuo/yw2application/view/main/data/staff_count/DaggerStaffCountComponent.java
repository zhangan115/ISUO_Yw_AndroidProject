// Generated by dagger.internal.codegen.ComponentProcessor (https://google.github.io/dagger).
package com.isuo.yw2application.view.main.data.staff_count;

import com.isuo.yw2application.mode.customer.CustomerRepository;
import com.isuo.yw2application.mode.customer.CustomerRepositoryComponent;
import dagger.MembersInjector;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class DaggerStaffCountComponent implements StaffCountComponent {
  private MembersInjector<StaffCountPresenter> staffCountPresenterMembersInjector;

  private Provider<CustomerRepository> getRepositoryProvider;

  private Provider<StaffCountContract.View> provideStaffCountContractViewProvider;

  private Provider<StaffCountPresenter> staffCountPresenterProvider;

  private MembersInjector<StaffCountActivity> staffCountActivityMembersInjector;

  private DaggerStaffCountComponent(Builder builder) {
    assert builder != null;
    initialize(builder);
  }

  public static Builder builder() {
    return new Builder();
  }

  @SuppressWarnings("unchecked")
  private void initialize(final Builder builder) {

    this.staffCountPresenterMembersInjector = StaffCountPresenter_MembersInjector.create();

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

    this.provideStaffCountContractViewProvider =
        StaffCountModule_ProvideStaffCountContractViewFactory.create(builder.staffCountModule);

    this.staffCountPresenterProvider =
        StaffCountPresenter_Factory.create(
            staffCountPresenterMembersInjector,
            getRepositoryProvider,
            provideStaffCountContractViewProvider);

    this.staffCountActivityMembersInjector =
        StaffCountActivity_MembersInjector.create(staffCountPresenterProvider);
  }

  @Override
  public void inject(StaffCountActivity activity) {
    staffCountActivityMembersInjector.injectMembers(activity);
  }

  public static final class Builder {
    private StaffCountModule staffCountModule;

    private CustomerRepositoryComponent customerRepositoryComponent;

    private Builder() {}

    public StaffCountComponent build() {
      if (staffCountModule == null) {
        throw new IllegalStateException(StaffCountModule.class.getCanonicalName() + " must be set");
      }
      if (customerRepositoryComponent == null) {
        throw new IllegalStateException(
            CustomerRepositoryComponent.class.getCanonicalName() + " must be set");
      }
      return new DaggerStaffCountComponent(this);
    }

    public Builder staffCountModule(StaffCountModule staffCountModule) {
      this.staffCountModule = Preconditions.checkNotNull(staffCountModule);
      return this;
    }

    public Builder customerRepositoryComponent(
        CustomerRepositoryComponent customerRepositoryComponent) {
      this.customerRepositoryComponent = Preconditions.checkNotNull(customerRepositoryComponent);
      return this;
    }
  }
}
