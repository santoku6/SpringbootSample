package com.raxn.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.raxn.entity.RchInsurance;

public interface RchInsuranceRepository extends JpaRepository<RchInsurance, Integer> {

	List<RchInsurance> findByUserid(String userid);

	@Query("FROM RchInsurance WHERE username is ?1 AND dateTime between ?2 And ?3")
	List<RchInsurance> findByDateTime(String username, Date pastDate, Date currentDate);

}
