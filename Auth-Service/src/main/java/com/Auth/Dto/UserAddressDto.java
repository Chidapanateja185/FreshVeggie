package com.Auth.Dto;

import lombok.Data;

@Data
public class UserAddressDto {
	
	private Integer userId;
	private String addressLine1;
	private String addressLine2;
	private String addressLine3;
	private String neighBorhood;
	private String city;
	private String state;
	private String zipCode;
	private String phoneNumber;
	private boolean isAgreeTearms;
	
}
