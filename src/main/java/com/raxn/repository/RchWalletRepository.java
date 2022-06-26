package com.raxn.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.raxn.entity.Wallet;

public interface RchWalletRepository extends JpaRepository<Wallet, Integer> {

	List<Wallet> findByUsername(String username);

	@Query("FROM Wallet WHERE username is ?1 AND dateTime between ?2 And ?3")
	List<Wallet> findByDateTime(String username, Date pastDate, Date currentDate);

}
