package com.kakaopay.ecosystem.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.kakaopay.ecosystem.exception.BadRequestException;
import com.kakaopay.ecosystem.jwt.JwtTokenUtil;
import com.kakaopay.ecosystem.service.UserService;

import io.jsonwebtoken.ExpiredJwtException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	private static final Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class.getCanonicalName());

	@Autowired
	private UserService userService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		final String requestTokenHeader = request.getHeader("Authorization");
		String username = null;
		String jwtToken = null;
		if (requestTokenHeader != null) {
			if (requestTokenHeader.startsWith("Bearer ") || requestTokenHeader.startsWith("bearer ")) {
				jwtToken = requestTokenHeader.substring(7);
				try {
					username = jwtTokenUtil.getUsernameFromToken(jwtToken);
				} catch (IllegalArgumentException e) {
					logger.error("Unable to get JWT Token");
					throw new BadRequestException("Unable to get JWT Token");
				} catch (ExpiredJwtException e) {
					logger.error("JWT Token has expired");
					throw new BadRequestException("JWT Token has expired");
				}
			} else {
				logger.warn("JWT Token does not begin with Bearer String");

			}
		}
		try {
			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails userDetails = this.userService.loadUserByUsername(username);

				if (jwtTokenUtil.isRefreshToken(jwtToken)) {
					if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
						// Valid
						SimpleGrantedAuthority refreshTokenAuthority = new SimpleGrantedAuthority("ROLE_REFRESH_TOKEN");
						List<GrantedAuthority> authorities = new ArrayList<>();
						authorities.add(refreshTokenAuthority);
						UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
								userDetails, null, authorities);
						usernamePasswordAuthenticationToken
								.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

						SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

					} else {

						throw new BadCredentialsException("Invalid refresh token");
					}

				} else if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {

					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
					usernamePasswordAuthenticationToken
							.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
				}
			}
		} catch (UsernameNotFoundException e) {
			logger.error(e.getMessage());

			throw e;
		}

		chain.doFilter(request, response);
	}
}