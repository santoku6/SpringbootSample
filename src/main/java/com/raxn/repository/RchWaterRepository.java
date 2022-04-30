package com.raxn.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.raxn.entity.RchWater;

public interface RchWaterRepository extends JpaRepository<RchWater, Integer> {

	List<RchWater> findByUserid(String userid);

	@Query("FROM RchWater WHERE userid is ?1 AND dateTime between ?2 And ?3")
	List<RchWater> findByDateTime(String userid, Date pastDate, Date currentDate);

}
