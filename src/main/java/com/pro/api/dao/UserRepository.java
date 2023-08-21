package com.pro.api.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pro.api.entities.User;

public interface UserRepository extends JpaRepository<User, Integer> 
{
	@Query("select u from User u where u.Email=:Email")
	User findByEmail(@Param("Email") String Email);
	
	@Query("select u from User u where u.Role=:role")
	List<User> findUsers(String role);
	
}
