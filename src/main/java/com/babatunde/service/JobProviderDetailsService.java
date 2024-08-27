package com.babatunde.service;

import com.babatunde.entity.JobProviderDetails;
import com.babatunde.repo.JobProviderDetailsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JobProviderDetailsService {

    private JobProviderDetailsRepo jobProviderDetailsRepo;

    @Autowired
    public JobProviderDetailsService(JobProviderDetailsRepo jobProviderDetailsRepo) {
        this.jobProviderDetailsRepo = jobProviderDetailsRepo;
    }

    public Optional<JobProviderDetails> findOne(Integer id) {
        return jobProviderDetailsRepo.findById(id);
    }

    public JobProviderDetails saveProvider(JobProviderDetails jobProviderDetails) {
        return jobProviderDetailsRepo.save(jobProviderDetails);
    }
}
