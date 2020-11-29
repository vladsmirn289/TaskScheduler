package com.scheduler.TaskScheduler.ControllerTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class LoginControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void showLoginPageTest() throws Exception {
        mockMvc
                .perform(get("/login"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("authentication/loginPage"))
                .andExpect(content().string(not(containsString("Неверный логин или пароль!"))));
    }

    @Test
    public void showLoginPageWithErrorAttributeTest() throws Exception {
        mockMvc
                .perform(get("/login?lang=US").param("error", ""))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("authentication/loginPage"))
                .andExpect(content().string(containsString("Wrong login or password!")));
    }

    @Test
    public void notAuthenticatedTest() throws Exception {
        mockMvc
                .perform(get("/"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    @Sql(value = "classpath:db/H2/client-test.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void shouldSuccessLogin() throws Exception {
        mockMvc
                .perform(post("/login")
                        .with(csrf())
                        .param("username", "secondUser")
                        .param("password", "16284"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        mockMvc
                .perform(get("/")
                        .with(user("secondUser")))
                .andExpect(xpath("/html/body/nav/div[2]").string(containsString("secondUser")));
    }

    @Test
    public void badCredentialsTest() throws Exception {
        mockMvc
                .perform(post("/login")
                        .with(csrf())
                        .param("username", "user"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"));
    }

    @Test
    public void logoutTest() throws Exception {
        mockMvc
                .perform(logout())
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?logout"));
    }
}
