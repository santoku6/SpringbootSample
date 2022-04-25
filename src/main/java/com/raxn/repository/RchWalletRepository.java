package com.raxn.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.raxn.entity.Wallet;

public interface RchWalletRepository extends JpaRepository<Wallet, Integer> {

	List<Wallet> findByUserid(String userid);

	@Query("FROM Wallet WHERE userid is ?1 AND dateTime between ?2 And ?3")
	List<Wallet> findByDateTime(String userid, Date pastDate, Date currentDate);

}
