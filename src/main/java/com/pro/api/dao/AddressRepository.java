package com.pro.api.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pro.api.entities.Address;

public interface AddressRepository extends JpaRepository<Address, Integer> 
{

}
