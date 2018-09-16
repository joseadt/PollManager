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

// TODO: Auto-generated Javadoc
/**
 * The Class UserService.
 *
 * @author JADT
 */
@Service
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserService implements UserDetailsService {

	/** The user repository. */
	@Autowired
	private UserProfileRepository userRepository;

	/** The password encoder. */
	@Autowired
	private PasswordEncoder passwordEncoder;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.core.userdetails.UserDetailsService#
	 * loadUserByUsername(java.lang.String)
	 */
	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		UserProfile userProfile = this.userRepository.findOneByEmail(userName);

		if (userProfile == null) {
			throw new UsernameNotFoundException("User " + userName + " not found");
		}

		return createUser(userProfile);
	}

	/**
	 * Save.
	 *
	 * @param user
	 *            the user
	 * @return the user profile
	 */
	@Transactional
	public UserProfile save(UserProfile user) {
		user.setPassword(this.passwordEncoder.encode(user.getPassword()));
		this.userRepository.save(user);
		return user;
	}

	/**
	 * Signin.
	 *
	 * @param user
	 *            the user
	 */
	public void signin(UserProfile user) {
		SecurityContextHolder.getContext().setAuthentication(authenticate(user));

	}

	/**
	 * Authenticate.
	 *
	 * @param userProfile
	 *            the user profile
	 * @return the authentication
	 */
	private Authentication authenticate(UserProfile userProfile) {
		return new UsernamePasswordAuthenticationToken(createUser(userProfile), null,
				Collections.singleton(createAuthority(userProfile)));
	}

	/**
	 * Creates the user.
	 *
	 * @param userProfile
	 *            the user profile
	 * @return the user details
	 */
	private UserDetails createUser(UserProfile userProfile) {
		return new User(userProfile.getEmail(), userProfile.getPassword(),
				Collections.singleton(createAuthority(userProfile)));
	}

	/**
	 * Creates the authority.
	 *
	 * @param userProfile
	 *            the user profile
	 * @return the granted authority
	 */
	private GrantedAuthority createAuthority(UserProfile userProfile) {
		return new SimpleGrantedAuthority(userProfile.getRole());
	}

}
