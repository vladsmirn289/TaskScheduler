package com.scheduler.TaskScheduler.ServiceTest;

import com.scheduler.TaskScheduler.Model.Client;
import com.scheduler.TaskScheduler.Model.Role;
import com.scheduler.TaskScheduler.Service.ClientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@PropertySource(value = "classpath:application.properties")
@Sql(value = {
        "classpath:db/H2/client-test.sql",
        "classpath:db/H2/task-test.sql"
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "classpath:db/H2/after-test.sql",
executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ClientServiceTest {
    @Autowired
    private ClientService clientService;

    @Test
    public void shouldFindClientByLogin() {
        Client found = clientService.findByLogin("simpleUser").orElse(null);
        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(100L);
        assertThat(found.getPassword()).isEqualTo("12345");
        assertThat(found.getLogin()).isEqualTo("simpleUser");
    }

    @Test
    public void shouldSaveClient() {
        Client client = new Client("NewUser", "newPass");
        clientService.save(client);

        Client found = clientService.findByLogin("NewUser").orElse(null);
        assertThat(found).isNotNull();
        assertThat(found.getRoles().size()).isEqualTo(1L);
        assertThat(found.getRoles().iterator().next()).isEqualTo(Role.USER);
        assertThat(found.getLogin()).isEqualTo("NewUser");
        assertThat(found.getPassword()).isEqualTo("newPass");
    }

    @Test
    public void shouldDeleteClient() {
        Client toDelete = clientService.findByLogin("simpleUser").orElse(null);
        assertThat(toDelete).isNotNull();

        clientService.delete(toDelete);
        Client notFound = clientService.findByLogin("simpleUser").orElse(null);
        assertThat(notFound).isNull();
    }

    @Test
    public void shouldDeleteClientById() {
        clientService.deleteById(100L);
        Client notFound = clientService.findByLogin("simpleUser").orElse(null);
        assertThat(notFound).isNull();
    }

    @Test
    public void shouldLoadUserByUsername() {
        UserDetails userDetails = clientService.loadUserByUsername("secondUser");
        assertThat(userDetails).isNotNull();
    }

    @Test
    public void shouldFallWithExceptionWhenLoadUserByInvalidUsername() {
        assertThrows(UsernameNotFoundException.class, () -> {
            UserDetails userDetails = clientService.loadUserByUsername("123");
        });
    }
}
