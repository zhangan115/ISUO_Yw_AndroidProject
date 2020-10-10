// Generated by dagger.internal.codegen.ComponentProcessor (https://google.github.io/dagger).
package com.isuo.yw2application.view.main.task.inspection.detial;

import com.isuo.yw2application.mode.customer.CustomerRepository;
import com.isuo.yw2application.mode.customer.CustomerRepositoryComponent;
import dagger.MembersInjector;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class DaggerInspectDetailComponent implements InspectDetailComponent {
  private MembersInjector<InspectDetailPresenter> inspectDetailPresenterMembersInjector;

  private Provider<CustomerRepository> getRepositoryProvider;

  private Provider<InspectDetailContract.View> provideInspectDetailContractViewProvider;

  private Provider<InspectDetailPresenter> inspectDetailPresenterProvider;

  private MembersInjector<InspectDetailActivity> inspectDetailActivityMembersInjector;

  private DaggerInspectDetailComponent(Builder builder) {
    assert builder != null;
    initialize(builder);
  }

  public static Builder builder() {
    return new Builder();
  }

  @SuppressWarnings("unchecked")
  private void initialize(final Builder builder) {

    this.inspectDetailPresenterMembersInjector = InspectDetailPresenter_MembersInjector.create();

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

    this.provideInspectDetailContractViewProvider =
        InspectDetailModule_ProvideInspectDetailContractViewFactory.create(
            builder.inspectDetailModule);

    this.inspectDetailPresenterProvider =
        InspectDetailPresenter_Factory.create(
            inspectDetailPresenterMembersInjector,
            getRepositoryProvider,
            provideInspectDetailContractViewProvider);

    this.inspectDetailActivityMembersInjector =
        InspectDetailActivity_MembersInjector.create(inspectDetailPresenterProvider);
  }

  @Override
  public void inject(InspectDetailActivity activity) {
    inspectDetailActivityMembersInjector.injectMembers(activity);
  }

  public static final class Builder {
    private InspectDetailModule inspectDetailModule;

    private CustomerRepositoryComponent customerRepositoryComponent;

    private Builder() {}

    public InspectDetailComponent build() {
      if (inspectDetailModule == null) {
        throw new IllegalStateException(
            InspectDetailModule.class.getCanonicalName() + " must be set");
      }
      if (customerRepositoryComponent == null) {
        throw new IllegalStateException(
            CustomerRepositoryComponent.class.getCanonicalName() + " must be set");
      }
      return new DaggerInspectDetailComponent(this);
    }

    public Builder inspectDetailModule(InspectDetailModule inspectDetailModule) {
      this.inspectDetailModule = Preconditions.checkNotNull(inspectDetailModule);
      return this;
    }

    public Builder customerRepositoryComponent(
        CustomerRepositoryComponent customerRepositoryComponent) {
      this.customerRepositoryComponent = Preconditions.checkNotNull(customerRepositoryComponent);
      return this;
    }
  }
}