package com.pro.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.pro.api.dao.UserRepository;
import com.pro.api.entities.User;

@Component
public class UserDetailsServiceImpl implements UserDetailsService 
{
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException 
	{
		User user = this.userRepository.findByEmail(username);
		if(user == null)
		{
			throw new UsernameNotFoundException("Could not found user !!");
		}
		CustomUserDetails customUserDetail = new CustomUserDetails(user);
		return customUserDetail;
	}

}
