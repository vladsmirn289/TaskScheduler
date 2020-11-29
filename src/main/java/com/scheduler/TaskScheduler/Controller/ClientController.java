package com.scheduler.TaskScheduler.Controller;

import com.scheduler.TaskScheduler.Model.Client;
import com.scheduler.TaskScheduler.Service.ClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/client")
@PreAuthorize("hasRole('USER')")
public class ClientController {
    private final static Logger logger = LoggerFactory.getLogger(ClientController.class);
    private final ClientService clientService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ClientController(ClientService clientService, PasswordEncoder passwordEncoder) {
        this.clientService = clientService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/personalRoom")
    public String personalRoomPage(@AuthenticationPrincipal Client client,
                                   Model model) {
        logger.info("Showing personal room");
        model.addAttribute("client", client);

        return "client/personalRoom";
    }

    @PostMapping("/personalRoom")
    public String changeClientDatum(@AuthenticationPrincipal Client client,
                                    @ModelAttribute("changedPerson") Client changedClient,
                                    Model model,
                                    HttpServletRequest request) {
        logger.info("Changing personal info");
        String login = changedClient.getLogin();
        String oldLogin = client.getLogin();
        boolean loginIsEmpty = login.isEmpty();
        boolean clientExists = !login.equals(oldLogin) && clientService.findByLogin(login).isPresent();

        if (loginIsEmpty || clientExists) {
            if (loginIsEmpty) {
                logger.warn("Login is empty!");
                model.addAttribute("loginIsEmpty", "");
            }

            if (clientExists) {
                logger.warn("Client already exists");
                model.addAttribute("userExistsError", "");
            }

            model.addAttribute("client", changedClient);
            return "client/personalRoom";
        }

        client.setLogin(login);
        clientService.save(client);
        if (!oldLogin.equals(login)) {
            SecurityContextHolder.getContext().setAuthentication(null);
            request.getSession().removeAttribute("SPRING_SECURITY_CONTEXT");
            return "redirect:/client/personalRoom";
        }

        logger.info("Datum is changed successfully");

        model.addAttribute("client", client);
        return "client/personalRoom";
    }

    @GetMapping("/resetPasswordPage")
    public String resetPasswordPage() {
        logger.info("Showing reset password page");

        return "client/resetPasswordPage";
    }

    @PostMapping("/changePassword")
    public String changePassword(@AuthenticationPrincipal Client client,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("retypePassword") String retypePassword,
                                 Model model) {
        logger.info("Changing password");

        if (!newPassword.equals(retypePassword)) {
            logger.error("Passwords are not match");
            model.addAttribute("retypePasswordError", "");

            return "client/resetPasswordPage";
        }

        client.setPassword(passwordEncoder.encode(newPassword));
        clientService.save(client);

        return "redirect:/client/personalRoom";
    }

    @GetMapping("/deleteAccount")
    public String deleteAccount(@AuthenticationPrincipal Client client,
                                HttpServletRequest request) {
        logger.info("Deleting account");

        clientService.delete(client);

        SecurityContextHolder.getContext().setAuthentication(null);
        request.getSession().removeAttribute("SPRING_SECURITY_CONTEXT");
        return "redirect:/login";
    }
}
