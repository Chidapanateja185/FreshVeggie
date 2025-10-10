package com.Auth.Controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Auth.Dto.EndUserDto;
import com.Auth.Dto.LoginRequest;
import com.Auth.Dto.UserAddressDto;
import com.Auth.Service.EndUserService;
import com.Auth.Service.Security.JwtUtil;
import com.Auth.Web.Response;
import com.Auth.Web.ResponseStatus;


@RestController
@RequestMapping("/api")
public class EndUserController {
	
	@Autowired
	private EndUserService userService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@PostMapping("/sentEmail")
	public Response initRegistration(@RequestBody EndUserDto userRequest) {
		return userService.initRegistration(userRequest);
	}
	
	@PostMapping("/verifyEmail")
	public Response verifyEmail(@RequestBody Map<String, String> request) {
		return userService.verifyEmail(request);
	}
	
	@PostMapping("/setPassword")
	public Response passwordSetUp(@RequestBody EndUserDto userRequest) {
		return userService.passwordSetUp(userRequest);
	}
	
	@PostMapping("/loginUser")
	public String loginUser(@RequestBody LoginRequest loginRequest) {
		Authentication authentication = authenticationManager.authenticate(
	            new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
	    );
		
	    if (authentication.isAuthenticated()) {
	    	return jwtUtil.generateToken(loginRequest.getEmail());
	    } else {
	        throw new UsernameNotFoundException("Invalid user request!");
	    }
	}
	
	@PostMapping("/addAddressInfo")
	public Response userAdressInformation(@RequestBody UserAddressDto addressRequest) {
		return userService.userAdressInformation(addressRequest);
	}
}
