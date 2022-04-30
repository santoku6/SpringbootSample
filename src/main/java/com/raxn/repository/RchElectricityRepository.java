package com.raxn.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.raxn.entity.RchElectricity;

public interface RchElectricityRepository extends JpaRepository<RchElectricity, Integer> {

	List<RchElectricity> findByUserid(String userid);

	@Query("FROM RchElectricity WHERE userid is ?1 AND dateTime between ?2 And ?3")
	List<RchElectricity> findByDateTime(String userid, Date pastDate, Date currentDate);

}
