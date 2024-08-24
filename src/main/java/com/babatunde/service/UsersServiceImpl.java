package com.babatunde.service;

import com.babatunde.entity.*;
import com.babatunde.repo.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.*;
import org.springframework.stereotype.*;

import java.time.*;
import java.util.*;

@Service
public class UsersServiceImpl implements UsersService {

    private UsersRepo usersRepo;
    private CandidateDetailsRepo candidateDetailsRepo;
    private JobProviderDetailsRepo jobProviderDetailsRepo;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UsersServiceImpl(UsersRepo usersRepo, CandidateDetailsRepo candidateDetailsRepo,
                            JobProviderDetailsRepo jobProviderDetailsRepo, PasswordEncoder passwordEncoder) {

        this.usersRepo = usersRepo;
        this.candidateDetailsRepo = candidateDetailsRepo;
        this.jobProviderDetailsRepo = jobProviderDetailsRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Users saveUser(Users users) {
        users.setActive(true);
        users.setRegistrationDate(new Date(System.currentTimeMillis()));
        users.setPassword(passwordEncoder.encode(users.getPassword()));
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

    @Override
    public Object getCurrentUserProfile() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(!(authentication instanceof AnonymousAuthenticationToken)) {
            String username = authentication.getName();
            Users users = usersRepo.findUserByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found "));
            int userId = users.getUserId();
            if(authentication.getAuthorities().contains(new SimpleGrantedAuthority("Recruiter"))) {
                JobProviderDetails jobProviderDetails = jobProviderDetailsRepo.findById(userId)
                        .orElse(new JobProviderDetails());
                return jobProviderDetails;
            } else{
                CandidateDetails candidateDetails = candidateDetailsRepo.findById(userId)
                        .orElse(new CandidateDetails());
                return candidateDetails;
            }
        }
        return null;
    }


}
