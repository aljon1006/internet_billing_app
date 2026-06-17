package com.pos.wifi_connection.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "customers_tbl")
@Data
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cust_id")
    private Integer custId;

    @Column(name = "f_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "l_name", nullable = false, length = 100)
    private String lastName;

    // Local residence attributes stay directly inside the customer entity
    @Column(name = "house_number", length = 30)
    private String houseNumber;

    @Column(name = "street", length = 100)
    private String street;

    // RELATIONSHIP LINK: Many customers can share the same master regional area
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    @ManyToOne
    @JoinColumn(name = "plan_id")
    private Plan plan;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private Status status;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @CreationTimestamp
    @Column(name = "create_date", updatable = false)
    private LocalDateTime createDate;

    @UpdateTimestamp
    @Column(name = "update_date")
    private LocalDateTime updateDate;
}