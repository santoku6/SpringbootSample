package com.raxn.security;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.raxn.entity.User;
import com.raxn.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {	
	Logger LOGGER = LoggerFactory.getLogger(CustomUserDetailsService.class);

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		LOGGER.info("Entered loadUserByUsername() -> Start");
		LOGGER.info("username=" + username);
		
		User user = userRepository.findByUsername(username);
		if (user == null) {
			LOGGER.info("User " + username + " not found");
			throw new UsernameNotFoundException("User " + username + " not found");
		} else {
			LOGGER.info("user="+ReflectionToStringBuilder.toString(user));
			return new CustomUserDetails(user);
		}
	}

}
