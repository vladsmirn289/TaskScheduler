package com.scheduler.TaskScheduler.Service;

import com.scheduler.TaskScheduler.Model.Client;
import com.scheduler.TaskScheduler.Model.Role;
import com.scheduler.TaskScheduler.Repository.ClientRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class ClientServiceImpl implements ClientService {
    private static final Logger logger = LoggerFactory.getLogger(ClientServiceImpl.class);
    private final ClientRepo clientRepo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ClientServiceImpl(ClientRepo clientRepo, PasswordEncoder passwordEncoder) {
        this.clientRepo = clientRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Client> findByLogin(String login) {
        logger.info("Finding client");

        return clientRepo.findByLogin(login);
    }

    @Override
    public void save(Client client) {
        logger.info("Saving a client");

        if (client == null) {
            return;
        }

        Set<Role> roles = client.getRoles();
        if (roles.isEmpty()) {
            roles.add(Role.USER);
        }

        if (client.getId() == null) {
            String plainPassword = client.getPassword();
            client.setPassword(passwordEncoder.encode(plainPassword));
        }
        clientRepo.save(client);
    }

    @Override
    public void delete(Client client) {
        logger.info("Deleting a client");

        clientRepo.delete(client);
    }

    @Override
    public void deleteById(Long id) {
        logger.info("Deleting a client with id - " + id);

        clientRepo.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        logger.info("loadUserByUsername method called with login - " + login);

        return findByLogin(login).orElseThrow(() ->
                new UsernameNotFoundException("Client with login - " + login + " does not exist"));
    }
}
