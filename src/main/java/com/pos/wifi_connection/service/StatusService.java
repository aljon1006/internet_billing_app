package com.pos.wifi_connection.service;


import org.springframework.stereotype.Service;

import com.pos.wifi_connection.model.Status;

@Service
public interface StatusService {
    public Status getStatus(Integer id);
    public int getCountActiveClients();
}
