package com.pos.wifi_connection.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pos.wifi_connection.model.Status;

@Repository
public interface StatusRepository extends JpaRepository<Status, Integer>{

}
