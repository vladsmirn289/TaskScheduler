package com.scheduler.TaskScheduler.Controller;

import com.scheduler.TaskScheduler.Config.JWT.JwtUtils;
import com.scheduler.TaskScheduler.DTO.AuthRequest;
import com.scheduler.TaskScheduler.DTO.AuthResponse;
import com.scheduler.TaskScheduler.Model.Client;
import com.scheduler.TaskScheduler.Model.Priority;
import com.scheduler.TaskScheduler.Model.Task;
import com.scheduler.TaskScheduler.Service.ClientService;
import com.scheduler.TaskScheduler.Service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class MainRestController {
    private static final Logger logger = LoggerFactory.getLogger(MainRestController.class);
    private final TaskService taskService;
    private final Task nullTask;
    private AuthenticationManager authManager;
    private JwtUtils jwtUtils;
    private ClientService clientService;

    @Autowired
    public MainRestController(TaskService taskService, AuthenticationManager authManager,
                              JwtUtils jwtUtils, ClientService clientService) {
        this.taskService = taskService;
        this.nullTask = new Task("", "", Priority.NO, LocalDate.MIN, 0);
        this.authManager = authManager;
        this.jwtUtils = jwtUtils;
        this.clientService = clientService;
    }

    @GetMapping("/tasks/id/{id}")
    public ResponseEntity<Task> showTaskById(@PathVariable("id") String id) {
        logger.info("Called showTaskById method");
        Task task = taskService.findById(Long.valueOf(id)).orElse(null);

        if (task != null) {
            return new ResponseEntity<>(task, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(nullTask, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/tasks/date/{day}-{month}-{year}")
    public ResponseEntity<List<Task>> showTasksByDate(@PathVariable("day") String day,
                                                      @PathVariable("month") String month,
                                                      @PathVariable("year") String year) {
        logger.info("Called showTasksByDate method");
        LocalDate date = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
        List<Task> items = taskService.findAllByDate(date);

        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @PostMapping("/authentication")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        logger.info("Login method called");

        String username = authRequest.getUsername();
        String password = authRequest.getPassword();

        try {
            authManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (BadCredentialsException ex) {
            logger.warn("Bad credentials");
            logger.error(ex.toString());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Bad credentials", ex);
        }

        Client client = clientService.findByLogin(username).orElse(null);

        if (client == null) {
            return new ResponseEntity<>(new AuthResponse(), HttpStatus.NOT_FOUND);
        }

        String token = client.getJwtToken();
        if (token == null) {
            token = jwtUtils.createToken(username, client.getRoles());
            client.setJwtToken(token);
            clientService.save(client);
        }

        AuthResponse authResponse = new AuthResponse(token);

        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }
}
