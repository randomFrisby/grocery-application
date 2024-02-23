package com.grofers.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class AppConfig {

	@Bean
	public SecurityFilterChain springSecurityConfiguration(HttpSecurity http) throws Exception {

		http.csrf(csrf -> csrf.disable())
		.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		.authorizeHttpRequests(auth ->
				auth.requestMatchers("/users/register").permitAll()
				.requestMatchers("/users/auth/login").permitAll()
				.requestMatchers("/orders/**").hasRole("CUSTOMER")
				.requestMatchers(HttpMethod.GET,"/users/admin/**").hasRole("ADMIN")
				.requestMatchers(HttpMethod.POST, "/supplier/**").hasRole("ADMIN")
				.requestMatchers(HttpMethod.POST, "/category/**").hasRole("ADMIN")
				.requestMatchers(HttpMethod.GET,"/customers/**").hasAnyRole("ADMIN","CUSTOMER")
				.anyRequest()
				.authenticated()
		).addFilterAfter(new JwtTokenGeneratorFilter(), BasicAuthenticationFilter.class)
		.addFilterBefore(new JwtTokenValidatorFilter(), BasicAuthenticationFilter.class);
//		.formLogin(form -> form
//				.usernameParameter(SecurityContextHolder.getContext().getAuthentication().getName())
//				.passwordParameter(SecurityContextHolder.getContext().getAuthentication().getName())
//				.loginPage("/users/auth/login")
//				.permitAll()
//		)
//		.httpBasic(Customizer.withDefaults());

		return http.build();

	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {

		return new BCryptPasswordEncoder();

	}

}
