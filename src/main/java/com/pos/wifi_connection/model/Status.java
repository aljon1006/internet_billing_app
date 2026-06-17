package com.pos.wifi_connection.model;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "status_tbl")
@Data
public class Status {
    @Id
    @Column(name = "status")
    private Integer status; // 1 or 0
    
    @Column(name = "status_name", length = 50)
    private String statusName;
}
