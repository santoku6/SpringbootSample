package com.raxn.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.raxn.entity.RchBroadbandLandline;

public interface RchBroadbandLandlineRepository extends JpaRepository<RchBroadbandLandline, Integer> {

	List<RchBroadbandLandline> findByUserid(String userid);

	@Query("FROM RchBroadbandLandline WHERE useusernamerid is ?1 AND dateTime between ?2 And ?3")
	List<RchBroadbandLandline> findByDateTime(String username, Date pastDate, Date currentDate);

}
