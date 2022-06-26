package com.raxn.util.service;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JWTUtil {
	
	@Value("${app.secret}")
	private String SECRET_KEY;
	
	//@Autowired
	//CustomUserDetails userdetails;
	
	//read subject/username
	public String getUsername(String token) {
        return getClaims(token).getSubject();
    }

	//get expiration date of a token
    public Date getExpirationDate(String token) {
        return getClaims(token).getExpiration();
    }

    //get claims
    public Claims getClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY.getBytes()).parseClaimsJws(token).getBody();
    }

    //validate exp date
    public Boolean isTokenExpired(String token) {
        Date expDate = getExpirationDate(token);
        return expDate.before(new Date(System.currentTimeMillis()));
    }
    
  //generate token
    public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();

		Collection<? extends GrantedAuthority> roles = userDetails.getAuthorities();

		if (roles.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
			claims.put("isAdmin", true);
		}
		if (roles.contains(new SimpleGrantedAuthority("ROLE_USER"))) {
			claims.put("isUser", true);
		}

		return createToken(claims, userDetails.getUsername());
	}

    
    private String createToken(Map<String, Object> claims, String subject) {
    	return Jwts.builder().setClaims(claims)
				.setSubject(subject.trim())
				.setIssuer("RECHARGEAXN TECH")
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(20)))
				.signWith(SignatureAlgorithm.HS512, SECRET_KEY.getBytes())
				.compact();
    }
    
    //refresh token
    public String generateRefreshToken(Map<String, Object> claims, String subject) {
    	
    	return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10)))
				.signWith(SignatureAlgorithm.HS512, SECRET_KEY.getBytes()).compact();
	}

    //validate username in token and exp date
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

}
