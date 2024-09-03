package com.babatunde.controller;

import com.babatunde.entity.JobMessageBoard;
import com.babatunde.entity.JobProviderDetails;
import com.babatunde.entity.JobProviderJobsDto;
import com.babatunde.entity.Users;
import com.babatunde.repo.JobMessageBoardRepo;
import com.babatunde.service.JobMessageBoardService;
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

@Controller
public class JobMessageBoardController {

    private UsersService usersService;
    private JobMessageBoardService jobMessageBoardService;

    public JobMessageBoardController(UsersService usersService, JobMessageBoardService jobMessageBoardService) {
        this.usersService = usersService;
        this.jobMessageBoardService = jobMessageBoardService;
    }

    @GetMapping("/console/")
    public String searchJobs(Model model) {

        Object currentUserProfile = usersService.getCurrentUserProfile();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUsername = authentication.getName();
            model.addAttribute("username", currentUsername);
            if(authentication.getAuthorities().contains(new SimpleGrantedAuthority("Recruiter"))) {
                List<JobProviderJobsDto> providerJobs = jobMessageBoardService.getProviderJobs(((JobProviderDetails)currentUserProfile)
                        .getUserAccountId());
                model.addAttribute("jobPost", providerJobs);
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
