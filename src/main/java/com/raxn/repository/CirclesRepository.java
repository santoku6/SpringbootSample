package com.raxn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.raxn.entity.Circles;

@Repository
public interface CirclesRepository extends JpaRepository<Circles, Integer> {

	

}
