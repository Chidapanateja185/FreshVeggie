package com.Auth.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserAddress {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer addressId;
	

	private Integer userId;
	
	@Column(name = "AddressLine1")
	private String addressLine1;
	
	@Column(name = "AddressLine2")
	private String addressLine2;
	
	@Column(name = "AddressLine3")
	private String addressLine3;
	
	@Column(name = "NeighBorhood")
	private String neighBorhood;
	
	@Column(name = "City")
	private String city;
	
	@Column(name = "State")
	private String state;
	
	@Column(name = "zipCode")
	private String zipCode;
	
	@Column(name = "phoneNumber")
	private String phoneNumber;
	
	@Column(name = "isAgreeTerms")
	private boolean isAgreeTearms;
	
}
