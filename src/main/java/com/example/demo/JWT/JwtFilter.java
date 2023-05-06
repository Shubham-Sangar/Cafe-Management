package com.example.demo.JWT;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.google.common.base.Strings;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private CustomerUserDetailsService service;

	Claims claims = null;
	private String userName = null;

	
	@Override
	protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			FilterChain filterChain) throws ServletException, IOException {

		if (httpServletRequest.getServletPath().matches("/user/login|/user/signup|/user/forgotPassword")) {
			filterChain.doFilter(httpServletRequest, httpServletResponse);

		} else {

			String authorizationHeader = httpServletRequest.getHeader("Authorization");
			String token = null;

			if (!Strings.isNullOrEmpty(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {
				token = authorizationHeader.substring(7);
				userName = jwtUtil.extractUsername(token);
				claims = jwtUtil.extractAllClaims(token);
			}

			if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails userDetails = service.loadUserByUsername(userName);
				if (jwtUtil.validateToken(token, userDetails)) {
					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
					usernamePasswordAuthenticationToken
							.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
					SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
				}
			}

			filterChain.doFilter(httpServletRequest, httpServletResponse);

		}

	}
	
	public boolean isAdmin() {
		return "admin".equalsIgnoreCase((String)claims.get("role"));
	}
	
	public boolean isUser() {
		return "admin".equalsIgnoreCase((String)claims.get("role"));
	}
	
	public String getCurrentUser() {
		return userName;
	}

}
