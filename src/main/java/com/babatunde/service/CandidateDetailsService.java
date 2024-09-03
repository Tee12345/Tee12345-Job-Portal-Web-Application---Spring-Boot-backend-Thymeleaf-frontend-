package com.babatunde.service;

import com.babatunde.entity.CandidateDetails;
import com.babatunde.repo.CandidateDetailsRepo;
import com.babatunde.repo.UsersRepo;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CandidateDetailsService {

    private CandidateDetailsRepo candidateDetailsRepo;
    private UsersRepo usersRepo;

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
}
