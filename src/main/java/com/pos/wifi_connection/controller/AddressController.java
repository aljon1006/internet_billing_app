package com.pos.wifi_connection.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.pos.wifi_connection.model.Address;
import com.pos.wifi_connection.service.AddressService;

@Controller
public class AddressController {

    @Autowired
    AddressService addressService;
    
    @PostMapping("/settings/areas/save")
    public String addAddress(@ModelAttribute Address address) {
        addressService.saveAddress(address);
        return "redirect:/settings";
    }
}
