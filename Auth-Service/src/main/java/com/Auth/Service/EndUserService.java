package com.Auth.Service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Auth.Dao.EndUserDao;
import com.Auth.Dto.EndUserDto;
import com.Auth.Dto.UserAddressDto;
import com.Auth.Web.Response;

@Service
public class EndUserService {
	
	@Autowired
	private EndUserDao endUserDao;
	
	public Response initRegistration(EndUserDto userRequest) {
		return endUserDao.initRegistration(userRequest);
	}
	
	public Response verifyEmail(Map<String, String> request) {
		return endUserDao.veifyEmail(request);
	}
	
	public Response passwordSetUp(EndUserDto userRequest) {
		return endUserDao.passwordSetUp(userRequest);
	}
	
	public Response userAdressInformation(UserAddressDto addressRequest) {
		return endUserDao.userAdressInformation(addressRequest);
	}
}
