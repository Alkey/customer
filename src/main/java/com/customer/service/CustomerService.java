package com.customer.service;

import com.customer.dto.CustomerRegistrationDto;
import com.customer.dto.CustomerResponseDto;
import com.customer.dto.CustomerUpdateDto;

import java.util.List;

public interface CustomerService {
    CustomerResponseDto save(CustomerRegistrationDto dto);

    List<CustomerResponseDto> getAll();

    CustomerResponseDto get(Long id);

    CustomerResponseDto update(CustomerUpdateDto customerUpdateDto);

    void delete(Long id);
}
