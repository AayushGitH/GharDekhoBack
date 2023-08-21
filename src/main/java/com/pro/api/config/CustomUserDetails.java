package com.pro.api.config;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.pro.api.entities.User;

public class CustomUserDetails implements UserDetails
{
	private String Name;
	private String Password;
	private List<GrantedAuthority> authorities;
	
	public CustomUserDetails(User user)
	{
		this.Name = user.getEmail();
		this.Password = user.getPassword();
		this.authorities = Arrays.stream(user.getRole().split(",")).map(SimpleGrantedAuthority::new).collect(Collectors.toList()); // Collecting roles in authorities
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() 
	{
		return authorities;
	}

	@Override
	public String getPassword() 
	{
		return Password;
	}

	@Override
	public String getUsername() 
	{
		return Name;
	}

	@Override
	public boolean isAccountNonExpired() 
	{
		return true;
	}

	@Override
	public boolean isAccountNonLocked() 
	{
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() 
	{
		return true;
	}

	@Override
	public boolean isEnabled() 
	{
		return true;
	}

}
