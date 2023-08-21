package com.pro.api.controllers;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pro.api.dao.AddressRepository;
import com.pro.api.dao.HouseRepository;
import com.pro.api.dao.UserRepository;
import com.pro.api.entities.Address;
import com.pro.api.entities.House;
import com.pro.api.entities.User;
import com.pro.api.model.AuthRequest;
import com.pro.api.service.HouseService;
import com.pro.api.service.JwtService;
import com.pro.api.service.UserService;

@CrossOrigin
@RestController
@RequestMapping("/project")
public class MainController 
{
	// ------------------------------| Properties |------------------------------
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private UserService userService;

	@Autowired
	private HouseService houseService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtService jwtService;

	// ------------------------------| Handlers |------------------------------
	// Renders the home page
	@GetMapping("/home")
	public String welcome() {
		return "Hey I am the first text in the project";
	}

	// Authentication through JWT
	@PostMapping("/authenticate")
	public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
		if (authentication.isAuthenticated()) {
			return this.jwtService.generateToken(authRequest.getUsername());
		} else {
			throw new UsernameNotFoundException("Invalid user request");
		}
	}

	// Signup User Handler
	@PostMapping(value = "/signup", consumes = { org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE })
	public ResponseEntity<?> signupwithfile(@RequestParam("file") MultipartFile file,
			@RequestParam("userData") String userData) {
		try {
			String UPLOAD_DIR = "E:\\Angular\\GharDekhoFront\\src\\assets\\userImages";
			Files.copy(file.getInputStream(), Paths.get(UPLOAD_DIR + File.separator + file.getOriginalFilename()),
					StandardCopyOption.REPLACE_EXISTING);

			// Converting string into json
			User user = this.objectMapper.readValue(userData, User.class); 
			user.setRole("ROLE_USER");
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			user.setImage(file.getOriginalFilename());

			// Saving user
			this.userService.addUser(user);

			return ResponseEntity.status(HttpStatus.ACCEPTED).body("Everything is perfect here");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Something went wrong in the server side");
		}
	}
	
	

	// Search houses by city
	@GetMapping("/search/{City}/{Area}/{HouseType}")
	public ResponseEntity<List<House>> searchquery(@PathVariable("City") String City,
			@PathVariable("Area") String Area,@PathVariable("HouseType") String HouseType) {
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(this.houseService.readHouseByAddress(City, Area, HouseType));
	}

}
