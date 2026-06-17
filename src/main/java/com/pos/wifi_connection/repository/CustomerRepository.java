package com.pos.wifi_connection.repository;

import com.pos.wifi_connection.model.Customer;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer>{
    int countByStatus_Status(Integer statusId);

    @Query("SELECT c FROM Customer c WHERE " +
           "LOWER(c.firstName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(c.lastName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(c.street) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(c.address.barangay) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<Customer> searchCustomers(@Param("query") String query, Pageable pageable);

    @Query(value = "SELECT c.* FROM customers_tbl c " +
               "ORDER BY " +
               "CASE WHEN :sortOption = 'lastName,asc' THEN c.l_name END ASC, " +
               "CASE WHEN :sortOption = 'lastName,desc' THEN c.l_name END DESC, " +
               "CASE WHEN :sortOption = 'dueDate,asc' THEN c.due_date END ASC, " +
               "CASE WHEN :sortOption = 'dueDate,desc' THEN c.due_date END DESC", 
       countQuery = "SELECT count(*) FROM customers_tbl",
       nativeQuery = true)
    Page<Customer> sortCustomers(@Param("sortOption") String sortOption, Pageable pageable);
}
