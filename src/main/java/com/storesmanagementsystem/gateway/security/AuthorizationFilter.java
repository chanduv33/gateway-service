package com.storesmanagementsystem.gateway.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.storesmanagementsystem.gateway.service.UserInfoBean;
import com.storesmanagementsystem.gateway.service.UserService;

import io.jsonwebtoken.Jwts;

public class AuthorizationFilter extends BasicAuthenticationFilter {

	Environment environment;
	private UserService service;

	public AuthorizationFilter(AuthenticationManager authenticationManager, Environment environment,
			UserService service) {
		super(authenticationManager);
		this.environment = environment;
		this.service = service;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String autherizationHeader = request.getHeader(environment.getProperty("auth.header.name"));
		if(null == autherizationHeader) {
			autherizationHeader = request.getHeader("authorization");
		}
		if (null == autherizationHeader
				|| !autherizationHeader.startsWith(environment.getProperty("auth.header.token.prefix"))) {
			chain.doFilter(request, response);
			return;
		}

		UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		chain.doFilter(request, response);

	}

	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
		String autherizationHeader = request.getHeader(environment.getProperty("auth.header.name"));
		if (autherizationHeader == null) {
			return null;
		}
		String token = autherizationHeader.replace(environment.getProperty("auth.header.token.prefix"), "").trim();

		String userId = Jwts.parser().setSigningKey(environment.getProperty("auth.header.token")).parseClaimsJws(token)
				.getBody().getSubject();
		if (userId == null) {
			return null;
		}
		UserInfoBean user = service.getUserByUserUd(userId);
		SimpleGrantedAuthority authority1 = new SimpleGrantedAuthority(user.getRole());

		List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		authorities.add(authority1);
		return new UsernamePasswordAuthenticationToken(user.getUserId(), null, authorities);
	}

}
