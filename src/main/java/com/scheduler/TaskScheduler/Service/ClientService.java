package com.scheduler.TaskScheduler.Service;

import com.scheduler.TaskScheduler.Model.Client;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface ClientService extends UserDetailsService {
    Optional<Client> findByLogin(String login);

    void save(Client client);

    void delete(Client client);
    void deleteById(Long id);
}
