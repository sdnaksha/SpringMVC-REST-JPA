package com.usermanagement.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.usermanagement.helper.DateHelper;
import com.usermanagement.model.User;
import com.usermanagement.model.UserRepository;

import ch.qos.logback.core.net.SyslogOutputStream;

/**
 * Spring MVC Controller to manage users.
 */
@RestController
@RequestMapping("/users")
public class UserController {

	private final UserRepository repository;

	@Autowired
	public UserController(UserRepository repository) {
		this.repository = repository;
	}

	/**
	 * Fetches a list of all users.
	 * @return List of all users in the repository.
	 */
	@RequestMapping(method = RequestMethod.GET)
	public List<User> getAllUsers() {
		return repository.findAll();
	}

	/**
	 * Adds a new user.
	 * @param new user to be added
	 * @return {@link ResponseEntity} with OK Status 
	 * if user email does not exist currently, else
	 * returns a HTTP conflict error.
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> add(@RequestBody User newUser) {
		if (repository.exists(newUser.getEmail()))
			return new ResponseEntity<String>("This email already exists", HttpStatus.CONFLICT);

		repository.save(newUser);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * Deletes the user from the user repository.
	 * @param user to be deleted
	 * @return true if user deleted, else false.
	 */
	@RequestMapping(method = RequestMethod.DELETE)
	public boolean deleteUser(@RequestBody User user) {
		if (!repository.exists(user.getEmail())) {
			return false;
		}
		
		repository.delete(user);
		if (repository.exists(user.getEmail()))
			return false;
		return true;
	}

	/**
	 * Calculates average age of existing users between two given dates.
	 * @param startDate
	 * @param endDate
	 * @return average age
	 */
	@Transactional
	@RequestMapping(method = RequestMethod.GET, value = "/avgAge")
	public int findAverageAge(@RequestParam(value = "startDate") String startDate,
			@RequestParam(value = "endDate") String endDate) {
		
		try(Stream<String> dobs = repository.getUsersInDobRange(startDate, endDate)) {
			double averageAge = dobs
					.mapToLong(dob -> DateHelper.parse(dob).toEpochDay())
					.average()
					.orElse(0);
			return (int) ((LocalDate.now().toEpochDay() - averageAge) / 365);
		}
		
	}
}
