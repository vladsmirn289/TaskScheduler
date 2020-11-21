package com.scheduler.TaskScheduler.Model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "repeatable_task_id")
    private RepeatableTask repeatableTask;

    private String name;
    private String description;
    private Priority priority;
    private LocalDate date;
    private int progress;

    protected Task() {

    }

    public Task(String name,
                String description,
                Priority priority,
                LocalDate date,
                int progress) {
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.date = date;
        this.progress = progress;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public RepeatableTask getRepeatableTask() {
        return repeatableTask;
    }

    public void setRepeatableTask(RepeatableTask repeatableTask) {
        this.repeatableTask = repeatableTask;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(client, task.client) &&
                Objects.equals(name, task.name) &&
                Objects.equals(description, task.description) &&
                priority == task.priority &&
                Objects.equals(date, task.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(client, name, description, priority, date);
    }
}
