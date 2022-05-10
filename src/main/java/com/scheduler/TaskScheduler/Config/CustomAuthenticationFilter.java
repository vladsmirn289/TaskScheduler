package com.scheduler.TaskScheduler.Config;

import com.scheduler.TaskScheduler.Config.JWT.JwtUtils;
import com.scheduler.TaskScheduler.Model.Client;
import com.scheduler.TaskScheduler.Service.ClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final static Logger logger = LoggerFactory.getLogger(CustomAuthenticationFilter.class);

    private AuthenticationManager authManager;
    private ClientService clientService;
    private JwtUtils jwtUtils;

    public CustomAuthenticationFilter(AuthenticationManager authManager, ClientService clientService, JwtUtils jwtUtils) {
        this.authManager = authManager;
        this.clientService = clientService;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        logger.info("Attempt to authenticate");

        String username = super.obtainUsername(request);
        String password = super.obtainPassword(request);
        Authentication auth = super.attemptAuthentication(request, response);

        logger.info("Getting jwt token");
        String token = getToken(username, password);

        if (token != null) {
            logger.info("Successful");
        }

        Cookie jwtCookie = new Cookie("jwtToken", token);
        response.addCookie(jwtCookie);

        return auth;
    }

    private String getToken(String username, String password) {
        logger.info("Attempt to login");

        try {
            authManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (BadCredentialsException ex) {
            logger.warn("Bad credentials");
            logger.error(ex.toString());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Bad credentials", ex);
        }

        Client client = clientService.findByLogin(username).orElse(null);

        if (client == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found");
        }

        String token = client.getJwtToken();
        if (token == null) {
            token = jwtUtils.createToken(username, client.getRoles());
            client.setJwtToken(token);
            clientService.save(client);
        }

        return token;
    }
}
