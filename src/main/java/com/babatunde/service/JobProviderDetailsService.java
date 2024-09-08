package com.babatunde.service;

import com.babatunde.entity.JobProviderDetails;
import com.babatunde.entity.Users;
import com.babatunde.repo.JobProviderDetailsRepo;
import com.babatunde.repo.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JobProviderDetailsService {

    private JobProviderDetailsRepo jobProviderDetailsRepo;
    private UsersRepo usersRepo;

    @Autowired
    public JobProviderDetailsService(JobProviderDetailsRepo jobProviderDetailsRepo, UsersRepo usersRepo) {
        this.jobProviderDetailsRepo = jobProviderDetailsRepo;
        this.usersRepo = usersRepo;
    }

    public Optional<JobProviderDetails> findOne(Integer id) {
        return jobProviderDetailsRepo.findById(id);
    }

    public JobProviderDetails saveProvider(JobProviderDetails jobProviderDetails) {
        return jobProviderDetailsRepo.save(jobProviderDetails);
    }

    public JobProviderDetails getCurrentJobProviderDetails() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            Users users = usersRepo.findUserByEmail(currentUserName).orElseThrow(() ->
                    new UsernameNotFoundException("This user is not in our database"));
            Optional<JobProviderDetails> jobProviderDetails = findOne(users.getUserId());
            return jobProviderDetails.orElse(null);
        } else return null;
    }
}
