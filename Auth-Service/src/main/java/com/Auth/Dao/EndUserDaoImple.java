package com.Auth.Dao;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import com.Auth.Dto.EndUserDto;
import com.Auth.Dto.UserAddressDto;
import com.Auth.Enum.EmailVerifiedStatus;
import com.Auth.Model.EndUser;
import com.Auth.Model.UserAddress;
import com.Auth.Repository.EndUserRepository;
import com.Auth.Repository.UserAddressRepository;
import com.Auth.Service.EmailService;
import com.Auth.Service.Security.JwtUtil;
import com.Auth.Web.Response;
import com.Auth.Web.ResponseStatus;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.mail.MessagingException;

@Repository
public class EndUserDaoImple implements EndUserDao {
	
	@Autowired
	private EndUserRepository userRepo;
	
	@Autowired
	private UserAddressRepository addressRepo;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	private static final Pattern PASSWORD_PATTERN = Pattern.compile(
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()\\-_=+\\[\\]{};:'\",.\\/?|]).{8,40}$"
    );
	
    private static final Pattern DISALLOWED_CHARS_PATTERN = Pattern.compile("[ `~<>\\\\]");
	
	@Override
	public Response initRegistration(EndUserDto userRequest) {

		Response response = null;
		
		response = userFormCheck(userRequest);
		if(response != null) {
			return response;
		}
		
		String email = userRequest.getEmail().toLowerCase();
		Optional<EndUser> endUser = userRepo.findByEmail(email);
		if(endUser.isPresent()) {
			response = new Response(ResponseStatus.EMAIL_ALREADY_EXISTS);
			return response;
		}

		EndUser user = new EndUser();
		user.setFirstName(userRequest.getFirstName().trim());
		user.setLastName(userRequest.getLastName().trim());
		user.setEmail(userRequest.getEmail().trim());
		user.setIsEmailVerfied(EmailVerifiedStatus.valueOf(userRequest.getEmailVerifiedStatus().trim().toUpperCase()));
		userRepo.save(user);

		try {
			emailService.sendVerificationMail(userRequest);
		} catch (MessagingException | IOException e) {
			response = new Response(ResponseStatus.BAD_REQUEST);
			response.setMessage("Problem in Sending Mail");
			return response;
		}

		response = new Response(ResponseStatus.REQUEST_SUCCESS);
		response.setMessage("Verification mail sent Please check your Inbox");
		response.setData(userRequest);
		
		return response;
	}
	
	@Override
	public Response veifyEmail(Map<String, String> request) {
		Response response;
		String token = request.get("token");
		String emailVerificationStatus = request.get("emailVerifiedStatus");
		
		response = new Response(ResponseStatus.VERIFICATION_LINK_EXPERIED);
		try {
			Claims claims = jwtUtil.extractAllClaims(token);
			
			String email = claims.getSubject();
			System.out.println("Email : "+email);
			Optional<EndUser> optionalUser = userRepo.findByEmail(email);
			if (optionalUser.isEmpty()) {
	            response = new Response(ResponseStatus.BAD_REQUEST);
	            response.setMessage("User not found for email: " + email);
	            return response;
	        }

	        EndUser user = optionalUser.get();

	        user.setIsEmailVerfied(EmailVerifiedStatus.valueOf(emailVerificationStatus));
	        userRepo.save(user);
			
			response = new Response(ResponseStatus.REQUEST_SUCCESS);
			response.setMessage("Email Verified Successfully");
			response.setData(emailVerificationStatus);
		}
		catch(ExpiredJwtException e) {
			response.setMessage("Verification link expired. Please register again.");
		}
		catch (JwtException e) {
			response.setMessage("Invalid verification link");
		}
		return response;
	}
	
	@Override
	public Response passwordSetUp(EndUserDto userRequest) {
		
		String passWord = userRequest.getPassword();
		String conformPassWord = userRequest.getConformPassword();
		String email = userRequest.getEmail();
		
		Response response = validatePassword(userRequest);
		if(response != null) {
			return response;
		}
		
		if(!passWord.equals(conformPassWord)) {
			response = new Response(ResponseStatus.PASSWORD_NOT_MATCHES);
			return response;
		}
		
		Optional<EndUser> optionalUser = userRepo.findByEmail(email);
		if (optionalUser.isEmpty()) {
            response = new Response(ResponseStatus.BAD_REQUEST);
            response.setMessage("User not found for email: " + email);
            return response;
        }
		EndUser user = optionalUser.get();
		user.setPassWord(passwordEncoder.encode(passWord));
		user.setConformPassWord(passwordEncoder.encode(conformPassWord));
		userRepo.save(user);
		
		response = new Response(ResponseStatus.REQUEST_SUCCESS);
		response.setMessage("Password setup was completed");
		return response;
	}
	
	@Override
	public Response userAdressInformation(UserAddressDto addressRequest) {
		Response response = validateAddress(addressRequest);

		if(response != null) {
			return response;
		}
		
		Integer userId = addressRequest.getUserId();
		Optional<UserAddress> optionalUser = addressRepo.findById(userId);
		if(optionalUser.isEmpty()) {
			response = new Response(ResponseStatus.BAD_REQUEST);
            response.setMessage("User not found for userId :" + userId);
            return response;
		}
		
		UserAddress userAddress = optionalUser.get();
		userAddress.setUserId(addressRequest.getUserId());
		userAddress.setAddressLine1(addressRequest.getAddressLine1().trim());
		userAddress.setAddressLine2(addressRequest.getAddressLine2().trim());
		userAddress.setAddressLine3(addressRequest.getAddressLine3().trim());
		userAddress.setCity(addressRequest.getCity().trim());
		userAddress.setState(addressRequest.getState().trim());
		userAddress.setNeighBorhood(addressRequest.getNeighBorhood().trim());
		userAddress.setZipCode(addressRequest.getZipCode().trim());
		userAddress.setPhoneNumber(addressRequest.getPhoneNumber().trim());
		userAddress.setAgreeTearms(addressRequest.isAgreeTearms());
		
		addressRepo.save(userAddress);
		response = new Response(ResponseStatus.REQUEST_SUCCESS);
		response.setMessage("Address is stored successfully");
		response.setData(userAddress);
		
		return response;
	}
	

	public Response validateAddress(UserAddressDto addressRequest) {
		
		Response response = new Response(ResponseStatus.MISSING_FEILDS);
		
		if(addressRequest.getAddressLine1() == null || addressRequest.getAddressLine1().isEmpty()) {
			response.setMessage("Address is Mandatory please enter");
			return response;
		}
		
		if(addressRequest.getCity() == null || addressRequest.getCity().isEmpty()) {
			response.setMessage("Please enter the City");
			return response;
		}
		
		if(addressRequest.getState() == null || addressRequest.getState().isEmpty()) {
			response.setMessage("Please enter the state");
			return response;
		}
		String phoneNumber = addressRequest.getPhoneNumber();
		if(phoneNumber == null || phoneNumber.isEmpty()) {
			response.setMessage("Please enter the phone number");
			return response;
		}
		
		if(phoneNumber.length() < 10 || phoneNumber.length() > 10) {
			response.setMessage("Phone number must be 10 digits");
			return response;
		}
		
		if(addressRequest.getZipCode().length() < 6 || addressRequest.getZipCode().length() > 6) {
			response.setMessage("Zip code must contains 6 digits");
			return response;
		}
		
		return null;
	}

	private Response validatePassword(EndUserDto userRequest) {
		Response response = new Response(ResponseStatus.PASSWORD_NOT_VALIED);
		
		String passWord = userRequest.getPassword();
		String conformPassword = userRequest.getConformPassword();
		String firstName = userRequest.getFirstName();
		String lastName = userRequest.getLastName();
		String email = userRequest.getEmail();
		
		System.out.println("password :" + passWord);
		System.out.println("Email :" + email);
		
		if(passWord == null || passWord.isEmpty()) {
			response.setMessage("Password is null or Empty");
			return response;
		}
		
		if(conformPassword == null || conformPassword.isEmpty()) {
			response.setMessage("ConformPassword is null or Empty");
			return response;
		}
		
		if(passWord.length() < 8 || passWord.length() > 40) {
			response.setMessage("Password length in between 8 to 40 letters");
			return response;
		}
		if(!PASSWORD_PATTERN.matcher(passWord).matches()) {
			response.setMessage("Password must contain at least 1 uppercase, 1 lowercase, 1 number, and 1 special character");
			return response;
		}
		if (DISALLOWED_CHARS_PATTERN.matcher(passWord).find()) {
             response.setMessage("Password contains invalid characters (spaces, `, ~, <, >, or \\).");
             return response;
        }
		String lowerPassword = passWord.toLowerCase();
        if (firstName != null && lowerPassword.contains(firstName.toLowerCase())) {
             response.setMessage("Password cannot contain your first name.");
             return response;
        }
        if (lastName != null && lowerPassword.contains(lastName.toLowerCase())) {
            response.setMessage("Password cannot contain your last name.");
            return response;
        }
        if (email != null) {
            String emailUser = email.split("@")[0];
            if (lowerPassword.contains(emailUser.toLowerCase())) {
                 response.setMessage("Password cannot contain your email address.");
                 return response;
            }
        }
		
		return null;
	}
	
	public Response userFormCheck(EndUserDto userRequest) {
		Response response = new Response(ResponseStatus.MISSING_FEILDS);
		
		if(userRequest.getFirstName() == null || userRequest.getFirstName().isEmpty()) {
			response.setMessage("User first name is null or Empty");
			return response;
		}
		if(userRequest.getLastName() == null || userRequest.getLastName().isEmpty()) {
			response.setMessage("User last name is null or Empty");
			return response;
		}
		if(userRequest.getEmail() == null || userRequest.getEmail().isEmpty()) {
			response.setMessage("User Email is null or Empty");
			return response;
		}
		
		if(!isValidEmail(userRequest.getEmail())) {
			response = new Response(ResponseStatus.BAD_REQUEST);
			response.setMessage("Invalied email formate");
			return response;
		}
		
		if(userRequest.getEmailVerifiedStatus() == null || 
		         !(userRequest.getEmailVerifiedStatus().equals("SENT") ||
		           userRequest.getEmailVerifiedStatus().equals("VERIFIED"))) {
			response.setMessage("User EmailVerified is null or not matching");
			return response;
		}
		
		return null;
	}
	
	private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return Pattern.compile(emailRegex).matcher(email).matches();
    }

}






