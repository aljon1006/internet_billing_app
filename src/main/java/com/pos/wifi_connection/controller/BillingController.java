package com.pos.wifi_connection.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import com.pos.wifi_connection.config.AppPaginationProperties;
import com.pos.wifi_connection.dto.PaymentLogDTO;
import com.pos.wifi_connection.service.CustomerService;
import com.pos.wifi_connection.service.PaymentLogService;
import org.springframework.data.domain.*;



@Controller
public class BillingController {

    @Autowired
    AppPaginationProperties paginationProperties;

    @Autowired
    PaymentLogService paymentLogService;

    @Autowired
    CustomerService customerService;
    
    @GetMapping("/billing")
    public String billing(Model model, 
                    @RequestParam(required = false, name = "page") Integer page,
                    @RequestParam(required = false, name = "size") Integer size) {

        int currentPage = (page != null) ? page : paginationProperties.getDefaultPage();
        int currentSize = (size != null) ? size : paginationProperties.getDefaultSize();
        Pageable pageable = PageRequest.of(currentPage, currentSize, Sort.by("createDate").descending());
        Page<PaymentLogDTO> paymentLogPage = paymentLogService.getPaymentLogs(pageable);

        model.addAttribute("currentPage", paymentLogPage.getNumber());
        model.addAttribute("totalPages", paymentLogPage.getTotalPages());
        model.addAttribute("paymentLogPage", paymentLogPage);
        return "billing/billing";
    }

    @GetMapping("/billing/data")
    public String getBillingData(
                        @RequestParam(required = false, name = "searchQuery") String searchQuery,
                        @RequestParam(required = false, name = "page") Integer page,
                        Model model) {

            int currentPage = (page != null) ? page : paginationProperties.getDefaultPage();
            Pageable pageable = PageRequest.of(currentPage, paginationProperties.getDefaultSize());
            Page<PaymentLogDTO> paymentLogPage;
                          
            // Filter dynamic page payload
            if (searchQuery != null && !searchQuery.trim().isEmpty()) {
                paymentLogPage = paymentLogService.searchPaymentLogs(searchQuery, pageable);
            } else {
                paymentLogPage = paymentLogService.getPaymentLogs(pageable);
                System.out.println("paymentLogPage.getNumber(): " + paymentLogPage.getTotalPages());  
            }
            
            model.addAttribute("paymentLogPage", paymentLogPage);
            model.addAttribute("currentPage", paymentLogPage.getNumber());
            model.addAttribute("totalPages", paymentLogPage.getTotalPages());
            return "billing/billing :: #paymentLogsTableContainer";
    }
}
