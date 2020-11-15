package com.scheduler.TaskScheduler.Controller;

import com.scheduler.TaskScheduler.Model.Client;
import com.scheduler.TaskScheduler.Model.Priority;
import com.scheduler.TaskScheduler.Model.Task;
import com.scheduler.TaskScheduler.Service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/task")
public class TaskController {
    private final static Logger logger = LoggerFactory.getLogger(TaskController.class);
    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/listOfTasks/{date}")
    public String listOfTasks(@AuthenticationPrincipal Client client,
                              @PathVariable("date") String stringDate,
                              Model model) {
        logger.info("Showing list of tasks");
        LocalDate date = LocalDate.parse(stringDate);
        List<Task> tasks = taskService.findByClientAndDate(client, date);

        model.addAttribute("tasks", tasks);
        model.addAttribute("date", stringDate);

        return "tasks/tasksList";
    }

    @GetMapping("/{date}/addTaskPage")
    public String addTaskPage(@PathVariable("date") String stringDate,
                              Model model) {
        model.addAttribute("date", LocalDate.parse(stringDate));
        model.addAttribute("priorities", Priority.values());

        return "tasks/addOrEditTaskPage";
    }

    @PostMapping("/addTask")
    public String createNewTask(@AuthenticationPrincipal Client client,
                                @ModelAttribute("task") Task task,
                                @RequestParam("textDate") String stringDate) {
        task.setClient(client);
        LocalDate date = LocalDate.parse(stringDate);
        task.setDate(date);
        taskService.save(task);

        return "redirect:/task/listOfTasks/" + stringDate;
    }
}
