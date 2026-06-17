package com.pos.wifi_connection.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pos.wifi_connection.model.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer>{
    
}
