package com.babatunde.service;

import com.babatunde.entity.*;
import com.babatunde.repo.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.time.*;
import java.util.*;

@Service
public class UsersServiceImpl implements UsersService {

    private UsersRepo usersRepo;

    @Autowired
    public UsersServiceImpl(UsersRepo usersRepo) {
        this.usersRepo = usersRepo;
    }

    @Override
    public Users saveUser(Users users) {
        users.setActive(true);
        users.setRegistrationDate(new Date(System.currentTimeMillis()));

        return usersRepo.save(users);
    }


}
