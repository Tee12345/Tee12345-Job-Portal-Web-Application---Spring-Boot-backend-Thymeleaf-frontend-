package com.babatunde.service;

import com.babatunde.entity.*;

import java.util.*;

public interface UsersService {
     Users saveUser (Users users);
     Optional<Users> getUserByEmail(String email);
     Object getCurrentUserProfile();
}
