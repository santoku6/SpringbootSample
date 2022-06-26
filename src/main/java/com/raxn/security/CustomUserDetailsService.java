package com.raxn.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.raxn.entity.User;
import com.raxn.repository.UserRepository;
import com.raxn.util.service.CommonServiceUtil;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomUserDetailsService.class);

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		LOGGER.info("Entered loadUserByUsername() -> Start");
		LOGGER.info("username=" + username);
		User user = null;
		if(username.contains("@") || CommonServiceUtil.mobileChecker(username)) {
			user = userRepository.findByEmailOrMobile(username);
		} else {
			user = userRepository.findByUsername(username);
		}
		
		if (user == null) {
			LOGGER.info("user not found");
			throw new UsernameNotFoundException("User " + username + " not found");
		}
		LOGGER.info("user=" + user.getEmail());
		return new CustomUserDetails(user);

	}

}
