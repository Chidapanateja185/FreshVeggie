package com.Auth.Dao;

import java.util.Map;

import com.Auth.Dto.EndUserDto;
import com.Auth.Dto.UserAddressDto;
import com.Auth.Web.Response;

public interface EndUserDao {
	
	Response initRegistration(EndUserDto userRequest);
	
	Response veifyEmail(Map<String, String> request);
	
	Response passwordSetUp(EndUserDto userRequest);
	
	Response userAdressInformation(UserAddressDto addressRequest);
}
