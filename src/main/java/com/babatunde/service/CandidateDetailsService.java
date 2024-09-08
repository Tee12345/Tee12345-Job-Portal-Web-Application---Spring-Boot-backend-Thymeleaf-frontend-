package com.babatunde.service;

import com.babatunde.entity.CandidateDetails;
import com.babatunde.entity.Users;
import com.babatunde.repo.CandidateDetailsRepo;
import com.babatunde.repo.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CandidateDetailsService {

    private CandidateDetailsRepo candidateDetailsRepo;
    private UsersRepo usersRepo;

    @Autowired
    public CandidateDetailsService(CandidateDetailsRepo candidateDetailsRepo, UsersRepo usersRepo) {
        this.candidateDetailsRepo = candidateDetailsRepo;
        this.usersRepo = usersRepo;
    }

    public Optional<CandidateDetails> getOne(Integer id) {

        return candidateDetailsRepo.findById(id);
    }

    public CandidateDetails addNew(CandidateDetails candidateDetails) {

        return candidateDetailsRepo.save(candidateDetails);
    }

    public CandidateDetails getCurrentCandidateDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUsername = authentication.getName();
            Users users = usersRepo.findUserByEmail(currentUsername).orElseThrow(() ->
                    new UsernameNotFoundException("This user is not in our database"));
            Optional<CandidateDetails> candidateDetails = getOne(users.getUserId());
            return candidateDetails.orElse(null);
        } else return null;
    }
}
