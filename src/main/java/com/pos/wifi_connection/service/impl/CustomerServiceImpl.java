package com.pos.wifi_connection.service.impl;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.pos.wifi_connection.dto.CustomerDTO;
import com.pos.wifi_connection.model.Address;
import com.pos.wifi_connection.model.Customer;
import com.pos.wifi_connection.repository.CustomerRepository;
import com.pos.wifi_connection.service.CustomerService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    StatusServiceImpl statusServiceImpl;

    @Autowired
    PlanServiceImpl planServiceImpl;

    @Autowired
    AddressServiceImpl addressServiceImpl;

    @Override
    public Page<CustomerDTO> getCustomers(Pageable pageable) {
        Page<Customer> customerPage = customerRepository.findAll(pageable);
        return customerPage.map(customer -> {
            String planSpeed = (customer.getPlan() != null) ? customer.getPlan().getSpeed() : "No Plan";
            Double price = (customer.getPlan() != null) ? customer.getPlan().getPrice() : 0.0;
            Address addr = customer.getAddress();
            
            return new CustomerDTO(
                customer.getCustId(),
                customer.getFirstName(),
                customer.getLastName(),
            
                customer.getHouseNumber() != null ? customer.getHouseNumber() : "",
                customer.getStreet() != null ? customer.getStreet() : "",
                
                addr != null ? addr.getAddressId() : null,
                addr != null ? addr.getBarangay() : "",
                addr != null ? addr.getMunicipality() : "",
                addr != null ? addr.getProvince() : "",
                
                customer.getPlan() != null ? customer.getPlan().getPlanId() : null,
                customer.getStatus() != null ? customer.getStatus().getStatus() : null,
                planSpeed,
                price,
                customer.getDueDate()
            );
        });
    }

    @Override
    public CustomerDTO getCustomerById(Integer id) {
        return customerRepository.findById(id)  
            .map(customer ->  {
                Address addr = customer.getAddress();
                return new CustomerDTO(  
                    customer.getCustId(),
                    customer.getFirstName(),  
                    customer.getLastName(),  
                    customer.getHouseNumber() != null ? customer.getHouseNumber() : "",
                    customer.getStreet() != null ? customer.getStreet() : "",
                    addr != null ? addr.getAddressId() : null,
                    addr != null ? addr.getBarangay() : "",
                    addr != null ? addr.getMunicipality() : "",
                    addr != null ? addr.getProvince() : "",
                    customer.getPlan().getPlanId(),  
                    customer.getStatus().getStatus(),  
                    customer.getPlan().getSpeed(),
                    customer.getPlan().getPrice(),
                    customer.getDueDate() 
                ); 
            })
            .orElseThrow(() -> new NoSuchElementException("Customer not found"));  
    }

    @Override
    public void saveCustomer(CustomerDTO customerDTO) {
        Customer customer = new Customer();
    
        customer.setFirstName(customerDTO.getFirstName());
        customer.setLastName(customerDTO.getLastName());
        customer.setHouseNumber(customerDTO.getHouseNumber());
        customer.setStreet(customerDTO.getStreet());
        customer.setDueDate(customerDTO.getDueDate());


        Address address = addressServiceImpl.getAddressById(customerDTO.getAddressId())
            .orElseThrow(() -> new EntityNotFoundException(
                "Service area with ID " + customerDTO.getAddressId() + " does not exist."
            ));
        
        customer.setAddress(address);
        customer.setStatus(statusServiceImpl.getStatus(customerDTO.getStatus()));
        customer.setPlan(planServiceImpl.getPlanById(customerDTO.getPlanId()));
        
        customerRepository.save(customer);
    }

    @Override
    public Page<CustomerDTO> searchCustomers(String query, Pageable pageable) {

        Page<Customer> entities;

        if (query == null || query.trim().isEmpty()) {
            entities = customerRepository.findAll(pageable);
        }
        else {
            entities = customerRepository.searchCustomers(query, pageable);
        }

        return entities.map(customer ->  {
            String planSpeed = (customer.getPlan() != null) ? customer.getPlan().getSpeed() : "No Plan";
            Double price = (customer.getPlan() != null) ? customer.getPlan().getPrice() : 0;
            Address addr = customer.getAddress();
            return new CustomerDTO(
                customer.getCustId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getHouseNumber() != null ? customer.getHouseNumber() : "",
                customer.getStreet() != null ? customer.getStreet() : "",
                addr != null ? addr.getAddressId() : null,
                addr != null ? addr.getBarangay() : "",
                addr != null ? addr.getMunicipality() : "",
                addr != null ? addr.getProvince() : "",
                customer.getPlan() != null ? customer.getPlan().getPlanId() : null,
                customer.getStatus() != null ? customer.getStatus().getStatus() : null,
                planSpeed,
                price,
                customer.getDueDate()
            );
        });
    }

    @Override
    public Page<CustomerDTO> sortCustomers(String sortOption, Pageable pageable) {

        Page<Customer> entities;

        if (sortOption == null || sortOption.trim().isEmpty()) {
            
            entities = customerRepository.findAll(pageable);
        }
        else {
            entities = customerRepository.sortCustomers(sortOption, pageable);
        }

        return entities.map(customer ->  {
            String planSpeed = (customer.getPlan() != null) ? customer.getPlan().getSpeed() : "No Plan";
            Double price = (customer.getPlan() != null) ? customer.getPlan().getPrice() : 0;
            Address addr = customer.getAddress();
            
            return new CustomerDTO(
                customer.getCustId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getHouseNumber() != null ? customer.getHouseNumber() : "",
                customer.getStreet() != null ? customer.getStreet() : "",
                addr != null ? addr.getAddressId() : null,
                addr != null ? addr.getBarangay() : "",
                addr != null ? addr.getMunicipality() : "",
                addr != null ? addr.getProvince() : "",
                customer.getPlan() != null ? customer.getPlan().getPlanId() : null,
                customer.getStatus() != null ? customer.getStatus().getStatus() : null,
                planSpeed,
                price,
                customer.getDueDate()
            );
        });
    }

    @Override
    public void updateCustomer(CustomerDTO customerDTO) {
        
    }
    
}

