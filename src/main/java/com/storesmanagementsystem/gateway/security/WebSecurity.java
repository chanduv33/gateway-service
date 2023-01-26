package com.storesmanagementsystem.gateway.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import com.storesmanagementsystem.gateway.service.UserService;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

	private final Environment environment;

	private UserService service;

	public WebSecurity(Environment environment, UserService service) {
		this.service = service;
		this.environment = environment;
	}

	@Autowired
	private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.headers().frameOptions().disable();
		http.headers().disable().cors();
		http.authorizeRequests().antMatchers("/security-service/User/register", "/security-service/User/update")
				.permitAll().and().exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint).and()
				.authorizeRequests()
				.antMatchers("/manufacturer-service/Product", "/manufacturer-service/Product/Cost",
						"/manufacturer-service/Orders/Payments", "/security-service/Order/deliveredOn",
						"/manufacturer-service/Product", "/security-service/Order/changeStatus",
						"/manufacturer-service/Products")
				.hasRole("MANUFACTURER").and().authorizeRequests()
				.antMatchers("/admin-service/User", "/admin-service/Users", "/admin-service/User").hasRole("ADMIN")
				.and().authorizeRequests()
				.antMatchers("/dealer-service/Product/Price", "/dealer-service/Products", "/dealer-service/Product",
						"/dealer-service/Product/Price/Min", "/dealer-service/Products/getMansProds", "/Orders/deliveredOn")
				.hasRole("DEALER").and().authorizeRequests()
				.antMatchers("/customer-service/Customer/buyProduct", "/customer-service/Products").hasRole("CUSTOMER")
				.and().authorizeRequests()
				.antMatchers("/security-service/Orders", "/security-service/Order", "/security-service/Cart/addtocart",
						"/security-service/Cart/getItems", "/security-service/Cart/remItem")
				.hasAnyRole("CUSTOMER", "DEALER").anyRequest().authenticated().and()
				.addFilter(new AuthorizationFilter(authenticationManager(), environment, service));

		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

	}
}
