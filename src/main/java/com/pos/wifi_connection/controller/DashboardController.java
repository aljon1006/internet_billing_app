package com.pos.wifi_connection.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.pos.wifi_connection.config.AppPaginationProperties;
import com.pos.wifi_connection.dto.CustomerDTO;
import com.pos.wifi_connection.model.Address;
import com.pos.wifi_connection.model.Plan;
import com.pos.wifi_connection.service.AddressService;
import com.pos.wifi_connection.service.CustomerService;
import com.pos.wifi_connection.service.PlanService;
import com.pos.wifi_connection.service.StatusService;

@Controller
public class DashboardController {

    @Autowired
    PlanService planService;

    @Autowired
    CustomerService customerService;

    @Autowired
    StatusService statusService;

    @Autowired
    AddressService addressService;
    
    @Autowired
    AppPaginationProperties paginationProperties;

    @GetMapping("/dashboard")
    String showData(Model model, 
                    @RequestParam(required = false, name = "page") Integer page,
                    @RequestParam(required = false, name = "size") Integer size) {

        int currentPage = (page != null) ? page : paginationProperties.getDefaultPage();
        int currentSize = (size != null) ? size : paginationProperties.getDefaultSize();           
        Pageable pageable = PageRequest.of(currentPage, currentSize, Sort.by(paginationProperties.getDefaultSort()).ascending());
        Page<CustomerDTO> customerPage = customerService.getCustomers(pageable);
        displayClients(model, new CustomerDTO(), customerPage);

        return "index"; 
    }

    @GetMapping("/dashboard/data")
    public String getDashBoardData(
                        @RequestParam(required = false, name = "searchQuery") String searchQuery,
                        @RequestParam(required = false, name = "sortValue") String sortOption,
                        @RequestParam(required = false, name = "page") Integer page,
                        Model model) {
            System.out.println("**********QUERY**********" + sortOption);
            int currentPage = (page != null) ? page : paginationProperties.getDefaultPage();
            String currentSort = (sortOption != null) ? sortOption : paginationProperties.getDefaultSort() + ",asc";
 
            Pageable pageable = PageRequest.of(currentPage, paginationProperties.getDefaultSize());

            Page<CustomerDTO> customerPage;
            
            // Filter dynamic page payload
            if (searchQuery != null && !searchQuery.trim().isEmpty()) {
                customerPage = customerService.searchCustomers(searchQuery, pageable);
            } else {
                customerPage = customerService.sortCustomers(sortOption, pageable);
            }
            displayClients(model, new CustomerDTO(), customerPage);
            System.out.println("customerPage.getNumber(): " + customerPage.getTotalPages());  
            model.addAttribute("clients", customerPage.getContent());
            return "index :: #customerTableContainer";
    }
    // @GetMapping("/dashboard/search")
    // public String searchCustomers(@RequestParam(required = false) String query, Model model) {
        
    //     Pageable pageable = PageRequest.of(paginationProperties.getDefaultPage(), 
    //                                 paginationProperties.getDefaultSize(), 
    //                                 Sort.by(paginationProperties.getDefaultSort()).ascending());

    //     Page<CustomerDTO> customerPage = customerService.searchCustomers(query, pageable);

    //     model.addAttribute("clients", customerPage.getContent());

    //     return "index :: #customerTableBody";
    // }

    // @GetMapping("/dashboard/sort")
    // public String sortCustomers(@RequestParam(required = false, name = "sortValue"  ) String sortOption, Model model) {
        
    //     Pageable pageable = PageRequest.of(paginationProperties.getDefaultPage(), paginationProperties.getDefaultSize());

    //     Page<CustomerDTO> customerPage = customerService.sortCustomers(sortOption, pageable);

    //     model.addAttribute("clients", customerPage.getContent());

    //     return "index :: #customerTableBody";
    // }

    @PostMapping("/dashboard/save")
    public String saveCustomer(@Validated @ModelAttribute("customer") CustomerDTO customerDTO, 
                            BindingResult result, Model model) {
        if (result.hasErrors()) {
            // Re-populate model attributes for the form
            List<FieldError> errors = result.getFieldErrors();
            for (FieldError error : errors ) {
                System.out.println(error.getField() + " : " + error.getDefaultMessage());
            }
            Pageable pageable = PageRequest.of(paginationProperties.getDefaultPage(), 
                                    paginationProperties.getDefaultSize(), 
                                    Sort.by(paginationProperties.getDefaultSort()).ascending());
            Page<CustomerDTO> customerPage = customerService.getCustomers(pageable);

            displayClients(model, customerDTO, customerPage);
           
            return "index";
        }

        System.out.println(LocalDate.now().plusDays(30));
        customerDTO.setDueDate(LocalDate.now().plusDays(30));
    
        customerService.saveCustomer(customerDTO);
        return "redirect:/dashboard";
    }

    

    @GetMapping("/customer/view/{id}")
    public String viewCustomer(@PathVariable Integer id, Model model) {
        CustomerDTO customer = customerService.getCustomerById(id);
        Address address = addressService.getAddressById(customer.getAddressId())
                        .orElse(new Address());
        model.addAttribute("address", address);
        model.addAttribute("client", customer);
        model.addAttribute("plans", planService.getPlans());
        // Path relative to templates/
        return "customer/view_customer"; 
    }

    @GetMapping("/settings")
    public String setting(Model model) {
        List<Address> addressList = addressService.getAllAddresses();
        model.addAttribute("addresses", addressList);
        model.addAttribute("plans", planService.getPlans());
        model.addAttribute("addressTag", new Address());
        model.addAttribute("planTag", new Plan());
        return "setting/plan_and_addresses";
    }

    public void displayClients(Model model, CustomerDTO customerDTO, Page<CustomerDTO> customerPage) {
        int active = statusService.getCountActiveClients();
        
        // Get the total count from the Page metadata instead of a List size
        long total = customerPage.getTotalElements(); 
        long inactive = total - active;

        model.addAttribute("totalClients", total);
        model.addAttribute("active_num", active);
        model.addAttribute("inactive_num", inactive);

        // Use the content of the page for the table
        model.addAttribute("clients", customerPage.getContent()); 
        model.addAttribute("customer", customerDTO);
        model.addAttribute("plans", planService.getPlans());
        
        // Pass pagination info to Thymeleaf
        model.addAttribute("currentPage", customerPage.getNumber());
        model.addAttribute("totalPages", customerPage.getTotalPages());
        List<Address> addressList = addressService.getAllAddresses();
        model.addAttribute("addresses", addressList);
    }
}
