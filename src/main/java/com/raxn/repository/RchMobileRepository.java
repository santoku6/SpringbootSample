package com.raxn.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.raxn.entity.RchMobile;

public interface RchMobileRepository extends JpaRepository<RchMobile, Integer>{
	
	List<RchMobile> findByUserid(String userid);
	
	@Query("FROM RchMobile WHERE username is ?1 AND dateTime between ?2 And ?3")
	List<RchMobile> findByDateTime(String username, Date pastDate, Date currentDate);
	

}
