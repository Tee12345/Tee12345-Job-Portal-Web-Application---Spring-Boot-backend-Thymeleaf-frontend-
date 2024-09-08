package com.babatunde.controller;

import com.babatunde.entity.CandidateApply;
import com.babatunde.entity.CandidateDetails;
import com.babatunde.entity.CandidateSave;
import com.babatunde.entity.JobMessageBoard;
import com.babatunde.entity.JobProviderDetails;
import com.babatunde.entity.Users;
import com.babatunde.service.CandidateApplyService;
import com.babatunde.service.CandidateDetailsService;
import com.babatunde.service.CandidateSaveService;
import com.babatunde.service.JobMessageBoardService;
import com.babatunde.service.JobProviderDetailsService;
import com.babatunde.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller

public class CandidateApplyController {

    private JobMessageBoardService jobMessageBoardService;
    private UsersService usersService;
    private CandidateApplyService candidateApplyService;
    private CandidateSaveService candidateSaveService;
    private JobProviderDetailsService jobProviderDetailsService;
    private CandidateDetailsService candidateDetailsService;

    @Autowired
    public CandidateApplyController(JobMessageBoardService jobMessageBoardService, UsersService usersService,
                                    CandidateApplyService candidateApplyService, CandidateSaveService candidateSaveService,
                                    JobProviderDetailsService jobProviderDetailsService, CandidateDetailsService candidateDetailsService) {
        this.jobMessageBoardService = jobMessageBoardService;
        this.usersService = usersService;
        this.candidateApplyService = candidateApplyService;
        this.candidateSaveService = candidateSaveService;
        this.jobProviderDetailsService = jobProviderDetailsService;
        this.candidateDetailsService = candidateDetailsService;

    }

    @GetMapping("/job-details-apply/{id}")
    public String display(@PathVariable("id") int id, Model model) {
        JobMessageBoard jobDetails = jobMessageBoardService.getOne(id);
        List<CandidateApply> candidateApplyList = candidateApplyService.getJobsCandidate(jobDetails);
        List<CandidateSave> candidateSaveList = candidateSaveService.getJobCandidateDate(jobDetails);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(!(authentication instanceof AnonymousAuthenticationToken)) {
            if(authentication.getAuthorities().contains(new SimpleGrantedAuthority("Recruiter"))) {
                 JobProviderDetails user = jobProviderDetailsService.getCurrentJobProviderDetails();
                 if(user != null) {
                     model.addAttribute("applyList", candidateApplyList);
                    }
                 } else {
                     CandidateDetails user = candidateDetailsService.getCurrentCandidateDetails();
                     if(user != null) {
                         boolean exists = false;
                         boolean saved = false;
                         for(CandidateApply candidateApply : candidateApplyList) {
                             if(candidateApply.getUserId().getUserAccountId() == user.getUserAccountId()) {
                                    exists = true;
                                    break;
                             }
                         }
                         for(CandidateSave candidateSave : candidateSaveList) {
                                if(candidateSave.getUserId().getUserAccountId() == user.getUserAccountId()) {
                                    saved = true;
                                    break;
                                }
                         }
                         model.addAttribute("alreadyApplied", exists);
                         model.addAttribute("alreadySaved", saved);
                     }
                 }
            }
            CandidateApply candidateApply = new CandidateApply();
            model.addAttribute("applyJob", candidateApply);

        model.addAttribute("jobDetails", jobDetails);
        model.addAttribute("user", usersService.getCurrentUserProfile());
        return "job-details";
    }
    @PostMapping("job-details/apply/{id}")
    public String apply(@PathVariable("id") int id, CandidateApply candidateApply) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUsername = authentication.getName();
            Optional<Users> user = usersService.findByEmail(currentUsername);
            Optional<CandidateDetails> candidateDetails = candidateDetailsService.getOne(user.get().getUserId());
            JobMessageBoard jobMessageBoard = jobMessageBoardService.getOne(id);
            if(candidateDetails.isPresent() && jobMessageBoard != null) {
                candidateApply = new CandidateApply();
                candidateApply.setUserId(candidateDetails.get());
                candidateApply.setJob(jobMessageBoard);
                candidateApply.setApplyDate(new Date());
            } else {
                throw new RuntimeException("User not found");
            }
            candidateApplyService.addNew(candidateApply);
        }
        return "redirect:/console/";

    }
}
