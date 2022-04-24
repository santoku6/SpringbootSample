package com.raxn.util.service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JWTUtil {
	
	@Value("${app.secret}")
	private String SECRET_KEY;
	
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
    public String createToken(String subject) {
    	return Jwts.builder()
				.setSubject(subject.trim())
				.setIssuer("RECHARGEAXN TECH")
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(3)))
				.signWith(SignatureAlgorithm.HS512, SECRET_KEY.getBytes())
				.compact();
    }

    //validate username in token and exp date
    public Boolean validateToken(String token, String identifier) {
        final String username = getUsername(token);
        return (username.equals(identifier) && !isTokenExpired(token));
    }

}
