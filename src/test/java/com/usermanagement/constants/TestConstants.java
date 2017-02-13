package com.usermanagement.constants;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;

import com.usermanagement.model.User;

public class TestConstants {

	public static final User USER_1 = User.builder()
			.firstName("John")
			.lastName("David")
			.email("john.david@gmail.com")
			.phoneNumber("1234567890")
			.dateOfBirth("2005-01-31")
			.build();
	
	public static final User USER_2 = User.builder()
			.firstName("Jennifer")
			.lastName("Rose")
			.email("jennifer.rose@outlook.com")
			.phoneNumber("1212567890")
			.dateOfBirth("1990-01-31")
			.build();
	
	public static final User USER_3 = User.builder()
			.firstName("Sarah")
			.lastName("Roy")
			.email("sarah.roy@outlook.com")
			.phoneNumber("1212567890")
			.dateOfBirth("1994-01-31")
			.build();
	
	public static final User USER_4 = User.builder()
			.firstName("Jen")
			.lastName("Rosemary")
			.email("jennifer.rose@outlook.com")
			.phoneNumber("8967452312")
			.dateOfBirth("1998-01-31")
			.build();
	
	public static final String START_DATE = "1990-01-01";
	public static final String END_DATE = "1995-12-31";
	public static final LocalDate AVG_AGE = LocalDate.of(1992, 1, 31);
	public static final LocalDate NOW = LocalDate.now();
}
