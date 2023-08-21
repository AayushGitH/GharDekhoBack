package com.pro.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.pro.api.entities.User;
import com.pro.api.service.HouseService;
import com.pro.api.service.UserService;

@CrossOrigin
@RestController
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminController 
{
	@Autowired
	private UserService userService;
	
	@Autowired
	private HouseService houseService;
	
	@GetMapping("/users")
	public ResponseEntity<?> allUsers()
	{
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(this.userService.readUserByRole()); 
	}
	
	@GetMapping("/houses")
	public ResponseEntity<?> allHouses()
	{
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(this.houseService.readHouses()); 
	}
	
	@PutMapping("/update")
	public ResponseEntity<?> updateUser(@RequestBody User user)
	{
		User finaluser = this.userService.readUserById(user.getUserId());
		System.out.println(finaluser.getName());
		user.setHouses(finaluser.getHouses());
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(this.userService.updateUser(user));
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable("id") int id)
	{
		this.userService.deleteUser(id);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body("Successfully deleted");
	}
}
