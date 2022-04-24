package com.raxn.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.raxn.entity.SmsVendor;

public interface SmsVendorRepository extends JpaRepository<SmsVendor, Integer>{
	

}
