// Generated by dagger.internal.codegen.ComponentProcessor (https://google.github.io/dagger).
package com.isuo.yw2application.mode.customer;

import android.content.Context;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class CustomerRepository_Factory implements Factory<CustomerRepository> {
  private final Provider<Context> contextProvider;

  public CustomerRepository_Factory(Provider<Context> contextProvider) {
    assert contextProvider != null;
    this.contextProvider = contextProvider;
  }

  @Override
  public CustomerRepository get() {
    return new CustomerRepository(contextProvider.get());
  }

  public static Factory<CustomerRepository> create(Provider<Context> contextProvider) {
    return new CustomerRepository_Factory(contextProvider);
  }
}
