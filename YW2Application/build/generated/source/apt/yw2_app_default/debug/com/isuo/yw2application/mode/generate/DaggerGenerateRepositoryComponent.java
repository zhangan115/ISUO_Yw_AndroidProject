// Generated by dagger.internal.codegen.ComponentProcessor (https://google.github.io/dagger).
package com.isuo.yw2application.mode.generate;

import dagger.internal.DoubleCheck;
import javax.inject.Provider;

public final class DaggerGenerateRepositoryComponent implements GenerateRepositoryComponent {
  private Provider<GenerateRepository> generateRepositoryProvider;

  private DaggerGenerateRepositoryComponent(Builder builder) {
    assert builder != null;
    initialize(builder);
  }

  public static Builder builder() {
    return new Builder();
  }

  public static GenerateRepositoryComponent create() {
    return builder().build();
  }

  @SuppressWarnings("unchecked")
  private void initialize(final Builder builder) {

    this.generateRepositoryProvider = DoubleCheck.provider(GenerateRepository_Factory.create());
  }

  @Override
  public GenerateRepository getRepository() {
    return generateRepositoryProvider.get();
  }

  public static final class Builder {
    private Builder() {}

    public GenerateRepositoryComponent build() {
      return new DaggerGenerateRepositoryComponent(this);
    }
  }
}