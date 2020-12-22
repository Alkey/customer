package com.customer.controller;

import com.customer.dto.CustomerRegistrationDto;
import com.customer.dto.CustomerResponseDto;
import com.customer.dto.CustomerUpdateDto;
import com.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService service;

    @PostMapping
    public CustomerResponseDto create(@RequestBody CustomerRegistrationDto dto) {
        return service.save(dto);
    }

    @GetMapping
    public List<CustomerResponseDto> readAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public CustomerResponseDto readCustomer(@PathVariable Long id) {
        return service.get(id);
    }

    @PutMapping
    public CustomerResponseDto updateCustomer(@RequestBody CustomerUpdateDto dto) {
        return service.update(dto);
    }

    @DeleteMapping("/{id}")
    public void deleteCustomer(@PathVariable Long id) {
        service.delete(id);
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleException(Exception ex) {
        return ex.getMessage();
    }
}
