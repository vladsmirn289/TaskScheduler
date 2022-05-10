package com.scheduler.TaskScheduler.Config.JWT;

import com.scheduler.TaskScheduler.Service.ClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Service
public class JwtFilter extends GenericFilterBean {
    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    private ClientService clientService;
    private JwtUtils jwtUtils;

    @Autowired
    public JwtFilter(ClientService clientService, JwtUtils jwtUtils) {
        this.clientService = clientService;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        logger.info("DoFilter method called");
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        String auth = request.getHeader("Authorization");
        if (auth != null && auth.split(" ")[0].equals("Bearer")) {
            String token = auth.split(" ")[1];
            if (jwtUtils.validateToken(token)) {
                setAuthentication(request, token);
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private void setAuthentication(HttpServletRequest request, String token) {
        String username = jwtUtils.getUserNameFromJwtToken(token);
        UserDetails userDetails = clientService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails,
                        null,
                        userDetails.getAuthorities());

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
