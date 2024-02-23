package com.grofers.security.custom;

import com.grofers.enums.Role;
import com.grofers.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerUserDetailsService implements UserDetailsService{

	
	
	
	@Autowired
	private UserRepository userRepository;
	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		
		Optional<com.grofers.model.User> opt= userRepository.findByEmail(username);

		if(opt.isPresent()) {
			
			
			//return new CustomerUserDetails(opt.get());

			com.grofers.model.User user = opt.get();
			
			List<GrantedAuthority> authorities = new ArrayList<>();
		
			
			
			// List<Authority> auths= user.getAuthorities();
			Role role = user.getRole();
			
			/*for(Authority auth:auths) {
				SimpleGrantedAuthority sga=new SimpleGrantedAuthority(auth.getName());
				System.out.println("siga "+sga);
				authorities.add(sga);
			}*/

			SimpleGrantedAuthority sga= new SimpleGrantedAuthority(role.toString());
			authorities.add(sga);
			
			System.out.println("granted authorities "+authorities);
			
			
			return new User(user.getEmail(), user.getPassword(), authorities);
			
			
			
		} else
			throw new BadCredentialsException("User Details not found with this username: "+username);

	}

}
