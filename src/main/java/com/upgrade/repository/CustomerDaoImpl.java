package com.upgrade.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.google.common.base.Optional;
import com.upgrade.domain.Customer;

public class CustomerDaoImpl {
	
	@Autowired
    private JdbcTemplate jdbcTemplate;
	
	public int insertCustomer(String email, String firstName, String lastName) {
		String sql = "INSERT INTO customer (email, firstName, lastName) VALUES (?, ?, ?)";
		return jdbcTemplate.update(sql, new Object[] { email, firstName, lastName });
	}
	

}
