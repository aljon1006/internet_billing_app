package com.pos.wifi_connection.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pos.wifi_connection.model.Address;
import com.pos.wifi_connection.repository.AddressRepository;
import com.pos.wifi_connection.service.AddressService;

import jakarta.transaction.Transactional;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    AddressRepository addressRepository;

    @Override
    public List<Address> getAllAddresses() {
        return addressRepository.findAll();
    }

    @Override
    public Optional<Address> getAddressById(Integer id) {
        return addressRepository.findById(id);
    }

    @Override
    public void saveAddress(Address address) {
        addressRepository.save(address);
    }
    
    
}
