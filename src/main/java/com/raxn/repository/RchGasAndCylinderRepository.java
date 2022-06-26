package com.raxn.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.raxn.entity.RchGasAndCylinder;

public interface RchGasAndCylinderRepository extends JpaRepository<RchGasAndCylinder, Integer> {

	List<RchGasAndCylinder> findByUserid(String userid);

	@Query("FROM RchGasAndCylinder WHERE username is ?1 AND dateTime between ?2 And ?3")
	List<RchGasAndCylinder> findByDateTime(String username, Date pastDate, Date currentDate);

}
