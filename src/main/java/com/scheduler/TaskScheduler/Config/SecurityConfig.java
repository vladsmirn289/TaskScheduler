package com.scheduler.TaskScheduler.Config;

import com.scheduler.TaskScheduler.Config.JWT.JwtFilter;
import com.scheduler.TaskScheduler.Config.JWT.JwtUtils;
import com.scheduler.TaskScheduler.Service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final PasswordEncoder passwordEncoder;
    private final ClientService clientService;
    private AuthenticationFailureHandler failureHandler;
    private JwtFilter jwtFilter;
    private JwtUtils jwtUtils;

    @Value("${root.url}")
    private String rootUrl;

    @Autowired
    public SecurityConfig(PasswordEncoder passwordEncoder, ClientService clientService, JwtUtils jwtUtils) {
        this.passwordEncoder = passwordEncoder;
        this.clientService = clientService;
        this.jwtUtils = jwtUtils;
    }

    @Autowired
    public void setFailureHandler(AuthenticationFailureHandler failureHandler) {
        this.failureHandler = failureHandler;
    }

    @Autowired
    public void setJwtFilter(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public CustomAuthenticationFilter customAuthenticationFilter() throws Exception {
        CustomAuthenticationFilter filter = new CustomAuthenticationFilter(authenticationManagerBean(), clientService, jwtUtils);
        filter.setAuthenticationFailureHandler(failureHandler);
        filter.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/login", "POST"));
        filter.setAuthenticationManager(authenticationManager());

        return filter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()

                    .antMatchers(
                            "/webjars/**",
                            "/css/**",
                            "/images/**",
                            "/registration",
                            "/login**",
                            "/api/authentication")
                    .permitAll()

                    .anyRequest()
                    .authenticated()

                .and()

                    .csrf()
                    .ignoringAntMatchers("/api/**")

                .and()

                    .logout()
                    .permitAll();

        http.addFilterBefore(jwtFilter, customAuthenticationFilter().getClass());
        http.addFilterBefore(customAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.exceptionHandling().authenticationEntryPoint((request, response, e) -> {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.sendRedirect(rootUrl + "/login");
        });
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(clientService)
                .passwordEncoder(passwordEncoder);
    }
}
