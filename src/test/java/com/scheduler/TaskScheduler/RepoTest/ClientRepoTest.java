package com.scheduler.TaskScheduler.RepoTest;

import com.scheduler.TaskScheduler.Model.Client;
import com.scheduler.TaskScheduler.Repository.ClientRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@PropertySource(value = "classpath:application.properties")
@Sql(value = "classpath:db/H2/client-test.sql",
     executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class ClientRepoTest {
    @Autowired
    private ClientRepo clientRepo;

    @Test
    public void shouldFindClientByLogin() {
        Client client1 = clientRepo.findByLogin("simpleUser").orElse(null);
        Client client2 = clientRepo.findByLogin("secondUser").orElse(null);
        Client client3 = clientRepo.findByLogin("anotherUser").orElse(null);

        assertThat(client1).isNotNull();
        assertThat(client1.getId()).isEqualTo(100L);
        assertThat(client1.getPassword()).isEqualTo("12345");
        assertThat(client1.getLogin()).isEqualTo("simpleUser");

        assertThat(client2).isNotNull();
        assertThat(client2.getId()).isEqualTo(101L);
        assertThat(client2.getPassword()).isEqualTo("16284");
        assertThat(client2.getLogin()).isEqualTo("secondUser");

        assertThat(client3).isNotNull();
        assertThat(client3.getId()).isEqualTo(102L);
        assertThat(client3.getPassword()).isEqualTo("59134");
        assertThat(client3.getLogin()).isEqualTo("anotherUser");
    }
}
