package com.raxn.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.raxn.entity.RchDth;

public interface RchDthRepository extends JpaRepository<RchDth, Integer> {

	List<RchDth> findByUserid(String userid);

	@Query("FROM RchDth WHERE username is ?1 AND dateTime between ?2 And ?3")
	List<RchDth> findByDateTime(String username, Date pastDate, Date currentDate);

}
