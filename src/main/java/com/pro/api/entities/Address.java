package com.pro.api.entities;



import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity
public class Address 
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int AddressId;
	private String Area;
	private String Street;
	private String City;
	private String State;
	private String Pincode;	

	@JsonBackReference
	@OneToOne
	private House house;
}
