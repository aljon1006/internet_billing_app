package com.pos.wifi_connection.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.pos.wifi_connection.dto.CustomerDTO;

@Service
public interface CustomerService {
    Page<CustomerDTO> getCustomers(Pageable pageable);
    CustomerDTO getCustomerById(Integer id);
    public void saveCustomer(CustomerDTO customerDTO);
    public Page<CustomerDTO> sortCustomers(String query, Pageable pageable) ;
    Page<CustomerDTO> searchCustomers(String query, Pageable pageable);
    public void updateCustomer(CustomerDTO customerDTO);
}
