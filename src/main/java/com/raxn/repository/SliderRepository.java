package com.raxn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.raxn.entity.Slider;

public interface SliderRepository extends JpaRepository<Slider, Integer>{
	
	@Query("FROM Slider WHERE status='1' ORDER BY id")
	List<Slider> findAllActiveSliders();
	
	//@Query("FROM Slider ORDER BY id")
	List<Slider> findAllByOrderByIdAsc();
	
	Slider findById(int id);

}
