package com.babatunde.service;

import com.babatunde.entity.CandidateDetails;
import com.babatunde.entity.CandidateSave;
import com.babatunde.entity.JobMessageBoard;
import com.babatunde.repo.CandidateSaveRepo;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
public class CandidateSaveService {

    @Autowired
    private CandidateSaveRepo candidateSaveRepo;

    public List<CandidateSave> getCandidateJob(CandidateDetails userAccountId) {
        return candidateSaveRepo.findByUserId(userAccountId);
    }

    public List<CandidateSave> getJobCandidateDate(JobMessageBoard job) {
        return candidateSaveRepo.findByJob(job);
    }

    public void addNew(CandidateSave candidateSave) {
         candidateSaveRepo.save(candidateSave);
    }

}
