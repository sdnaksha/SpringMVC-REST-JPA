package com.usermanagement.helper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateHelper {

	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	public static LocalDate parse(String date) {
		return LocalDate.parse(date, formatter);
	}
}
