package com.scheduler.TaskScheduler.ControllerTest;

import com.scheduler.TaskScheduler.Model.Client;
import com.scheduler.TaskScheduler.Model.Role;
import com.scheduler.TaskScheduler.Service.ClientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@PropertySource(value = "classpath:application.properties")
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@Sql(value = {
        "classpath:db/H2/after-test.sql"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class RegistrationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClientService clientService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void showRegistrationPageTest() throws Exception {
        mockMvc
                .perform(get("/registration"))
                .andDo(print())
                .andExpect(view().name("authentication/registrationPage"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldSuccessRegisterNewClient() throws Exception {
        mockMvc
                .perform(post("/registration")
                        .with(csrf())
                        .param("login", "justLogin")
                        .param("password", "12345")
                        .param("passwordRepeat", "12345"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        Client newClient = clientService.findByLogin("justLogin").orElse(null);
        assertThat(newClient).isNotNull();
        assertThat(newClient.getRoles().iterator().next()).isEqualTo(Role.USER);
        assertThat(passwordEncoder.matches("12345", newClient.getPassword()));
    }

    @Test
    public void shouldShowErrorPasswordDoNotMatch() throws Exception {
        mockMvc
                .perform(post("/registration")
                        .with(csrf())
                        .param("login", "")
                        .param("password", "1234")
                        .param("passwordRepeat", "123"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("authentication/registrationPage"))
                .andExpect(model().attributeExists("passwordRepeatError"))
                .andExpect(model().attributeDoesNotExist("userExistsError"));
    }

    @Test
    @Sql(value = {
            "classpath:db/H2/client-test.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void shouldErrorUserExists() throws Exception {
        mockMvc
                .perform(post("/registration")
                        .with(csrf())
                        .param("login", "simpleUser")
                        .param("password", "12345")
                        .param("passwordRepeat", "12345"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("authentication/registrationPage"))
                .andExpect(model().attributeExists("userExistsError"))
                .andExpect(model().attributeDoesNotExist("passwordRepeatError"));
    }
}
