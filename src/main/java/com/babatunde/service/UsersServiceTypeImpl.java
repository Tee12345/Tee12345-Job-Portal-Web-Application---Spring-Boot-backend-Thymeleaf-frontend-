package com.babatunde.service;

import com.babatunde.entity.*;
import com.babatunde.repo.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.util.List;

@Service
public class UsersServiceTypeImpl implements UsersServiceType{

    private UsersTypeRepo usersTypeRepo;

    @Autowired
    public UsersServiceTypeImpl(UsersTypeRepo usersTypeRepo) {
        this.usersTypeRepo = usersTypeRepo;
    }

    @Override
    public List<UsersType> findAll() {

        return usersTypeRepo.findAll();
    }
}
