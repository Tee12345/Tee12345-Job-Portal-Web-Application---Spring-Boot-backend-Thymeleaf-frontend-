package com.babatunde.controller;

import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@Controller
public class IndexPageController {

    @GetMapping("/")
    public String indexPage() {
        return "index";
    }


}
