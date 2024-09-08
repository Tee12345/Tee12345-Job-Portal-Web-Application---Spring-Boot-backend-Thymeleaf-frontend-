package com.babatunde.controller;

import com.babatunde.entity.*;
import com.babatunde.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;


import java.util.*;

@Controller
public class UserController {

    private UsersServiceType usersServiceType;
    private UsersService usersService;

    @Autowired
    public UserController(UsersServiceType usersServiceType, UsersService usersService) {
        this.usersServiceType = usersServiceType;
        this.usersService = usersService;
    }

    @GetMapping("/register")
    public String registrationPage(Model model) {

        List<UsersType> usersType = usersServiceType.findAll();
        model.addAttribute("findAllTypes", usersType);
        model.addAttribute("user", new Users());
        return "register";
    }

    @PostMapping("/register/new")
    public String regNewUser(@Valid Users users, Model model) {

        Optional<Users> verifyUser = usersService.getUserByEmail(users.getEmail());

        if(verifyUser.isPresent()) {
            model.addAttribute("badRequest", "This email is in our database, kindly use another email addres");
            List<UsersType> usersType = usersServiceType.findAll();
            model.addAttribute("findAllTypes", usersType);
            model.addAttribute("user", new Users());
            return "register";
        }
        usersService.saveUser(users);
        return "redirect:/console/";
    }
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return "redirect:/";
    }

}
