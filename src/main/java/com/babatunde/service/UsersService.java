package com.babatunde.service;

import com.babatunde.entity.*;

import java.util.*;

public interface UsersService {
    public Users saveUser (Users users);
    public Optional<Users> getUserByEmail(String email);



}
