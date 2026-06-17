package com.pos.wifi_connection.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pos.wifi_connection.model.Status;
import com.pos.wifi_connection.repository.CustomerRepository;
import com.pos.wifi_connection.repository.StatusRepository;
import com.pos.wifi_connection.service.StatusService;

@Service
public class StatusServiceImpl implements StatusService {

    @Autowired
    StatusRepository statusRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Override
    public Status getStatus(Integer id) {
        return statusRepository.findById(id).orElse(null);
    }

    @Override
    public int getCountActiveClients() {
        return customerRepository.countByStatus_Status(0);
    }




}
