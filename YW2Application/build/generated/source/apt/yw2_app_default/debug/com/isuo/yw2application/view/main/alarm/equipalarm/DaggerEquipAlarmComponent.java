// Generated by dagger.internal.codegen.ComponentProcessor (https://google.github.io/dagger).
package com.isuo.yw2application.view.main.alarm.equipalarm;

import com.isuo.yw2application.mode.customer.CustomerRepositoryComponent;
import dagger.internal.MembersInjectors;
import dagger.internal.Preconditions;

public final class DaggerEquipAlarmComponent implements EquipAlarmComponent {
  private DaggerEquipAlarmComponent(Builder builder) {
    assert builder != null;
  }

  public static Builder builder() {
    return new Builder();
  }

  @Override
  public void inject(EquipAlarmActivity activity) {
    MembersInjectors.<EquipAlarmActivity>noOp().injectMembers(activity);
  }

  public static final class Builder {
    private CustomerRepositoryComponent customerRepositoryComponent;

    private Builder() {}

    public EquipAlarmComponent build() {
      if (customerRepositoryComponent == null) {
        throw new IllegalStateException(
            CustomerRepositoryComponent.class.getCanonicalName() + " must be set");
      }
      return new DaggerEquipAlarmComponent(this);
    }

    /**
     * @deprecated This module is declared, but an instance is not used in the component. This method is a no-op. For more, see https://google.github.io/dagger/unused-modules.
     */
    @Deprecated
    public Builder equipAlarmModule(EquipAlarmModule equipAlarmModule) {
      Preconditions.checkNotNull(equipAlarmModule);
      return this;
    }

    public Builder customerRepositoryComponent(
        CustomerRepositoryComponent customerRepositoryComponent) {
      this.customerRepositoryComponent = Preconditions.checkNotNull(customerRepositoryComponent);
      return this;
    }
  }
}
