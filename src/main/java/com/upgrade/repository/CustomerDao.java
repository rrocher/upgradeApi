package com.upgrade.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.upgrade.domain.Customer;

@Repository
public interface CustomerDao  extends JpaRepository<Customer, Integer>{
	Customer findById(int id);

	Customer findByEmail(String email);
}
