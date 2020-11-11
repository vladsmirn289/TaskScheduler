package com.scheduler.TaskScheduler.Controller;

import com.scheduler.TaskScheduler.Model.Client;
import com.scheduler.TaskScheduler.Service.ClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/registration")
public class RegistrationController {
    private final static Logger logger = LoggerFactory.getLogger(RegistrationController.class);
    private final ClientService clientService;

    @Autowired
    public RegistrationController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public String registrationPage() {
        logger.info("Showing registration page");

        return "authentication/registrationPage";
    }

    @PostMapping
    public String registerClient(@RequestParam("passwordRepeat") String passwordRepeat,
                                 @ModelAttribute("newClient") Client client,
                                 Model model) {
        logger.info("Registration of a client");
        boolean passwordsIsNotMatch = !passwordRepeat.equals(client.getPassword());
        boolean clientExists = clientService.findByLogin(client.getLogin()).isPresent();

        if (passwordsIsNotMatch || clientExists) {
            logger.warn("Registration fail");

            if (passwordsIsNotMatch) {
                logger.warn("Passwords are not match");
                model.addAttribute("passwordRepeatError", "Пароли не совпадают");
            }

            if (clientExists) {
                logger.warn("User with the same login is already exists");
                model.addAttribute("userExistsError", "Пользователь с таким логином уже существует");
            }

            model.addAttribute("client", client);
            return "authentication/registrationPage";
        } else {
            logger.info("Registration successful");
        }

        clientService.save(client);
        return "redirect:/login";
    }
}
