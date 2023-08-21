package com.pro.api.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pro.api.entities.House;

public interface HouseRepository extends JpaRepository<House, Integer> 
{
	@Query("select h from House h where h.address.City=:City and h.address.Area=:Area and h.HouseType=:HouseType")
	List<House> findHouseByAddressCity(@Param("City") String City,@Param("Area") String Area,@Param("HouseType") String HouseType);
	
	@Query("select h from House h where h.user.userId=:userId")
	List<House> findHousesByUserId(@Param("userId") int userId);
}
