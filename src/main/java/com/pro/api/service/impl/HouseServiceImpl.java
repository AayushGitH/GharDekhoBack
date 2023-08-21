package com.pro.api.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pro.api.dao.HouseRepository;
import com.pro.api.entities.House;
import com.pro.api.service.HouseService;

@Service
public class HouseServiceImpl implements HouseService 
{
	@Autowired
	private HouseRepository houseRepository;
	
	// Create
	@Override
	public House addHouse(House house) 
	{
		return this.houseRepository.save(house);
	}

	// Read
	@Override
	public House readHouse(int houseId) 
	{
		return this.houseRepository.findById(houseId).orElseThrow();
	}

	@Override
	public List<House> readHouses() 
	{
		return this.houseRepository.findAll();
	}
	
	@Override
	public List<House> readHouseByAddress(String City, String Area, String HouseType) 
	{
		return this.houseRepository.findHouseByAddressCity(City, Area, HouseType);
	}
	
	@Override
	public List<House> readHouseByUserId(int userId) 
	{
		return this.houseRepository.findHousesByUserId(userId);
	}


	// Update
	@Override
	public House updateHouse(House house) 
	{
		return this.houseRepository.save(house);
	}

	// Delete
	@Override
	public void deleteHouse(int houseId) 
	{
		this.houseRepository.deleteById(houseId);
	}

	
	

}
