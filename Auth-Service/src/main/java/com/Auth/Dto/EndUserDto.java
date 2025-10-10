package com.Auth.Dto;

import lombok.Data;

@Data
public class EndUserDto {
	
	private String firstName;
	private String lastName;
	private String email;
	private String emailVerifiedStatus;
	private String password;
	private String conformPassword;
	
}
