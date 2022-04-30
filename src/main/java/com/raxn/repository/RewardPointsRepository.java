package com.raxn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.raxn.entity.RewardPoints;

public interface RewardPointsRepository extends JpaRepository<RewardPoints, Integer>{
	
	List<RewardPoints> findFirst20ByUseridOrderByDateTimeDesc(String userid);
	
	

}
