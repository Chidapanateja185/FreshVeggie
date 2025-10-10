package com.Auth.Service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.Auth.Dto.EndUserDto;
import com.Auth.Service.Security.JwtUtil;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
	
	@Value("${spring.mail.username}")
	private String sendMail;
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	public void sendVerificationMail(EndUserDto userRequest) throws MessagingException, IOException {
		
		String email = userRequest.getEmail();
		String token = jwtUtil.generateToken(email);
		String verificationLink = "https://signup.freshveggie.com/?verify_email="+token;
		
		MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        
        System.out.println("token : "+ token);
        
        helper.setFrom(sendMail);
        helper.setTo(userRequest.getEmail());
        helper.setSubject("Verify your email address");
        ClassPathResource resource = new ClassPathResource("templates/commonTemplate.html");
	    
	    String html = "<h2>Welcome to FreshVeggie!</h2>"
                + "<p>Click the button below to verify your email:</p>"
                + "<a href='" + verificationLink + "' "
                + "style='background-color:#4CAF50;color:white;padding:10px 20px;text-decoration:none;'>"
                + "Verify Email</a>"
                + "<p>This link will expire in 1 hour.</p>";
	    
        helper.setText(html, true);
        mailSender.send(message);
	}
}




