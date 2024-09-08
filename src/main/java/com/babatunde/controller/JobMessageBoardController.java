package com.babatunde.controller;

import com.babatunde.entity.CandidateApply;
import com.babatunde.entity.CandidateDetails;
import com.babatunde.entity.CandidateSave;
import com.babatunde.entity.JobMessageBoard;
import com.babatunde.entity.JobProviderDetails;
import com.babatunde.entity.JobProviderJobsDto;
import com.babatunde.entity.Users;
import com.babatunde.service.CandidateApplyService;
import com.babatunde.service.CandidateSaveService;
import com.babatunde.service.JobMessageBoardService;
import com.babatunde.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Controller
public class JobMessageBoardController {

    private UsersService usersService;
    private JobMessageBoardService jobMessageBoardService;
    private CandidateApplyService candidateApplyService;
    private CandidateSaveService candidateSaveService;

    @Autowired
    public JobMessageBoardController(UsersService usersService, JobMessageBoardService jobMessageBoardService,
                                     CandidateApplyService candidateApplyService, CandidateSaveService candidateSaveService) {
        this.usersService = usersService;
        this.jobMessageBoardService = jobMessageBoardService;
        this.candidateApplyService = candidateApplyService;
        this.candidateSaveService = candidateSaveService;
    }

    @GetMapping("/console/")
    public String searchJobs(Model model,
                             @RequestParam(name = "job",required = false) String job,
                             @RequestParam(name = "location", required = false) String location,
                             @RequestParam(name = "partTime", required = false) String partTime,
                             @RequestParam(name = "fullTime", required = false) String fullTime,
                             @RequestParam(name = "freelance", required = false) String freelance,
                             @RequestParam(name = "remoteOnly", required = false) String remoteOnly,
                             @RequestParam(name = "officeOnly", required = false) String officeOnly,
                             @RequestParam(name = "partialRemote", required = false) String partialRemote,
                             @RequestParam(name = "today", required = false) boolean today,
                             @RequestParam(name = "days7", required = false) boolean days7,
                             @RequestParam(name = "days30", required = false) boolean days30
    ) {

        model.addAttribute("partTime", Objects.equals(partTime, "Part-Time"));
        model.addAttribute("fullTime", Objects.equals(fullTime, "Full-Time"));
        model.addAttribute("freelance", Objects.equals(freelance, "Freelance"));

        model.addAttribute("remoteOnly", Objects.equals(remoteOnly, "Remote-Only"));
        model.addAttribute("officeOnly", Objects.equals(officeOnly, "Office-Only"));
        model.addAttribute("partialRemote", Objects.equals(partialRemote, "Partial-Remote"));

        model.addAttribute("today", today);
        model.addAttribute("days7", days7);
        model.addAttribute("days30", days30);

        model.addAttribute("job", job);
        model.addAttribute("location", location);

        LocalDate searchDate = null;
        List<JobMessageBoard> jobPost = null;
        boolean dateSearchFlag = true;
        boolean remote = true;
        boolean type = true;

        if(days30) {
            searchDate = LocalDate.now().minusDays(30);
        } else if(days7) {
            searchDate = LocalDate.now().minusDays(7);
        } else if(today) {
            searchDate = LocalDate.now();
        } else {
            dateSearchFlag = false;
        }

        if(partTime == null && fullTime == null && freelance == null) {
            partTime = "Part-Time";
            fullTime = "Full-Time";
            freelance = "Freelance";
            remote = false;
        }

        if(officeOnly == null && remoteOnly == null && partialRemote == null) {
                officeOnly = "Office-Only";
                remoteOnly = "Remote-Only";
                partialRemote = "partialRemote";
                type = false;
        }

        if(!dateSearchFlag && !remote && !type && !StringUtils.hasText(job) && !StringUtils.hasText(location)) {
            jobPost = jobMessageBoardService.getAll();
        } else {
            jobPost = jobMessageBoardService.search(job, location, Arrays.asList(partTime, fullTime, freelance),
                    Arrays.asList(remoteOnly, officeOnly, partialRemote), searchDate);
        }


        Object currentUserProfile = usersService.getCurrentUserProfile();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUsername = authentication.getName();
            model.addAttribute("username", currentUsername);
            if(authentication.getAuthorities().contains(new SimpleGrantedAuthority("Recruiter"))) {
                List<JobProviderJobsDto> providerJobs = jobMessageBoardService.getProviderJobs(((JobProviderDetails)currentUserProfile)
                        .getUserAccountId());
                model.addAttribute("jobPost", providerJobs);
            } else {
                List<CandidateApply> candidateApplyList = candidateApplyService.getCandidateJobs((CandidateDetails)
                        currentUserProfile);
                List<CandidateSave> candidateSaveList = candidateSaveService.getCandidateJob((CandidateDetails)
                        currentUserProfile);

                boolean exist;
                boolean saved;

                for(JobMessageBoard jobMessageBoard : jobPost) {
                    exist = false;
                    saved = false;
                    for(CandidateApply candidateApply : candidateApplyList) {
                        if(Objects.equals(jobMessageBoard.getJobPostId(), candidateApply.getJob().getJobPostId())) {
                            jobMessageBoard.setIsActive(true);
                            exist = true;
                            break;
                        }
                    }

                    for(CandidateSave candidateSave : candidateSaveList) {
                        if(Objects.equals(jobMessageBoard.getJobPostId(), candidateSave.getJob().getJobPostId())) {
                            jobMessageBoard.setIsSaved(true);
                            saved = true;
                            break;
                        }
                    }

                    if(!exist) {
                        jobMessageBoard.setIsActive(false);
                    }
                    if(!saved) {
                        jobMessageBoard.setIsSaved(false);
                    }
                    model.addAttribute("jobPost", jobPost);
                }
            }
        }

        model.addAttribute("user", currentUserProfile);
        return "console";
    }

    @GetMapping("/console/add")
    public String addJobs(Model model) {
        model.addAttribute("jobMessageBoard", new JobMessageBoard());
        model.addAttribute("user", usersService.getCurrentUserProfile());
        return "add-jobs";
    }

    @PostMapping("/console/addNew")
    private String savePost(JobMessageBoard jobMessageBoard, Model model) {

        Users user = usersService.getCurrentUser();
        if(user != null) {
            jobMessageBoard.setPostedById(user);
        }
        jobMessageBoard.setPostedDate(new Date());
        model.addAttribute("jobMessageBoard", jobMessageBoard);
        JobMessageBoard saved = jobMessageBoardService.addNew(jobMessageBoard);
        return "redirect:/console/";
    }

    @PostMapping("/dashboard/edit/{id}")
    public String editJob(@PathVariable("id") int id, Model model) {
        JobMessageBoard jobMessageBoard = jobMessageBoardService.getOne(id);
        model.addAttribute("jobMessageBoard", jobMessageBoard);
        model.addAttribute("user", usersService.getCurrentUserProfile());

        return "add-jobs";
    }
}
