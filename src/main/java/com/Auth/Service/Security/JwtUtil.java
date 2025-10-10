package com.Auth.Service.Security;


import java.time.Instant;
import java.util.Date;
import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtUtil {
	
	private static final int VALIDITY = 3600*1000;
	
	private static final String SECRET = "afafasfafafasfasfasfafacasdasfasxASFACASDFACASDFASFASFDAFASFASDAADSCSDFADCVSGCFVADXCcadwavfsfarvf";
	
	private SecretKey getSigningKey() {
        byte[] decodedKey = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(decodedKey);
    }
	
	 public String generateToken(String email) {
	        return Jwts.builder()
	                .setSubject(email)
	                //.claim("email", email)
	                .setIssuedAt(Date.from(Instant.now()))
	                .setExpiration(Date.from(Instant.now().plusMillis(VALIDITY)))
	                .signWith(getSigningKey())
	                .compact();
	 }
	 
	 public String extractUsername(String token) {
	        try {
	            Claims claims = extractAllClaims(cleanToken(token));
	            return claims.getSubject();
	        } catch (Exception e) {
	            return null;
	        }
	  }

	  public Claims extractAllClaims(String token) {
	       return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
	  }

	  private String cleanToken(String token) {
	      if (token != null && token.startsWith("Bearer ")) {
	          return token.substring(7).trim();
	      }
	      return token;
	  }

	  public boolean validateToken(String token, UserDetails userDetails) {
	     try {
	          String extractedUsername = extractUsername(token);
	          return extractedUsername != null 
	                  && extractedUsername.equals(userDetails.getUsername()) 
	                  && !isTokenExpired(token);
	     } catch (Exception e) {
	         System.err.println("Error validating token: " + e.getMessage());
	         return false;
	     }
	  }
	  
	  private boolean isTokenExpired(String token) {
	      try {
	          Claims claims = extractAllClaims(cleanToken(token));
	          return claims.getExpiration().before(new Date());
	      } catch (Exception e) {
	    	  System.err.println("Error while checking token expiration: " + e.getMessage());
	          return true;
	      }
	  }
	 
}
