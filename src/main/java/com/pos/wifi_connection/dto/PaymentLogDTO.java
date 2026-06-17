package com.pos.wifi_connection.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentLogDTO {
    private Integer billId;
    private Integer custId;
    private BigDecimal amount;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private String customerFullName;
}
