package com.example.projectdb.repository;

import com.example.projectdb.entity.CustomerProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerProfileRepository extends JpaRepository<CustomerProfile, Integer> {
}
