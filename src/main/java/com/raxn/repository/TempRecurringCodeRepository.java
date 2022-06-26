package com.raxn.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;

import com.raxn.entity.TempRecurringCode;

public interface TempRecurringCodeRepository extends JpaRepository<TempRecurringCode, Integer>{
	
	TempRecurringCode findByCashbackDate(LocalDate date);
	

}
