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
    private CandidateDetailsRepo candidateDetailsRepo;
    private JobProviderDetailsRepo jobProviderDetailsRepo;

    @Autowired
    public UsersServiceImpl(UsersRepo usersRepo, CandidateDetailsRepo candidateDetailsRepo,
                            JobProviderDetailsRepo jobProviderDetailsRepo) {

        this.usersRepo = usersRepo;
        this.candidateDetailsRepo = candidateDetailsRepo;
        this.jobProviderDetailsRepo = jobProviderDetailsRepo;
    }

    @Override
    public Users saveUser(Users users) {
        users.setActive(true);
        users.setRegistrationDate(new Date(System.currentTimeMillis()));
        Users savedUser = usersRepo.save(users);
        int userTypeId = users.getUserTypeId().getUserTypeId();
        if(userTypeId == 1) {
            jobProviderDetailsRepo.save(new JobProviderDetails(savedUser));
        } else {
            candidateDetailsRepo.save(new CandidateDetails(savedUser));
        }
        return savedUser;
    }
    @Override
    public Optional<Users> getUserByEmail(String email) {
        return usersRepo.findUserByEmail(email);
    }


}
