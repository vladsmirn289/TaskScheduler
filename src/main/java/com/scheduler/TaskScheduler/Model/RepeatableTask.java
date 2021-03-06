package com.scheduler.TaskScheduler.Model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class RepeatableTask {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToMany(mappedBy = "repeatableTask", cascade = CascadeType.ALL)
    private List<Task> tasks = new ArrayList<>();

    private String name;
    private String description;
    private Priority priority;

    private LocalDate startDate;
    private LocalDate endDate;
    private PeriodMode periodMode;

    protected RepeatableTask() {

    }

    public RepeatableTask(String name,
                          String description,
                          Priority priority,
                          LocalDate startDate,
                          LocalDate endDate,
                          PeriodMode periodMode) {
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.startDate = startDate;
        this.endDate = endDate;
        this.periodMode = periodMode;
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

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
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

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public PeriodMode getPeriodMode() {
        return periodMode;
    }

    public void setPeriodMode(PeriodMode periodMode) {
        this.periodMode = periodMode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RepeatableTask that = (RepeatableTask) o;
        return client.equals(that.client) &&
                name.equals(that.name) &&
                description.equals(that.description) &&
                priority == that.priority &&
                startDate.equals(that.startDate) &&
                endDate.equals(that.endDate) &&
                periodMode == that.periodMode;
    }

    @Override
    public int hashCode() {
        return Objects.hash(client, name, description, priority, startDate, endDate, periodMode);
    }
}
