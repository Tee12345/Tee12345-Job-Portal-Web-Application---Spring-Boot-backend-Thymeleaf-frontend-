package com.babatunde.service;

import com.babatunde.entity.*;
import com.babatunde.repo.*;
import com.babatunde.utils.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.*;
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private UsersRepo usersRepo;
    @Autowired
    public CustomUserDetailsService(UsersRepo usersRepo) {
        this.usersRepo = usersRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Users user = usersRepo.findUserByEmail(username).orElseThrow(() ->
                new UsernameNotFoundException("user could not be found"));

        return new CustomUserDetails(user);
    }
}
