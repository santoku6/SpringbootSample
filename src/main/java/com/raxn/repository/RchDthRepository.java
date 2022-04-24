package com.raxn.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.raxn.entity.RchDth;
import com.raxn.entity.RchMobile;

public interface RchDthRepository extends JpaRepository<RchDth, Integer> {

	List<RchMobile> findByUserid(String userid);

	@Query("FROM RchDth WHERE userid is ?1 AND dateTime between ?2 And ?3")
	List<RchDth> findByDateTime(String userid, Date pastDate, Date currentDate);

}