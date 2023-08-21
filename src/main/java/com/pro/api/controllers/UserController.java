package com.pro.api.controllers;

import java.awt.PageAttributes.MediaType;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.logging.LogManager;

import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pro.api.dao.HouseRepository;
import com.pro.api.dao.UserRepository;
import com.pro.api.entities.Address;
import com.pro.api.entities.House;
import com.pro.api.entities.Images;
import com.pro.api.entities.User;
import com.pro.api.model.Demo;
import com.pro.api.service.HouseService;
import com.pro.api.service.JwtService;
import com.pro.api.service.UserService;

@CrossOrigin
@RestController
@RequestMapping("/user")
@PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
//@PreAuthorize("hasAnyAuthority('ROLE_USER')")
public class UserController {
	// ---------------------------------| Properties |---------------------------------
	@Autowired
	private JwtService jwtService;

	@Autowired
	private UserService userService;

	@Autowired
	private HouseService houseService;

	@Autowired
	private ObjectMapper objectMapper;

	// ---------------------------------| Handlers |---------------------------------
	// Dashboard of front
	@PostMapping("/index")
	public ResponseEntity<User> index(@RequestBody String token) {
		String username = this.jwtService.extractUsername(token);
		User user = this.userService.readUserByName(username);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(user);
	}

	// Returns all the houses to the specific user
	@PostMapping("/gethouses")
	public ResponseEntity<List<House>> gethouses(@RequestBody String token) {
		String username = this.jwtService.extractUsername(token);
		User user = this.userService.readUserByName(username);
		int id = user.getUserId();
		List<House> houses = this.houseService.readHouseByUserId(id);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(houses);
	}

	// Adds house to the specific user id
//	@PostMapping("/addhouse/{city}/{state}/{pincode}/{street}/{area}/{token}")
//	public ResponseEntity<String> addHouse(@RequestBody House house, @PathVariable("city") String city,
//			@PathVariable("state") String state, @PathVariable("pincode") String pincode,
//			@PathVariable("street") String street, @PathVariable("area") String area,
//			@PathVariable("token") String token) {
//		String username = this.jwtService.extractUsername(token);
//		User user = this.userService.readUserByName(username);
//		house.setUser(user);
//		Address address = new Address();
//		address.setArea(area);
//		address.setStreet(street);
//		address.setCity(city);
//		address.setState(state);
//		address.setPincode(pincode);
//		address.setHouse(house);
//		house.setAddress(address);
//		user.getHouses().add(house);
//		this.userService.addUser(user);
//		return ResponseEntity.status(HttpStatus.ACCEPTED).body("House is added perfectly");
//	}

	// Updates the specific house
	@PutMapping("/updateHouse")
	public ResponseEntity<String> updateHouse(@RequestBody House house) {
		House demoHouse = this.houseService.readHouse(house.getHouseId());
		System.out.println(house.getHouseId());
		System.out.println(house.getHouseStatus());
		house.setAddress(demoHouse.getAddress());
		house.setUser(demoHouse.getUser());
		this.houseService.addHouse(house);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body("House is successfully updated");
	}

	// Deletes the specific house
	@DeleteMapping("/deleteHouse/{houseId}")
	public ResponseEntity<String> deleteHouse(@PathVariable("houseId") Integer houseId) {
		House house = this.houseService.readHouse(houseId);
		// Unlinking the user
		house.setUser(null);
		this.houseService.deleteHouse(house.getHouseId());
		return ResponseEntity.status(HttpStatus.ACCEPTED).body("House is successfully deleted");
	}

	// Demo
	@PostMapping(value="/savehouse", consumes = { org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE })
	public ResponseEntity<?> demo(@RequestParam("houseData") String house, @RequestParam("addressData") String address, @RequestParam("imageData") String image, @RequestParam("file") MultipartFile file,@RequestParam("token") String token)
			throws IOException {
		House finalhouse = this.objectMapper.readValue(house, House.class);
		Address finaladdress = this.objectMapper.readValue(address, Address.class);
		Images finalimage = this.objectMapper.readValue(image, Images.class);
		String finaltoken = this.objectMapper.readValue(token, String.class);
		
		String UPLOAD_DIR = "E:\\Angular\\GharDekhoFront\\src\\assets\\houseImages";
		Files.copy(file.getInputStream(), Paths.get(UPLOAD_DIR + File.separator + file.getOriginalFilename()),
				StandardCopyOption.REPLACE_EXISTING);
		
		finalimage.setImage(file.getOriginalFilename());
		String username = this.jwtService.extractUsername(finaltoken);
		User user = this.userService.readUserByName(username);
		System.out.println(user.getName());
		System.out.println(finalhouse.getAreaType());
		System.out.println(finaladdress.getAddressId());
		System.out.println(finalimage.getImage());
		
		finalhouse.setUser(user);
		finaladdress.setHouse(finalhouse);
		finalimage.setHouse(finalhouse);
		finalhouse.setAddress(finaladdress);
		finalhouse.setImages(finalimage);
		user.getHouses().add(finalhouse);

		this.userService.addUser(user);
		
		return ResponseEntity.status(HttpStatus.ACCEPTED).body("Everything is good here , chinta mat lo aap !!");
	}

//	@PostMapping("/fileadd")
//	public ResponseEntity<?> filedatatogether(@RequestParam("file") MultipartFile file, @RequestParam("demoData") String demoData)
//	{
//		try 
//		{
//			this.logger.info("File information : {}",file.getOriginalFilename());
//			this.logger.info("Demo user data is {}", demoData);
//			// Converting string into json
//			Demo demovalue = this.objectMapper.readValue(demoData, Demo.class);
//			System.out.println("Demo user data is " + demovalue);
//			String UPLOAD_DIR = "E:\\Angular\\GharDekhoFront\\src\\assets";
//			Files.copy(file.getInputStream(), Paths.get(UPLOAD_DIR + File.separator + file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
//			
//			return ResponseEntity.status(HttpStatus.ACCEPTED).body("Everything is perfect here");
//		} 
//		catch (Exception e) 
//		{
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong in the server side");
//		}
//	}
//	

}
