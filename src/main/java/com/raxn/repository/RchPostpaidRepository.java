package com.raxn.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.raxn.entity.RchPostpaid;

public interface RchPostpaidRepository extends JpaRepository<RchPostpaid, Integer> {

	List<RchPostpaid> findByUserid(String userid);

	@Query("FROM RchPostpaid WHERE username is ?1 AND dateTime between ?2 And ?3")
	List<RchPostpaid> findByDateTime(String username, Date pastDate, Date currentDate);

}
