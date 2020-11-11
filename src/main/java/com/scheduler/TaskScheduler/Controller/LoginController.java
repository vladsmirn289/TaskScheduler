package com.scheduler.TaskScheduler.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LoginController {
    private final static Logger logger = LoggerFactory.getLogger(LoginController.class);

    @GetMapping
    public String loginPage() {
        logger.info("Showing login page");

        return "authentication/loginPage";
    }
}
