// Generated by dagger.internal.codegen.ComponentProcessor (https://google.github.io/dagger).
package com.isuo.yw2application.view.main.data.fault_report;

import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class FaultReportModule_ProvideFaultReportContractViewFactory
    implements Factory<FaultReportContract.View> {
  private final FaultReportModule module;

  public FaultReportModule_ProvideFaultReportContractViewFactory(FaultReportModule module) {
    assert module != null;
    this.module = module;
  }

  @Override
  public FaultReportContract.View get() {
    return Preconditions.checkNotNull(
        module.provideFaultReportContractView(),
        "Cannot return null from a non-@Nullable @Provides method");
  }

  public static Factory<FaultReportContract.View> create(FaultReportModule module) {
    return new FaultReportModule_ProvideFaultReportContractViewFactory(module);
  }
}