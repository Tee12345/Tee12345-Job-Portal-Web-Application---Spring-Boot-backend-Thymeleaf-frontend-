package com.babatunde.controller;

import com.babatunde.entity.CandidateDetails;
import com.babatunde.entity.CandidateSave;
import com.babatunde.entity.JobMessageBoard;
import com.babatunde.entity.Users;
import com.babatunde.service.CandidateDetailsService;
import com.babatunde.service.CandidateSaveService;
import com.babatunde.service.JobMessageBoardService;
import com.babatunde.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class CandidateSaveController {

    private UsersService usersService;
    private CandidateDetailsService candidateDetailsService;
    private JobMessageBoardService jobMessageBoardService;
    private CandidateSaveService candidateSaveService;

    @Autowired
    public CandidateSaveController(UsersService usersService, CandidateDetailsService candidateDetailsService,
                                   JobMessageBoardService jobMessageBoardService, CandidateSaveService candidateSaveService) {
        this.usersService = usersService;
        this.candidateDetailsService = candidateDetailsService;
        this.jobMessageBoardService = jobMessageBoardService;
        this.candidateSaveService = candidateSaveService;
    }

    @PostMapping("job-details/save/{id}")
    public String save(@PathVariable("id") int id, CandidateSave candidateSave) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUsername = authentication.getName();
            Optional<Users> user = usersService.findByEmail(currentUsername);
            Optional<CandidateDetails> candidateDetails = candidateDetailsService.getOne(user.get().getUserId());
            JobMessageBoard jobMessageBoard = jobMessageBoardService.getOne(id);
            if(candidateDetails.isPresent() && jobMessageBoard != null) {
                candidateSave.setJob(jobMessageBoard);
                candidateSave.setUserId(candidateDetails.get());
            } else {
                throw new RuntimeException("User not found");
            }
            candidateSaveService.addNew(candidateSave);
        }
        return "redirect:/console/";
    }

    @GetMapping("saved-jobs/")
    public String savedJobs(Model model) {
        List<JobMessageBoard> jobPost = new ArrayList<>();
        Object currentUserDetails = usersService.getCurrentUserProfile();

        List<CandidateSave> candidateSaveList = candidateSaveService.getCandidateJob((CandidateDetails) currentUserDetails);
        for(CandidateSave candidateSave : candidateSaveList) {
            jobPost.add(candidateSave.getJob());
        }
        model.addAttribute("jobPost", jobPost);
        model.addAttribute("user", currentUserDetails);

        return "saved-jobs";
    }
}
