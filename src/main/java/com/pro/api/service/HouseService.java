package com.pro.api.service;

import java.util.List;

import com.pro.api.entities.House;

public interface HouseService 
{
	// Create
	House addHouse(House house);
	
	//Read
	House readHouse(int houseId);
	List<House> readHouses();
	List<House> readHouseByAddress(String City, String Area, String HouseType);
	List<House> readHouseByUserId(int userId);
	
	// Update
	House updateHouse(House house);
	
	// Delete
	void deleteHouse(int houseId);
}
