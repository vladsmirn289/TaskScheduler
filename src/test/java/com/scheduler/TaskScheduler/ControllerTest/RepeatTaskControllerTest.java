package com.scheduler.TaskScheduler.ControllerTest;

import com.scheduler.TaskScheduler.Model.Client;
import com.scheduler.TaskScheduler.Model.PeriodMode;
import com.scheduler.TaskScheduler.Model.Priority;
import com.scheduler.TaskScheduler.Model.RepeatableTask;
import com.scheduler.TaskScheduler.Service.ClientService;
import com.scheduler.TaskScheduler.Service.RepeatTaskService;
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
public class RepeatTaskControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClientService clientService;

    @Autowired
    private RepeatTaskService repeatTaskService;

    @Autowired
    private TaskService taskService;

    @Test
    public void shouldShowListOfRepeatableTasksPage() throws Exception {
        mockMvc
                .perform(get("/repeatTask/list"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("tasks/listRepeatTasks"))
                .andExpect(model().attributeExists("repeatTasks"))
                .andExpect(xpath("/html/body/div/div/div[3]/table/tbody/tr[1]/td/div")
                        .string(containsString("RepeatableTask1")));
    }

    @Test
    public void shouldShowAddRepeatableTaskPage() throws Exception {
        mockMvc
                .perform(get("/repeatTask/addTaskPage"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("tasks/addOrEditRepeatTaskPage"))
                .andExpect(model().attribute("periodModes", PeriodMode.values()))
                .andExpect(model().attribute("priorities", Priority.values()));
    }

    @Test
    public void shouldShowEditRepeatableTaskPage() throws Exception {
        RepeatableTask task = repeatTaskService.findById(200L).get();
        assertThat(task).isNotNull();

        mockMvc
                .perform(get("/repeatTask/editPage")
                        .param("taskId", "200"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("tasks/addOrEditRepeatTaskPage"))
                .andExpect(model().attribute("repeatTask", task))
                .andExpect(model().attribute("priorities", Priority.values()))
                .andExpect(model().attribute("periodModes", PeriodMode.values()));
    }

    @Test
    @WithUserDetails("simpleUser")
    public void shouldCreateNewRepeatableTask() throws Exception {
        mockMvc
                .perform(post("/repeatTask/createOrUpdateTask")
                        .with(csrf())
                        .param("startDateString", "2020-11-01")
                        .param("endDateString", "2020-11-20")
                        .param("name", "Repeatable...")
                        .param("description", "RepeatableDescription...")
                        .param("priority", "HIGH")
                        .param("periodMode", "EACH_DAY"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/repeatTask/list"));

        Client client = clientService.findByLogin("simpleUser").get();
        PageRequest page = PageRequest.of(0, 100);
        List<RepeatableTask> tasks = repeatTaskService.findByClient(client, page).getContent();

        assertThat(tasks.size()).isEqualTo(1);
        RepeatableTask task = tasks.get(0);
        assertThat(task.getClient()).isEqualTo(client);
        assertThat(task.getStartDate()).isEqualTo(LocalDate.of(2020, 11, 1));
        assertThat(task.getEndDate()).isEqualTo(LocalDate.of(2020, 11, 20));
        assertThat(task.getDescription()).isEqualTo("RepeatableDescription...");
        assertThat(task.getName()).isEqualTo("Repeatable...");
        assertThat(task.getPriority()).isEqualTo(Priority.HIGH);
        assertThat(task.getPeriodMode()).isEqualTo(PeriodMode.EACH_DAY);

        int size = taskService.findByClient(client).size();
        assertThat(size).isEqualTo(21);
    }

    @Test
    public void shouldNotSaveBecauseDateError() throws Exception {
        mockMvc
                .perform(post("/repeatTask/createOrUpdateTask")
                        .with(csrf())
                        .param("startDateString", "")
                        .param("endDateString", "")
                        .param("name", "Repeatable...")
                        .param("description", "RepeatableDescription...")
                        .param("priority", "HIGH")
                        .param("periodMode", "EACH_DAY"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("tasks/addOrEditRepeatTaskPage"))
                .andExpect(model().attributeExists("dateError", "repeatTask"));
    }

    @Test
    public void shouldNotSaveBecauseDateStartError() throws Exception {
        mockMvc
                .perform(post("/repeatTask/createOrUpdateTask")
                        .with(csrf())
                        .param("startDateString", "2020-11-02")
                        .param("endDateString", "2020-11-01")
                        .param("name", "Repeatable...")
                        .param("description", "RepeatableDescription...")
                        .param("priority", "HIGH")
                        .param("periodMode", "EACH_DAY"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("tasks/addOrEditRepeatTaskPage"))
                .andExpect(model().attributeExists("dateStartError", "repeatTask"))
                .andExpect(model().attributeDoesNotExist("dateError"));
    }

    @Test
    public void shouldUpdateTask() throws Exception {
        mockMvc
                .perform(post("/repeatTask/createOrUpdateTask")
                        .with(csrf())
                        .param("startDateString", "2020-11-01")
                        .param("endDateString", "2020-11-20")
                        .param("name", "Repeatable...")
                        .param("description", "RepeatableDescription...")
                        .param("priority", "HIGH")
                        .param("periodMode", "EACH_DAY")
                        .param("id", "200"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/repeatTask/list"));

        Client client = clientService.findByLogin("anotherUser").get();
        PageRequest page = PageRequest.of(0, 100);
        List<RepeatableTask> tasks = repeatTaskService.findByClient(client, page).getContent();

        assertThat(tasks.size()).isEqualTo(1);
        RepeatableTask task = tasks.get(0);
        assertThat(task.getClient()).isEqualTo(client);
        assertThat(task.getStartDate()).isEqualTo(LocalDate.of(2020, 11, 1));
        assertThat(task.getEndDate()).isEqualTo(LocalDate.of(2020, 11, 20));
        assertThat(task.getDescription()).isEqualTo("RepeatableDescription...");
        assertThat(task.getName()).isEqualTo("Repeatable...");
        assertThat(task.getPriority()).isEqualTo(Priority.HIGH);
        assertThat(task.getPeriodMode()).isEqualTo(PeriodMode.EACH_DAY);
        assertThat(task.getId()).isEqualTo(200L);
    }

    @Test
    @WithUserDetails("simpleUser")
    public void shouldNotDeleteRepeatableTaskBecauseWrongUser() throws Exception {
        mockMvc
                .perform(post("/repeatTask/delete")
                        .with(csrf())
                        .param("taskId", "200"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/repeatTask/list"));

        RepeatableTask task = repeatTaskService.findById(200L).orElse(null);
        assertThat(task).isNotNull();
        assertThat(task.getName()).isEqualTo("RepeatableTask1");
    }

    @Test
    public void shouldDeleteRepeatableTask() throws Exception {
        mockMvc
                .perform(post("/repeatTask/delete")
                        .with(csrf())
                        .param("taskId", "200"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/repeatTask/list"));

        RepeatableTask task = repeatTaskService.findById(200L).orElse(null);
        assertThat(task).isNull();
    }
}
