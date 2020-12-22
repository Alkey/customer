package com.customer.dto;

import lombok.Data;

@Data
public class CustomerUpdateDto {
    private Long id;
    private String fullName;
    private String phone;
}
