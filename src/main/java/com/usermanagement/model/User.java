package com.usermanagement.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@Entity
public class User {
	
	@Id
	private String email;
	private String firstName;
	private String lastName;
	private String phoneNumber;
	private String dateOfBirth;

}
