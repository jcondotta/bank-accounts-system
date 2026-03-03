package com.jcondotta.bankaccounts.application.usecase.lookup.model;

public record AddressDetails(
    String street,
    String streetNumber,
    String addressComplement,
    String postalCode,
    String city
) {}