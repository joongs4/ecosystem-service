package com.kakaopay.ecosystem.jwt;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtil implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3878505844565163307L;

	@Value("${jwt.accessTokenValidHour:1}")
	private Long inputAccessTokenValidHour;

	@Value("${jwt.refreshTokenValidHour:24}")
	private Long inputRefreshTokenValidHour;

	@Value("${jwt.secret:kakaopay}")
	private String secret;

	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}

	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	public String generateRefreshToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();

		claims.put("scope", Arrays.asList("REFRESH_TOKEN"));
		long validHour = inputRefreshTokenValidHour * 60 * 60;
		return doGenerateToken(claims, userDetails.getUsername(), validHour);
	}

	public String generateAccessToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("scope", Arrays.asList("ROLE_USER"));
		long validHour = inputAccessTokenValidHour * 60 * 60;
		return doGenerateToken(claims, userDetails.getUsername(), validHour);
	}

	private String doGenerateToken(Map<String, Object> claims, String subject, long validHour) {

		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + validHour * 1000))
				.signWith(SignatureAlgorithm.HS512, secret).compact();
	}

	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = getUsernameFromToken(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

	public boolean isRefreshToken(String token) {
		final Claims claims = getAllClaimsFromToken(token);

		Object objScopes = claims.get("scope");
		if (objScopes != null && objScopes instanceof List) {
			List scopes = (List) objScopes;
			for (Object scope : scopes) {
				String strScope = String.valueOf(scope);
				if ("REFRESH_TOKEN".equalsIgnoreCase(strScope)) {
					return true;
				}
			}
		}
		return false;

	}
}
