package com.pos.wifi_connection.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pos.wifi_connection.dto.CustomerDTO;
import com.pos.wifi_connection.dto.PaymentLogDTO;
import com.pos.wifi_connection.model.PaymentLog;
import com.pos.wifi_connection.service.CustomerService;
import com.pos.wifi_connection.service.PaymentLogService;
import com.pos.wifi_connection.service.PlanService;
import com.pos.wifi_connection.model.Customer;
import java.math.BigDecimal;
import com.pos.wifi_connection.model.Plan;


@Controller
public class ClientController {

    @Autowired
    PaymentLogService paymentLogService;

    @Autowired
    CustomerService customerService;

    @Autowired
    PlanService planService;

    @PostMapping("/customers/pay")
    public String savePaymentLog(@Validated @ModelAttribute("paymentLog") PaymentLogDTO paymentLogDTO, 
                BindingResult result, Model model, RedirectAttributes redirectAttributes) { 
        
        if (result.hasErrors()) {
            return "redirect:/dashboard";
        }
        CustomerDTO customerDTO = customerService.getCustomerById(paymentLogDTO.getCustId());
        Plan plan = planService.getPlanById(customerDTO.getPlanId());
        BigDecimal price = (plan != null) ? BigDecimal.valueOf(plan.getPrice()) : BigDecimal.ZERO;
        paymentLogDTO.setAmount(price);

        PaymentLog savPaymentLog = paymentLogService.savePaymentLog(paymentLogDTO);
        redirectAttributes.addFlashAttribute("downloadReceipt", true);
        redirectAttributes.addFlashAttribute("targetCustId", paymentLogDTO.getCustId());
        redirectAttributes.addFlashAttribute("targetBillId", savPaymentLog.getBillId());
        return "redirect:/customer/view/" + paymentLogDTO.getCustId();
                    
    }

    @PostMapping("/customers/deactivate")
    public String deactivateUser(@ModelAttribute("customerDTO") CustomerDTO customerDTO, 
                BindingResult result, Model model, RedirectAttributes redirectAttributes) { 
        
        if (result.hasErrors()) {
            result.getFieldErrors().forEach(error -> {
                System.out.println("Field: " + error.getField());
                System.out.println("Rejected Value: " + error.getRejectedValue());
                System.out.println("Reason: " + error.getDefaultMessage());
                System.out.println("-----------------------------");
            });
            return "redirect:/dashboard";
        } 
        paymentLogService.deactivateUser(customerDTO);
        // redirectAttributes.addFlashAttribute("downloadReceipt", true);
        // redirectAttributes.addFlashAttribute("targetCustId", paymentLogDTO.getCustId());
        // System.out.println("paymentLogDTO.getBillId(): " + paymentLogDTO.getBillId());
        // redirectAttributes.addFlashAttribute("targetBillId", savPaymentLog.getBillId());
        return "redirect:/dashboard";
                    
    }
    @PostMapping("/customers/edit_upgrade")
    public String updateCustomer(@ModelAttribute("customerDTO") CustomerDTO customerDTO, 
                BindingResult result, Model model, RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            result.getFieldErrors().forEach(error -> {
                System.out.println("Field: " + error.getField());
                System.out.println("Rejected Value: " + error.getRejectedValue());
                System.out.println("Reason: " + error.getDefaultMessage());
                System.out.println("-----------------------------");
            });
            return "redirect:/dashboard";
        }
        paymentLogService.updatePlan(customerDTO.getCustId(), customerDTO.getPlanId());

        return "redirect:/dashboard";

    }


    @GetMapping("/customer/receipt/download/{id}/{billId}")
    public ResponseEntity<byte[]> downloadReceipt(@PathVariable Integer id, @PathVariable Integer billId) {
        CustomerDTO customer = customerService.getCustomerById(id);
        PaymentLogDTO paymentLog = paymentLogService.getPaymentLogById(billId);
        
        // Generate the PDF bytes (Using your PDF Service)
        byte[] pdfBytes = paymentLogService.createReceipt(customer, paymentLog, billId);

        // Set headers for file download
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        
        // This filename will look like: Receipt_DelaCruz_2026-05-10.pdf
        String filename = "Receipt_" + customer.getLastName() + "_" + LocalDate.now() + ".pdf";
        headers.setContentDispositionFormData("attachment", filename);

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }


}
