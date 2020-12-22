package com.customer;

import com.customer.dto.CustomerRegistrationDto;
import com.customer.dto.CustomerUpdateDto;
import com.customer.validator.CustomerDtoValidator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CustomerDtoValidatorTest {
    private static final String NAME = "Ivan Ivanov";
    private static final String PHONE_NUMBER = "+380976544547";
    private static CustomerDtoValidator validator;
    private CustomerRegistrationDto registrationDto;
    private CustomerUpdateDto updateDto;

    @BeforeAll
    public static void init() {
        validator = new CustomerDtoValidator();
    }

    @BeforeEach
    public void initializeDto() {
        registrationDto = new CustomerRegistrationDto();
        registrationDto.setFullName(NAME);
        registrationDto.setPhone(PHONE_NUMBER);
        registrationDto.setEmail("ivanov@gmail.com");
        updateDto = new CustomerUpdateDto();
        updateDto.setPhone(PHONE_NUMBER);
        updateDto.setFullName(NAME);
        updateDto.setId(1L);
    }

    @Test
    public void checkIsValidWithCorrectRegistrationDto() {
        assertTrue(validator.isValid(registrationDto));
    }

    @Test
    public void checkIsValidRegistrationDtoWithIncorrectPhone() {
        registrationDto.setPhone("+3844");
        assertFalse(validator.isValid(registrationDto));
        registrationDto.setPhone("380555");
        assertFalse(validator.isValid(registrationDto));
        registrationDto.setPhone("+38098081230821");
        assertFalse(validator.isValid(registrationDto));
        registrationDto.setPhone(null);
        assertFalse(validator.isValid(registrationDto));
        registrationDto.setPhone("+38098rr");
        assertFalse(validator.isValid(registrationDto));
    }

    @Test
    public void checkIsValidRegistrationDtoWithIncorrectName() {
        registrationDto.setFullName("I");
        assertFalse(validator.isValid(registrationDto));
        registrationDto.setFullName("     ");
        assertFalse(validator.isValid(registrationDto));
        registrationDto.setFullName(" Bob");
        assertFalse(validator.isValid(registrationDto));
        registrationDto.setFullName("Bob ");
        assertFalse(validator.isValid(registrationDto));
        registrationDto.setFullName("This string will be more than fifty characters long.");
        assertFalse(validator.isValid(registrationDto));
        registrationDto.setFullName(null);
        assertFalse(validator.isValid(registrationDto));
    }

    @Test
    public void checkIsValidRegistrationDtoWithIncorrectEmail() {
        registrationDto.setEmail("@gmail.com");
        assertFalse(validator.isValid(registrationDto));
        registrationDto.setEmail("qer@ter@gmail.com");
        assertFalse(validator.isValid(registrationDto));
        registrationDto.setEmail("Email.email.com");
        assertFalse(validator.isValid(registrationDto));
        registrationDto.setEmail("email.com@");
        assertFalse(validator.isValid(registrationDto));
        registrationDto.setEmail(null);
        assertFalse(validator.isValid(registrationDto));
    }

    @Test
    public void checkIsValidWithCorrectUpdateDto() {
        assertTrue(validator.isValid(updateDto));
        updateDto.setPhone(null);
        assertTrue(validator.isValid(updateDto));
    }

    @Test
    public void checkIsValidUpdateDtoWithIncorrectName() {
        updateDto.setFullName("B");
        assertFalse(validator.isValid(updateDto));
        updateDto.setFullName(" B");
        assertFalse(validator.isValid(updateDto));
        updateDto.setFullName("    ");
        assertFalse(validator.isValid(updateDto));
        updateDto.setFullName("B ");
        assertFalse(validator.isValid(updateDto));
        updateDto.setFullName(null);
        assertFalse(validator.isValid(updateDto));
        updateDto.setFullName("This string will be more than fifty characters long.");
        assertFalse(validator.isValid(updateDto));
    }

    @Test
    public void checkIsValidUpdateDtoWithIncorrectPhone() {
        updateDto.setPhone("+38");
        assertFalse(validator.isValid(updateDto));
        updateDto.setPhone("380987");
        assertFalse(validator.isValid(updateDto));
        updateDto.setPhone("");
        assertFalse(validator.isValid(updateDto));
        updateDto.setPhone("+38098898989892");
        assertFalse(validator.isValid(updateDto));
    }
}
