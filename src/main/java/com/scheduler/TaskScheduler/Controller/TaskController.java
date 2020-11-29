package com.scheduler.TaskScheduler.Controller;

import com.scheduler.TaskScheduler.Model.Client;
import com.scheduler.TaskScheduler.Model.Priority;
import com.scheduler.TaskScheduler.Model.Task;
import com.scheduler.TaskScheduler.Service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/task")
@PreAuthorize("hasRole('USER')")
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
                              Model model,
                              @PageableDefault(sort = {"name"}, size = 5) Pageable pageable) {
        logger.info("Showing list of tasks");
        LocalDate date = LocalDate.parse(stringDate);
        Page<Task> tasks = taskService.findByClientAndDate(client, date, pageable);

        model.addAttribute("url", "/task/listOfTasks/" + date + "?");
        model.addAttribute("tasks", tasks);
        model.addAttribute("date", stringDate);

        return "tasks/tasksList";
    }

    @GetMapping("/{date}/addTaskPage")
    public String addTaskPage(@PathVariable("date") String stringDate,
                              Model model) {
        logger.info("Showing the creating task page");
        model.addAttribute("date", LocalDate.parse(stringDate));
        model.addAttribute("priorities", Priority.values());

        return "tasks/addOrEditTaskPage";
    }

    @GetMapping("/editPage")
    public String editPage(@RequestParam("taskId") Long id,
                           Model model) {
        logger.info("Showing the editor of task");
        Task task = taskService.findById(id).orElse(null);
        if (task == null) {
            return "index";
        }

        model.addAttribute("task", task);
        model.addAttribute("priorities", Priority.values());
        return "tasks/addOrEditTaskPage";
    }

    @PostMapping("/createOrUpdateTask")
    public String createOrUpdateTask(@AuthenticationPrincipal Client client,
                                     @ModelAttribute("task") Task task,
                                     @RequestParam("textDate") String stringDate) {
        logger.info("Creating or updating task");
        task.setClient(client);
        LocalDate date = LocalDate.parse(stringDate);
        task.setDate(date);
        taskService.save(task);

        return "redirect:/task/listOfTasks/" + stringDate;
    }

    @PostMapping("/toNextWeek")
    public String transferToNextWeek(@AuthenticationPrincipal Client client,
                                     @RequestParam("taskId") Long id,
                                     @RequestParam("taskCurrentDate") String stringDate) {
        logger.info("Transfer task to next week");
        Task task = taskService.findById(id).orElse(null);
        if (task != null && taskService.clientHasTask(client, task)) {
            LocalDate onNextWeek = task.getDate().plusWeeks(1);
            task.setDate(onNextWeek);
            taskService.save(task);
        }

        return "redirect:/task/listOfTasks/" + stringDate;
    }

    @PostMapping("/delete")
    public String deleteTask(@AuthenticationPrincipal Client client,
                             @RequestParam("taskId") Long id,
                             @RequestParam("taskCurrentDate") String stringDate) {
        logger.info("Deleting task");
        Task task = taskService.findById(id).orElse(null);
        if(task != null && taskService.clientHasTask(client, task)) {
            taskService.deleteById(id);
        }

        return "redirect:/task/listOfTasks/" + stringDate;
    }
}
