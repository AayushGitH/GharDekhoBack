package com.pro.api.entities;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class House 
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int HouseId;
	private String HouseArea;
	private String HouseType;
	private String AreaType;
	private String HousePrice;
	private String HouseStatus;
	private String Contact;
	
	@JsonBackReference
	@ManyToOne
	private User user;
	
	@JsonManagedReference
	@OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "house")
	private Address address;
	
	@JsonManagedReference
	@OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "house")
	private Images images;
}
