package com.babatunde.controller;

import com.babatunde.entity.*;
import com.babatunde.service.*;
import jakarta.validation.*;
import org.springframework.beans.factory.annotation.*;
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

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registrationPage(Model model) {

        List<UsersType> usersType = usersServiceType.findAll();
        model.addAttribute("findAllTypes", usersType);
        model.addAttribute("user", new Users());

        return "register";
    }

    @PostMapping("/register/new")
    public String regNewUser(@Valid Users users) {
       // System.out.println("User :: " + users);
        usersService.saveUser(users);
        return "console";
    }

}
