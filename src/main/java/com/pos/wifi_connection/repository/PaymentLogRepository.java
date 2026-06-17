package com.pos.wifi_connection.repository;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.pos.wifi_connection.model.PaymentLog;

public interface PaymentLogRepository extends JpaRepository<PaymentLog, Integer>{
    @Query("SELECT p FROM PaymentLog p JOIN p.customer c WHERE " +
           "LOWER(c.firstName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(c.lastName) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<PaymentLog> searchPaymentLogs(@Param("query") String query, Pageable pageable);
}
