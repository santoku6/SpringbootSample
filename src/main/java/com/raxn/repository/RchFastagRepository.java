package com.raxn.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.raxn.entity.RchFastag;

public interface RchFastagRepository extends JpaRepository<RchFastag, Integer> {

	List<RchFastag> findByUserid(String userid);

	@Query("FROM RchFastag WHERE userid is ?1 AND dateTime between ?2 And ?3")
	List<RchFastag> findByDateTime(String userid, Date pastDate, Date currentDate);

}
