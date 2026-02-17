package com.jcondotta.bankaccounts.domain.value_objects;

import com.jcondotta.bankaccounts.domain.exceptions.DomainValidationException;

import java.util.Locale;
import java.util.regex.Pattern;

public record Email(String value) {

    public static final String EMAIL_NOT_PROVIDED = "Email must be provided.";
    public static final String EMAIL_INVALID_FORMAT = "Email format is invalid.";
    public static final String EMAIL_EXCEEDS_MAX_LENGTH = "Email exceeds maximum length of 254 characters.";

    public static final int MAX_LENGTH = 254;

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
      "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$"
    );

    public Email {

        if (value == null) {
            throw new DomainValidationException(EMAIL_NOT_PROVIDED);
        }

        var sanitized = value.trim().toLowerCase(Locale.ROOT);

        if (sanitized.isEmpty()) {
            throw new DomainValidationException(EMAIL_NOT_PROVIDED);
        }

        if (sanitized.length() > MAX_LENGTH) {
            throw new DomainValidationException(EMAIL_EXCEEDS_MAX_LENGTH);
        }

        if (!EMAIL_PATTERN.matcher(sanitized.toUpperCase(Locale.ROOT)).matches()) {
            throw new DomainValidationException(EMAIL_INVALID_FORMAT);
        }

        value = sanitized;
    }

    public static Email of(String value) {
        return new Email(value);
    }
}