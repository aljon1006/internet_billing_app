package com.pos.wifi_connection.dto;

import lombok.Data;
import lombok.NoArgsConstructor;    
import lombok.AllArgsConstructor;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {
    
    private Integer custId;
    @NotNull(message = "First name cannot be null")
    @Size(min = 3, max = 50)
    private String firstName;

    @NotNull(message = "Last name cannot be null")
    @Size(min = 3, max = 50)
    private String lastName;

    @NotNull(message = "House number is required")
    private String houseNumber;

    @NotNull(message = "Street is required")
    @Size(min = 3, max = 100)
    private String street;

    
    private Integer addressId;
    private String barangay;
    private String municipality;
    private String province;
    private Integer planId;
    private Integer status;
    private String speed;
    private Double price;
    private LocalDate dueDate;
}