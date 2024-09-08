package com.babatunde.repo;

import com.babatunde.entity.CandidateDetails;
import com.babatunde.entity.CandidateSave;
import com.babatunde.entity.JobMessageBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CandidateSaveRepo extends JpaRepository<CandidateSave, Integer> {

     List<CandidateSave> findByUserId(CandidateDetails userAccountId);

     List<CandidateSave> findByJob(JobMessageBoard job);
}
