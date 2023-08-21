package com.pro.api.service;

import java.util.List;

import com.pro.api.entities.User;

public interface UserService 
{
	// Create
	User addUser(User user);
	
	// Read
	User readUser(int userId);
	List<User> readUsers();
	User readUserByName(String username);
	List<User> readUserByRole();
	User readUserById(int id);
	
	// Update 
	User updateUser(User user);
	
	// Delete
	void deleteUser(int userId);
}
