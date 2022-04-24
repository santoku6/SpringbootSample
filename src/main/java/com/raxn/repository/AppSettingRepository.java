package com.raxn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.raxn.entity.AppSetting;

@Repository
public interface AppSettingRepository extends JpaRepository<AppSetting, Integer> {

	

}
