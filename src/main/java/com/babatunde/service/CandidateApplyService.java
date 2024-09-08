package com.babatunde.service;

import com.babatunde.entity.CandidateApply;
import com.babatunde.entity.CandidateDetails;
import com.babatunde.entity.JobMessageBoard;
import com.babatunde.entity.Users;
import com.babatunde.repo.CandidateApplyRepo;
import com.babatunde.repo.UsersRepo;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
public class CandidateApplyService {

    @Autowired
    private CandidateApplyRepo candidateApplyRepo;

    @Autowired
    private UsersRepo userRepo;

       public List<CandidateApply> getCandidateJobs(CandidateDetails userAccountId) {
        return candidateApplyRepo.findByUserId(userAccountId);
    }

       public List<CandidateApply> getJobsCandidate(JobMessageBoard job) {
        return candidateApplyRepo.findByJob(job);
    }

    public void addNew(CandidateApply candidateApply) {
           candidateApplyRepo.save(candidateApply);
    }
}
