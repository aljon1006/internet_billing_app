package com.pos.wifi_connection.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "address_tbl")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Integer addressId;

    @Column(name = "barangay", nullable = false, length = 100)
    private String barangay;

    @Column(name = "municipality", nullable = false, length = 100)
    private String municipality; 
    
    @Column(name = "province", nullable = false, length = 100)
    private String province;
}