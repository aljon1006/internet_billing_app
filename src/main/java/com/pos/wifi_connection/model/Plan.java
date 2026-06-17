package com.pos.wifi_connection.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "plan_tbl")
@Data
public class Plan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plan_id")
    private Integer planId;

    private Double price;
    private String speed;
}
