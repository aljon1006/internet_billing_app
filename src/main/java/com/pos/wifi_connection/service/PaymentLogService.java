package com.pos.wifi_connection.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.pos.wifi_connection.dto.CustomerDTO;
import com.pos.wifi_connection.dto.PaymentLogDTO;
import com.pos.wifi_connection.model.Customer;
import com.pos.wifi_connection.model.PaymentLog;

@Service
public interface PaymentLogService {
    Page<PaymentLogDTO>getPaymentLogs(Pageable pageable);
    PaymentLogDTO getPaymentLogById(Integer id);
    public PaymentLog savePaymentLog(PaymentLogDTO paymentLogDTO);
    public Customer deactivateUser(CustomerDTO customerDTO);
    public byte[] createReceipt(CustomerDTO dto, PaymentLogDTO paymentLog, Integer billId);
    public void updatePlan(Integer custId, Integer planId);
    public Page<PaymentLogDTO> searchPaymentLogs(String searchQuery, Pageable pageable);
}
