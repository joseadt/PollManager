package es.udc.jadt.arbitrium.model.service.user;

import java.util.Collections;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import es.udc.jadt.arbitrium.model.entities.userprofile.UserProfile;
import es.udc.jadt.arbitrium.model.entities.userprofile.UserProfileRepository;

@Service
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserService implements UserDetailsService {

	@Autowired
	private UserProfileRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		UserProfile userProfile = userRepository.findOneByEmail(userName);

		if(userProfile == null) {
			throw new UsernameNotFoundException("User " + userName + " not found");
		}

		return createUser(userProfile);
	}

	@Transactional
	public UserProfile save(UserProfile user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		userRepository.save(user);
		return user;
	}

	public void signin(UserProfile user) {
		SecurityContextHolder.getContext().setAuthentication(authenticate(user));

	}

	private Authentication authenticate(UserProfile userProfile) {
		return new UsernamePasswordAuthenticationToken(createUser(userProfile), null,
				Collections.singleton(createAuthority(userProfile)));
	}

	private UserDetails createUser(UserProfile userProfile) {
		return new User(userProfile.getEmail(), userProfile.getPassword(),
				Collections.singleton(createAuthority(userProfile)));
	}

	private GrantedAuthority createAuthority(UserProfile userProfile) {
		return new SimpleGrantedAuthority(userProfile.getRole());
	}

}
