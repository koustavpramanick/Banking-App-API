package com.task.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.task.model.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {

	@Query(value = "SELECT * FROM testdb.accounts WHERE customer_id = ?1", nativeQuery = true)
	List<Account> findAccountForCustomerId(int id);
	
	List<Account> findAccountByCustomerId(int id);
}
