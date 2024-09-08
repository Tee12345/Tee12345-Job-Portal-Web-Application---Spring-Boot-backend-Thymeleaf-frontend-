package com.babatunde.repo;


import com.babatunde.entity.CandidateApply;
import com.babatunde.entity.CandidateDetails;
import com.babatunde.entity.JobMessageBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CandidateApplyRepo extends JpaRepository<CandidateApply, Integer> {

    List<CandidateApply> findByUserId(CandidateDetails userId);

    List<CandidateApply> findByJob(JobMessageBoard job);


}
