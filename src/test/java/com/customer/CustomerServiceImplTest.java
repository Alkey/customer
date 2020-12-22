package com.customer;

import com.customer.dto.CustomerRegistrationDto;
import com.customer.dto.CustomerResponseDto;
import com.customer.dto.CustomerUpdateDto;
import com.customer.exception.DataProcessingException;
import com.customer.exception.ValidationException;
import com.customer.model.Customer;
import com.customer.repository.CustomerDao;
import com.customer.service.CustomerService;
import com.customer.service.impl.CustomerServiceImpl;
import com.customer.validator.CustomerDtoValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CustomerServiceImplTest {
    private static final String PHONE_NUMBER = "+3809808080808";
    private static final String EMAIL = "email@gmail.com";
    private static final String NAME = "Ivan Ivanov";
    private static final Long ID = 1L;
    private final CustomerDao dao = Mockito.mock(CustomerDao.class);
    private final ModelMapper mapper = Mockito.spy(ModelMapper.class);
    private final CustomerService service = new CustomerServiceImpl(dao, mapper, new CustomerDtoValidator());
    private Customer customer;
    private CustomerRegistrationDto registrationDto;
    private CustomerResponseDto responseDto;
    private CustomerUpdateDto updateDto;

    @BeforeEach
    public void init() {
        customer = new Customer();
        customer.setPhone(PHONE_NUMBER);
        customer.setEmail(EMAIL);
        customer.setFullName(NAME);
        customer.setCreated(System.currentTimeMillis());
        customer.setId(ID);
        registrationDto = new CustomerRegistrationDto();
        registrationDto.setEmail(EMAIL);
        registrationDto.setPhone(PHONE_NUMBER);
        registrationDto.setFullName(NAME);
        responseDto = new CustomerResponseDto();
        responseDto.setId(ID);
        responseDto.setPhone(PHONE_NUMBER);
        responseDto.setEmail(EMAIL);
        responseDto.setFullName(NAME);
        updateDto = new CustomerUpdateDto();
        updateDto.setId(ID);
        updateDto.setPhone("+3809877777");
        updateDto.setFullName("John Alison");
    }

    @Test
    public void checkCreateWithCorrectRegistrationDto() {
        when(dao.findCustomerByEmail(registrationDto.getEmail())).thenReturn(Optional.empty());
        when(dao.save(any())).thenReturn(customer);
        assertEquals(responseDto, service.save(registrationDto));
        verify(dao).findCustomerByEmail(any());
        verify(dao).save(any());
    }

    @Test
    public void checkCreateWithCustomerExistingInDb() {
        when(dao.findCustomerByEmail(any())).thenReturn(Optional.of(customer));
        assertThrows(DataProcessingException.class, () -> service.save(registrationDto));
        verify(dao).findCustomerByEmail(any());
        verify(dao, never()).save(any());
    }

    @Test
    public void checkCreateWithIncorrectData() {
        registrationDto.setEmail("@gmail.com");
        when(dao.findCustomerByEmail(any())).thenReturn(Optional.empty());
        assertThrows(ValidationException.class, () -> service.save(registrationDto));
        verify(dao).findCustomerByEmail(any());
        verify(dao, never()).save(any());
    }

    @Test
    public void checkGetWithCorrectId() {
        when(dao.findById(anyLong())).thenReturn(Optional.of(customer));
        assertEquals(responseDto, service.get(ID));
        verify(dao).findById(anyLong());
    }

    @Test
    public void checkGetWithIncorrectId() {
        when(dao.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(DataProcessingException.class, () -> service.get(ID));
        verify(dao).findById(anyLong());
    }

    @Test
    public void checkUpdateWithCorrectDto() {
        responseDto.setFullName("John Alison");
        responseDto.setPhone("+3809877777");
        Customer updatedIvan = new Customer();
        updatedIvan.setId(ID);
        updatedIvan.setEmail(EMAIL);
        updatedIvan.setFullName("John Alison");
        updatedIvan.setPhone("+3809877777");
        updatedIvan.setCreated(System.currentTimeMillis());
        updatedIvan.setUpdated(System.currentTimeMillis());
        when(dao.findById(anyLong())).thenReturn(Optional.of(customer));
        when(dao.save(any())).thenReturn(updatedIvan);
        assertEquals(responseDto, service.update(updateDto));
        verify(dao).findById(anyLong());
        verify(dao).save(any());
    }

    @Test
    public void checkUpdateWhenCustomerNotInDb() {
        when(dao.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(DataProcessingException.class, () -> service.update(updateDto));
        verify(dao).findById(anyLong());
    }

    @Test
    public void checkUpdateWhenDtoNotValid() {
        updateDto.setPhone("+38");
        when(dao.findById(anyLong())).thenReturn(Optional.of(customer));
        assertThrows(ValidationException.class, () -> service.update(updateDto));
        verify(dao).findById(anyLong());
    }

    @Test
    public void checkUpdateWhenCustomerIsDeleted() {
        customer.setDeleted(true);
        when(dao.findById(anyLong())).thenReturn(Optional.of(customer));
        assertThrows(DataProcessingException.class, () -> service.update(updateDto));
        verify(dao).findById(anyLong());
    }
}
