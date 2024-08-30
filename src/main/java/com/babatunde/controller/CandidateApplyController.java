package com.babatunde.controller;

import com.babatunde.entity.JobMessageBoard;
import com.babatunde.service.JobMessageBoardService;
import com.babatunde.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller

public class CandidateApplyController {

    private JobMessageBoardService jobMessageBoardService;
    private UsersService usersService;

    @Autowired
    public CandidateApplyController(JobMessageBoardService jobMessageBoardService, UsersService usersService) {
        this.jobMessageBoardService = jobMessageBoardService;
        this.usersService = usersService;
    }

    @GetMapping("/job-details-apply/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        JobMessageBoard jobDetails = jobMessageBoardService.getOne(id);

        model.addAttribute("jobDetails", jobDetails);
        model.addAttribute("user", usersService.getCurrentUserProfile());

        return "job-details";
    }
}
