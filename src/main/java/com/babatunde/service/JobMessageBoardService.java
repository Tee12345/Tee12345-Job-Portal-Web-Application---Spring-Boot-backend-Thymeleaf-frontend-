package com.babatunde.service;

import com.babatunde.entity.JobMessageBoard;
import com.babatunde.repo.JobMessageBoardRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobMessageBoardService {

    public JobMessageBoardRepo jobMessageBoardRepo;

    @Autowired
    public JobMessageBoardService(JobMessageBoardRepo jobMessageBoardRepo) {
        this.jobMessageBoardRepo = jobMessageBoardRepo;
    }

    public JobMessageBoard addNew(JobMessageBoard jobMessageBoard) {
            return jobMessageBoardRepo.save(jobMessageBoard);

    }
}
