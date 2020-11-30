package com.scheduler.TaskScheduler.ControllerTest;

import com.scheduler.TaskScheduler.Model.Client;
import com.scheduler.TaskScheduler.Model.Priority;
import com.scheduler.TaskScheduler.Model.Task;
import com.scheduler.TaskScheduler.Service.ClientService;
import com.scheduler.TaskScheduler.Service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
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
        "classpath:db/H2/client-test.sql",
        "classpath:db/H2/repeatTask-test.sql",
        "classpath:db/H2/task-test.sql"
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {
        "classpath:db/H2/after-test.sql"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@WithUserDetails("anotherUser")
public class TaskControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClientService clientService;

    @Autowired
    private TaskService taskService;

    @Test
    public void shouldShowListOfTasksPage() throws Exception {
        mockMvc
                .perform(get("/task/listOfTasks/2020-11-23"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("tasks/tasksList"))
                .andExpect(model().attributeExists("tasks", "date"))
                .andExpect(xpath("/html/body/div/div/div[3]/table/tbody/tr[1]/td/div")
                        .string(containsString("Task3")));
    }

    @Test
    public void shouldShowAddTaskPage() throws Exception {
        mockMvc
                .perform(get("/task/2020-11-01/addTaskPage"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("tasks/addOrEditTaskPage"))
                .andExpect(model().attribute("date", LocalDate.parse("2020-11-01")))
                .andExpect(model().attribute("priorities", Priority.values()));
    }

    @Test
    public void shouldShowEditTaskPage() throws Exception {
        Task task = taskService.findById(106L).get();
        assertThat(task).isNotNull();

        mockMvc
                .perform(get("/task/editPage")
                        .param("taskId", "106"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("tasks/addOrEditTaskPage"))
                .andExpect(model().attribute("task", task))
                .andExpect(model().attribute("priorities", Priority.values()));
    }

    @Test
    public void shouldCreateNewTask() throws Exception {
        mockMvc
                .perform(post("/task/createOrUpdateTask")
                        .with(csrf())
                        .param("textDate", "2020-11-01")
                        .param("name", "TestTask1")
                        .param("description", "TestTaskDescription1")
                        .param("priority", "HIGH")
                        .param("progress", "17"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/task/listOfTasks/2020-11-01"));

        Client client = clientService.findByLogin("anotherUser").get();
        PageRequest page = PageRequest.of(0, 100);
        List<Task> tasks = taskService.findByClientAndDate(client,
                LocalDate.of(2020, 11, 1), page).getContent();

        assertThat(tasks.size()).isEqualTo(1);
        Task task = tasks.get(0);
        assertThat(task.getClient()).isEqualTo(client);
        assertThat(task.getDate()).isEqualTo(LocalDate.of(2020, 11, 1));
        assertThat(task.getDescription()).isEqualTo("TestTaskDescription1");
        assertThat(task.getName()).isEqualTo("TestTask1");
        assertThat(task.getPriority()).isEqualTo(Priority.HIGH);
        assertThat(task.getProgress()).isEqualTo(17);
    }

    @Test
    public void shouldUpdateTask() throws Exception {
        mockMvc
                .perform(post("/task/createOrUpdateTask")
                        .with(csrf())
                        .param("textDate", "2020-11-30")
                        .param("id", "106")
                        .param("name", "TestTask4")
                        .param("description", "TestTaskDescription4")
                        .param("priority", "NO")
                        .param("progress", "57"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/task/listOfTasks/2020-11-30"));

        Client client = clientService.findByLogin("anotherUser").get();
        PageRequest page = PageRequest.of(0, 100);
        List<Task> tasks = taskService.findByClientAndDate(client,
                LocalDate.of(2020, 11, 30), page).getContent();

        assertThat(tasks.size()).isEqualTo(1);
        Task task = tasks.get(0);
        assertThat(task.getId()).isEqualTo(106);
        assertThat(task.getClient()).isEqualTo(client);
        assertThat(task.getDate()).isEqualTo(LocalDate.of(2020, 11, 30));
        assertThat(task.getDescription()).isEqualTo("TestTaskDescription4");
        assertThat(task.getName()).isEqualTo("TestTask4");
        assertThat(task.getPriority()).isEqualTo(Priority.NO);
        assertThat(task.getProgress()).isEqualTo(57);
    }

    @Test
    public void shouldNotTransferTaskToTheNextWeekBecauseWrongUser() throws Exception {
        mockMvc
                .perform(post("/task/toNextWeek")
                        .with(csrf())
                        .param("taskCurrentDate", "2020-11-01")
                        .param("taskId", "103"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/task/listOfTasks/2020-11-01"));

        Task task = taskService.findById(103L).orElse(null);
        assertThat(task).isNotNull();
        assertThat(task.getDate()).isEqualTo(LocalDate.of(2020, 11, 1));
        assertThat(task.getDescription()).isEqualTo("This is a description of task1");
        assertThat(task.getName()).isEqualTo("Task1");
        assertThat(task.getPriority()).isEqualTo(Priority.LOW);
        assertThat(task.getProgress()).isEqualTo(0);
    }

    @Test
    @WithUserDetails("simpleUser")
    public void shouldTransferTaskToTheNextWeek() throws Exception {
        mockMvc
                .perform(post("/task/toNextWeek")
                        .with(csrf())
                        .param("taskCurrentDate", "2020-11-01")
                        .param("taskId", "103"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/task/listOfTasks/2020-11-01"));

        Task task = taskService.findById(103L).orElse(null);
        assertThat(task).isNotNull();
        assertThat(task.getDate()).isEqualTo(LocalDate.of(2020, 11, 8));
        assertThat(task.getDescription()).isEqualTo("This is a description of task1");
        assertThat(task.getName()).isEqualTo("Task1");
        assertThat(task.getPriority()).isEqualTo(Priority.LOW);
        assertThat(task.getProgress()).isEqualTo(0);
    }

    @Test
    public void shouldNotDeleteTaskBecauseWrongUser() throws Exception {
        mockMvc
                .perform(post("/task/delete")
                        .with(csrf())
                        .param("taskCurrentDate", "2020-11-01")
                        .param("taskId", "103"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/task/listOfTasks/2020-11-01"));

        Task task = taskService.findById(103L).orElse(null);
        assertThat(task).isNotNull();
        assertThat(task.getDate()).isEqualTo(LocalDate.of(2020, 11, 1));
        assertThat(task.getDescription()).isEqualTo("This is a description of task1");
        assertThat(task.getName()).isEqualTo("Task1");
        assertThat(task.getPriority()).isEqualTo(Priority.LOW);
        assertThat(task.getProgress()).isEqualTo(0);
    }

    @Test
    @WithUserDetails("simpleUser")
    public void shouldDeleteTask() throws Exception {
        mockMvc
                .perform(post("/task/delete")
                        .with(csrf())
                        .param("taskCurrentDate", "2020-11-01")
                        .param("taskId", "103"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/task/listOfTasks/2020-11-01"));

        Task task = taskService.findById(103L).orElse(null);
        assertThat(task).isNull();
    }
}
