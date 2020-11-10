package com.scheduler.TaskScheduler.Repository;

import com.scheduler.TaskScheduler.Model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepo extends JpaRepository<Client, Long> {
    Optional<Client> findByLogin(String login);
}
