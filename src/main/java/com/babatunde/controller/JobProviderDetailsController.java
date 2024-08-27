package com.babatunde.controller;

import com.babatunde.entity.JobProviderDetails;
import com.babatunde.entity.Users;
import com.babatunde.repo.UsersRepo;
import com.babatunde.service.JobProviderDetailsService;
import com.babatunde.utils.FileUploadUtil;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/job-provider-details")
public class JobProviderDetailsController {

    private UsersRepo userRepo;
    private JobProviderDetailsService jobProviderDetailsService;

    public JobProviderDetailsController(UsersRepo userRepo,
                                        JobProviderDetailsService jobProviderDetailsService) {
        this.userRepo = userRepo;
        this.jobProviderDetailsService = jobProviderDetailsService;
    }

    @GetMapping("/")
    public String jobProviderDetails(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUsername = authentication.getName();
            Users users = userRepo.findUserByEmail(currentUsername).orElseThrow(()
                    -> new UsernameNotFoundException("User not in our database"));
            Optional<JobProviderDetails> jobProviderDetails = jobProviderDetailsService.findOne(users.getUserId());

            if(!jobProviderDetails.isEmpty())
                model.addAttribute("profile", jobProviderDetails.get());
        }
            return "job_provider_details";
    }

    @PostMapping("/saveProvider")
    public String saveProvider(JobProviderDetails jobProviderDetails, @RequestParam("image")MultipartFile multipartFile,
                               Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            Users users = userRepo.findUserByEmail(currentUserName).orElseThrow(()
                    -> new UsernameNotFoundException("User not in our database"));
            jobProviderDetails.setUserId(users);
            jobProviderDetails.setUserAccountId(users.getUserId());
        }
            model.addAttribute("profile", jobProviderDetails);
        String fileName = "";
        if(!multipartFile.getOriginalFilename().equals("")) {
            fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            jobProviderDetails.setProfilePhoto(fileName);
        }
        JobProviderDetails savedUser = jobProviderDetailsService.saveProvider(jobProviderDetails);

        String dirForUpload = "photos/provider/" + savedUser.getUserAccountId();
        try{
            FileUploadUtil.saveFile(dirForUpload,fileName, multipartFile);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return "redirect:/console/";
    }
}
