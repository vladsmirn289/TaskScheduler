package com.scheduler.TaskScheduler.ControllerTest;

import com.scheduler.TaskScheduler.Model.Client;
import com.scheduler.TaskScheduler.Service.ClientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

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
        "classpath:db/H2/client-test.sql"
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {
        "classpath:db/H2/after-test.sql"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ClientControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClientService clientService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @WithUserDetails("simpleUser")
    public void showPersonalRoomPageTest() throws Exception {
        Client client = clientService.findByLogin("simpleUser").get();
        assertThat(client).isNotNull();

        mockMvc
                .perform(get("/client/personalRoom"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("client/personalRoom"))
                .andExpect(model().attribute("client", client));
    }

    @Test
    @WithUserDetails("simpleUser")
    public void shouldDoNotChangeWithDefaultDatum() throws Exception {
        mockMvc
                .perform(post("/client/personalRoom")
                        .with(csrf())
                        .param("id", "100")
                        .param("login", "simpleUser")
                        .param("password", "12345"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("client/personalRoom"))
                .andExpect(model().attributeExists("client"));

        Client client = clientService.findByLogin("simpleUser").get();
        assertThat(client).isNotNull();
        assertThat(client.getId()).isEqualTo(100L);
        assertThat(passwordEncoder.matches(client.getPassword(), "12345"));
    }

    @Test
    @WithUserDetails("simpleUser")
    public void shouldSuccessfulChangeLogin() throws Exception {
        mockMvc
                .perform(post("/client/personalRoom")
                        .with(csrf())
                        .param("id", "100")
                        .param("login", "changedSimpleUser")
                        .param("password", "12345"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/client/personalRoom"));

        Client client = clientService.findByLogin("changedSimpleUser").get();
        assertThat(client).isNotNull();
        assertThat(client.getId()).isEqualTo(100L);
        assertThat(passwordEncoder.matches(client.getPassword(), "12345"));
    }

    @Test
    @WithUserDetails("simpleUser")
    public void shouldShowLoginEmptyErrorWhenTryToChangeLogin() throws Exception {
        mockMvc
                .perform(post("/client/personalRoom")
                        .with(csrf())
                        .param("id", "100")
                        .param("login", "")
                        .param("password", "12345"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("client/personalRoom"))
                .andExpect(model().attributeExists("loginIsEmpty"))
                .andExpect(model().attributeDoesNotExist("userExistsError"));

        Client client = clientService.findByLogin("simpleUser").get();
        assertThat(client).isNotNull();
        assertThat(client.getId()).isEqualTo(100L);
        assertThat(passwordEncoder.matches(client.getPassword(), "12345"));
    }

    @Test
    @WithUserDetails("simpleUser")
    public void shouldShowUserExistsErrorWhenTryToChangeLogin() throws Exception {
        mockMvc
                .perform(post("/client/personalRoom")
                        .with(csrf())
                        .param("id", "100")
                        .param("login", "anotherUser")
                        .param("password", "12345"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("client/personalRoom"))
                .andExpect(model().attributeExists("userExistsError"))
                .andExpect(model().attributeDoesNotExist("loginIsEmpty"));

        Client client = clientService.findByLogin("simpleUser").get();
        assertThat(client).isNotNull();
        assertThat(client.getId()).isEqualTo(100L);
        assertThat(passwordEncoder.matches(client.getPassword(), "12345"));
    }

    @Test
    @WithUserDetails("simpleUser")
    public void showChangePasswordPage() throws Exception {
        mockMvc
                .perform(get("/client/resetPasswordPage"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("client/resetPasswordPage"));
    }

    @Test
    @WithUserDetails("simpleUser")
    public void shouldSuccessfulChangePassword() throws Exception {
        mockMvc
                .perform(post("/client/changePassword")
                        .with(csrf())
                        .param("newPassword", "1")
                        .param("retypePassword", "1"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/client/personalRoom"));

        Client client = clientService.findByLogin("simpleUser").get();
        assertThat(client).isNotNull();
        assertThat(client.getId()).isEqualTo(100L);
        assertThat(passwordEncoder.matches(client.getPassword(), "1"));
    }

    @Test
    @WithUserDetails("simpleUser")
    public void shouldRetypePasswordErrorShowWhenTryingToChangePassword() throws Exception {
        mockMvc
                .perform(post("/client/changePassword")
                        .with(csrf())
                        .param("newPassword", "1")
                        .param("retypePassword", "12"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("client/resetPasswordPage"))
                .andExpect(model().attributeExists("retypePasswordError"));

        Client client = clientService.findByLogin("simpleUser").get();
        assertThat(client).isNotNull();
        assertThat(client.getId()).isEqualTo(100L);
        assertThat(passwordEncoder.matches(client.getPassword(), "12345"));
    }

    @Test
    @WithUserDetails("simpleUser")
    public void shouldDeleteAccount() throws Exception {
        mockMvc
                .perform(get("/client/deleteAccount"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        Optional<Client> client = clientService.findByLogin("simpleUser");
        assertThat(client.isPresent()).isFalse();
    }
}
