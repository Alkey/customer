package com.customer.validator;

import com.customer.dto.CustomerRegistrationDto;
import com.customer.dto.CustomerUpdateDto;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class CustomerDtoValidator {
    private static final String EMAIL_REGEX = "^([^@]{1,80}[@][^@]{1,19})$";
    private static final String PHONE_REGEX = "^\\+(?:[0-9]){6,13}";
    private static final String NAME_REGEX = "^[\\w\\s]{2,50}";

    public boolean isValid(CustomerRegistrationDto dto) {
        return validateName(dto.getFullName())
                && validateEmail(dto.getEmail())
                && Objects.nonNull(dto.getPhone())
                && validatePhone(dto.getPhone());
    }

    public boolean isValid(CustomerUpdateDto dto) {
        return validateName(dto.getFullName())
                && (Objects.isNull(dto.getPhone()) || validatePhone(dto.getPhone()));
    }

    private boolean validateName(String name) {
        return Objects.nonNull(name)
                && name.matches(NAME_REGEX)
                && name.trim().length() == name.length();
    }

    private boolean validateEmail(String email) {
        return Objects.nonNull(email) && email.matches(EMAIL_REGEX);
    }

    private boolean validatePhone(String phone) {
        return phone.matches(PHONE_REGEX);
    }
}
