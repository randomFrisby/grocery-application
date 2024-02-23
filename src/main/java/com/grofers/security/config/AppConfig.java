package com.grofers.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
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
				auth.requestMatchers("/users/signup").permitAll()
				.requestMatchers("/users/login").permitAll()
				.requestMatchers(HttpMethod.GET,"/users/admin/all").hasRole("ADMIN")
				.requestMatchers("/orders/**").permitAll()
				.requestMatchers(HttpMethod.POST, "/supplier/**").hasRole("ADMIN")
				.requestMatchers(HttpMethod.POST, "/category/**").hasRole("ADMIN")
				.requestMatchers(HttpMethod.GET,"/customers/**").hasAnyRole("ADMIN","USER")
				.anyRequest()
				.authenticated()
		).addFilterAfter(new JwtTokenGeneratorFilter(), BasicAuthenticationFilter.class)
		.addFilterBefore(new JwtTokenValidatorFilter(), BasicAuthenticationFilter.class)
		.formLogin(form -> form
				.usernameParameter(SecurityContextHolder.getContext().getAuthentication().getName())
				.passwordParameter(SecurityContextHolder.getContext().getAuthentication().getName())
				.loginPage("/users/login")
				.permitAll()
		)
		.httpBasic(Customizer.withDefaults());

		return http.build();

	}

	@Bean
	public AuthenticationManager authenticationManager(
			UserDetailsService userDetailsService,
			PasswordEncoder passwordEncoder) {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService);
		authenticationProvider.setPasswordEncoder(passwordEncoder);

		return new ProviderManager(authenticationProvider);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {

		return new BCryptPasswordEncoder();

	}

}
