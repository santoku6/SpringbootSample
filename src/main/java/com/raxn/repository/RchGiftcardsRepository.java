package com.raxn.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.raxn.entity.RchGiftcards;

public interface RchGiftcardsRepository extends JpaRepository<RchGiftcards, Integer> {

	List<RchGiftcards> findByUserid(String userid);

	@Query("FROM RchGiftcards WHERE userid is ?1 AND dateTime between ?2 And ?3")
	List<RchGiftcards> findByDateTime(String userid, Date pastDate, Date currentDate);

}
