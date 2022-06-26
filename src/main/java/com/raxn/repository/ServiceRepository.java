package com.raxn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.raxn.entity.Service;

public interface ServiceRepository extends JpaRepository<Service, Integer> {

	@Query("FROM Service WHERE status='1' ORDER BY id")
	List<Service> findAllActiceService();

	List<Service> findAllByOrderByIdAsc();
	
	@Query("SELECT name FROM Service service")
	List<String> findNames();
	
	Service findById(int id);
}
