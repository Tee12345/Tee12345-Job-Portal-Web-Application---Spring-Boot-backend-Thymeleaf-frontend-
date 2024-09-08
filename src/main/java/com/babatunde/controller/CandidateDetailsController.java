package com.babatunde.controller;

import com.babatunde.entity.CandidateDetails;
import com.babatunde.entity.Skills;
import com.babatunde.entity.Users;
import com.babatunde.repo.UsersRepo;
import com.babatunde.service.CandidateDetailsService;
import com.babatunde.utils.FileUploadUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Controller
@RequestMapping("/candidate-details")
@Data
public class CandidateDetailsController {

    private CandidateDetailsService candidateDetailsService;

    private UsersRepo usersRepo;

    @Autowired
    public CandidateDetailsController(CandidateDetailsService candidateDetailsService, UsersRepo usersRepo) {
        this.candidateDetailsService = candidateDetailsService;
        this.usersRepo = usersRepo;
    }

    @GetMapping("/")
    public String candidateDetails(Model model) {
        CandidateDetails candidateDetails = new CandidateDetails();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<Skills> skills = new ArrayList<>();

        if(!(authentication instanceof AnonymousAuthenticationToken)) {
            Users user = usersRepo.findUserByEmail(authentication.getName()).orElseThrow(()
                    -> new UsernameNotFoundException("User not available in our database"));
            Optional<CandidateDetails> candidateProfile = candidateDetailsService.getOne(user.getUserId());
            if(candidateProfile.isPresent()) {
                candidateDetails = candidateProfile.get();
                if(candidateDetails.getSkills().isEmpty()) {
                    skills.add(new Skills());
                    candidateDetails.setSkills(skills);
                }
            }
            model.addAttribute("skills", skills);
            model.addAttribute("profile", candidateDetails);
        }
        return "job-seeker-profile";
    }


    @PostMapping("/addNew")
    public String addNew(CandidateDetails candidateDetails,
                         @RequestParam("image")MultipartFile image,
                         @RequestParam("pdf") MultipartFile pdf,
                         Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)) {
            Users user = usersRepo.findUserByEmail(authentication.getName()).orElseThrow(()
                    -> new UsernameNotFoundException("User not available in our database"));
            candidateDetails.setUserId(user);
            candidateDetails.setUserAccountId(user.getUserId());
        }

        List<Skills> skillsList = new ArrayList();
        model.addAttribute("profile", candidateDetails);
        model.addAttribute("skills", skillsList);

        for(Skills skills : candidateDetails.getSkills()) {
            skills.setCandidateDetails(candidateDetails);

            String imageName = "";
            String resumeName = "";

            if(!Objects.equals(image.getOriginalFilename(), "")) {
                imageName = StringUtils.cleanPath(Objects.requireNonNull(image.getOriginalFilename()));
                candidateDetails.setProfilePhoto(imageName);
            }

            if(!Objects.equals(pdf.getOriginalFilename(), "")) {
                resumeName = StringUtils.cleanPath(Objects.requireNonNull(pdf.getOriginalFilename()));
                candidateDetails.setResume(resumeName);
            }

            CandidateDetails seekerProfile = candidateDetailsService.addNew(candidateDetails);

            try{
                 String uploadDir = "photos/candidate/" + candidateDetails.getUserAccountId();
                 if(!Objects.equals(image.getOriginalFilename(), "")) {
                     FileUploadUtil.saveFile(uploadDir, imageName, image);
                 }

                if(!Objects.equals(pdf.getOriginalFilename(), "")) {
                    FileUploadUtil.saveFile(uploadDir, resumeName, pdf);
                }
            } catch(IOException e) {
                throw new RuntimeException();
            }
        }

        return "redirect:/console/";
    }

}
