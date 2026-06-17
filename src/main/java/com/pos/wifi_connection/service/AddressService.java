package com.pos.wifi_connection.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.pos.wifi_connection.model.Address;

@Service
public interface AddressService {
    
    List<Address> getAllAddresses();
    Optional<Address> getAddressById(Integer id);
    public void saveAddress(Address address);
    
}
