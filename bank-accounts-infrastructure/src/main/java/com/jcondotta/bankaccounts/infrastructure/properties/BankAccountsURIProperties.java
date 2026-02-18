package com.jcondotta.bankaccounts.infrastructure.properties;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.net.URI;
import java.util.UUID;

@Validated
@ConfigurationProperties(prefix = "api.v1.bank-accounts")
public record BankAccountsURIProperties(@NotBlank String rootPath, @NotBlank String bankAccountIdPath) {

  public URI bankAccountURI(UUID bankAccountId) {
    var expanded = bankAccountIdPath.replace("{bank-account-id}", bankAccountId.toString());
    return URI.create(expanded);
  }

  public URI accountHoldersURI(UUID bankAccountId) {
    var expanded = bankAccountIdPath.replace("{bank-account-id}", bankAccountId.toString());
    return URI.create(expanded + "/account-holders");
  }
}
