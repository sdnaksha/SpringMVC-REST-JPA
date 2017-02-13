package com.usermanagement.model;

import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, String> {

	@Query("select u.dateOfBirth from User u where u.dateOfBirth >= :startDate"
			+ " and u.dateOfBirth <= :endDate")
	Stream<String> getUsersInDobRange(@Param("startDate") String startDate,
	                                 @Param("endDate") String endDate);
	
}
