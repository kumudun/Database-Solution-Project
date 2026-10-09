package com.example.projectdb.repository;

import com.example.projectdb.entity.CompanyCustomer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyCustomerRepository extends JpaRepository<CompanyCustomer, Integer> {
}