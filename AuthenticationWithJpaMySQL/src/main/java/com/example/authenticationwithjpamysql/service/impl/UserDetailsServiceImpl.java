package com.example.authenticationwithjpamysql.service.impl;

import com.example.authenticationwithjpamysql.model.MyUserDetails;
import com.example.authenticationwithjpamysql.model.User;
import com.example.authenticationwithjpamysql.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.getUserByUserName(username);
        if(user == null) {
            throw new UsernameNotFoundException("Could not find user!");
        }
        return new MyUserDetails(user);
    }
}
