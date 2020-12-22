package com.customer.service.impl;

import com.customer.dto.CustomerRegistrationDto;
import com.customer.dto.CustomerResponseDto;
import com.customer.dto.CustomerUpdateDto;
import com.customer.exception.DataProcessingException;
import com.customer.exception.ValidationException;
import com.customer.model.Customer;
import com.customer.repository.CustomerDao;
import com.customer.service.CustomerService;
import com.customer.validator.CustomerDtoValidator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private static final Type CUSTOMER_RESPONSE_DTO_LIST_TYPE = new TypeToken<List<CustomerResponseDto>>() {}.getType();

    private final CustomerDao dao;
    private final ModelMapper mapper;
    private final CustomerDtoValidator validator;

    @Override
    public CustomerResponseDto save(CustomerRegistrationDto customerDto) {
        if (dao.findCustomerByEmail(customerDto.getEmail()).isPresent()) {
            throw new DataProcessingException("Customer already exists: " + customerDto.getEmail());
        }
        if (!validator.isValid(customerDto)) {
            throw new ValidationException("Incorrect input data: " + customerDto);
        }
        Customer customer = mapper.map(customerDto, Customer.class);
        customer.setCreated(System.currentTimeMillis());
        return mapper.map(dao.save(customer), CustomerResponseDto.class);
    }

    @Override
    public List<CustomerResponseDto> getAll() {
        return mapper.map(dao.findCustomersByDeletedFalse(), CUSTOMER_RESPONSE_DTO_LIST_TYPE);
    }

    @Override
    public CustomerResponseDto get(Long id) {
        return dao.findById(id)
                .map(m -> mapper.map(m, CustomerResponseDto.class))
                .orElseThrow(() -> new DataProcessingException("Can't find customer by id: " + id));
    }

    @Override
    public CustomerResponseDto update(CustomerUpdateDto customerUpdateDto) {
        Optional<Customer> optionalCustomer = dao.findById(customerUpdateDto.getId());
        if (optionalCustomer.isEmpty()) {
            throw new DataProcessingException("Can't find customer by id: " + customerUpdateDto.getId());
        }
        if (!validator.isValid(customerUpdateDto)) {
            throw new ValidationException("Incorrect input data: " + customerUpdateDto);
        }
        Customer customer = optionalCustomer.get();
        if (customer.isDeleted()) {
            throw new DataProcessingException("Can't update deleted customer: id = " + customerUpdateDto.getId());
        }
        String phone = customerUpdateDto.getPhone();
        if (Objects.nonNull(phone)) {
            customer.setPhone(phone);
        }
        customer.setFullName(customerUpdateDto.getFullName());
        customer.setUpdated(System.currentTimeMillis());
        return mapper.map(dao.save(customer), CustomerResponseDto.class);
    }

    @Override
    public void delete(Long id) {
        dao.findById(id)
                .map(customer -> {
                    customer.setDeleted(true);
                    customer.setUpdated(System.currentTimeMillis());
                    dao.save(customer);
                    return customer;
                })
                .orElseThrow(() -> new DataProcessingException("Can't find customer by id: " + id));
    }
}
