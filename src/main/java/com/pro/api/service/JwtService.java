package com.pro.api.service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtService 
{
	
	public static final String SECRET = "48404D635166546A576E5A7234753778214125442A462D4A614E645267556B58";
	
	// Will return token who is authorized
	public String generateToken(String userName)
	{
		Map<String, Object> claims = new HashMap<>();
		return createToken(claims,userName);
	}

	public String createToken(Map<String, Object> claims, String userName) 
	{
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(userName)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis()+1000*60*30))
				.signWith(getSignKey(),SignatureAlgorithm.HS256).compact();
	}
	
	private Key getSignKey()
	{
		byte[] keyBytes = Decoders.BASE64.decode(SECRET);
		return Keys.hmacShaKeyFor(keyBytes);
	}
	
	public String extractUsername(String token)
	{
		 return extractClaim(token,Claims::getSubject);
	}

	private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) 
	{
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) 
	{
		return Jwts
				.parserBuilder()
				.setSigningKey(getSignKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
	}
	
	public Boolean validateToken(String token,UserDetails userDetails)
	{
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

	private Boolean isTokenExpired(String token) 
	{
		return extractExpiration(token).before(new Date());
	}

	private Date extractExpiration(String token) 
	{
		return extractClaim(token, Claims::getExpiration);
	}
	
	
}
