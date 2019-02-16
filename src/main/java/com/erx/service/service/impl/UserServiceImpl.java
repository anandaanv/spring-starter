package com.erx.service.service.impl;

import com.erx.service.model.Role;
import com.erx.service.model.User;
import com.erx.service.model.UserDto;
import com.erx.service.repository.UserRepository;
import com.erx.service.service.UserService;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Service(value = "userService")
public class UserServiceImpl implements UserDetailsService, UserService {
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder bcryptEncoder;

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);
		if(user == null){
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), getAuthority(user));
	}

	private List<SimpleGrantedAuthority> getAuthority(User user) {
		List<SimpleGrantedAuthority> grantedAuthorities = new ArrayList<>(); // use list if you wish
		grantedAuthorities.add(new SimpleGrantedAuthority("USER"));
		for (Role role : user.getRoles()) {
			grantedAuthorities.add(new SimpleGrantedAuthority(role.getRolename()));
		}
		return grantedAuthorities;
	}

	public List<User> findAll() {
		List<User> list = new ArrayList<>();
		userRepository.findAll().iterator().forEachRemaining(list::add);
		return list;
	}

	@Override
	public void delete(int id) {
		userRepository.deleteById(id);
	}

	@Override
	public User findOne(String username) {
		return userRepository.findByUsername(username);
	}

	@Override
	public User findById(int id) {
		Optional<User> optionalUser = userRepository.findById(id);
		return optionalUser.isPresent() ? optionalUser.get() : null;
	}

    @Override
    public UserDto update(UserDto userDto) {
        User user = findById(userDto.getId());
        if(user != null) {
            BeanUtils.copyProperties(userDto, user, "password");
            userRepository.save(user);
        }
        return userDto;
    }

	@Override
	public User approve(UserDto dto) {
		User user = findOne(dto.getUsername());
		user.setEnabled(dto.isEnabled());
		return userRepository.save(user);
	}

    @Override
    public User save(UserDto user) {
	    User newUser = new User();
	    newUser.setUsername(user.getUsername());
	    newUser.setEmail(user.getEmail());
	    newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
	    newUser.setEnabled(false);
        return userRepository.save(newUser);
    }
}
