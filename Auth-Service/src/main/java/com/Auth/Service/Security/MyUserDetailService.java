package com.Auth.Service.Security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.Auth.Model.EndUser;
import com.Auth.Repository.EndUserRepository;

@Service
public class MyUserDetailService implements UserDetailsService {
	
	@Autowired
	private EndUserRepository userRepo;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<EndUser> user = userRepo.findByEmail(email.toLowerCase());
        if (user.isPresent()) {
        	EndUser userObj = user.get();
            return User.builder()
                    .username(userObj.getEmail())
                    .password(userObj.getPassWord())
                    .build();
        } else {
            throw new UsernameNotFoundException("User not found.");
        }
	}

}
