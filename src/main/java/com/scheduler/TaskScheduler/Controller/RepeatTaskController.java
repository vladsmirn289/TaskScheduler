package com.scheduler.TaskScheduler.Controller;

import com.scheduler.TaskScheduler.DTO.PeriodParameters;
import com.scheduler.TaskScheduler.Model.*;
import com.scheduler.TaskScheduler.Service.RepeatTaskService;
import com.scheduler.TaskScheduler.Service.TaskService;
import com.scheduler.TaskScheduler.Util.PeriodFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/repeatTask")
@PreAuthorize("hasRole('USER')")
public class RepeatTaskController {
    private final static Logger logger = LoggerFactory.getLogger(RepeatTaskController.class);
    private final RepeatTaskService repeatTaskService;
    private final TaskService taskService;

    @Autowired
    public RepeatTaskController(RepeatTaskService repeatTaskService, TaskService taskService) {
        this.repeatTaskService = repeatTaskService;
        this.taskService = taskService;
    }

    @GetMapping("/list")
    public String listRepeatTasks(@AuthenticationPrincipal Client client,
                                  Model model) {
        logger.info("Showing the list of repeatable tasks");
        List<RepeatableTask> tasks = repeatTaskService.findByClient(client);

        model.addAttribute("repeatTasks", tasks);
        return "tasks/listRepeatTasks";
    }

    @GetMapping("/addTaskPage")
    public String addTaskPage(Model model) {
        logger.info("Showing the page to create new repeatable task");
        model.addAttribute("priorities", Priority.values());
        model.addAttribute("periodModes", PeriodMode.values());

        return "tasks/addOrEditRepeatTaskPage";
    }

    @GetMapping("/editPage")
    public String editTaskPage(@RequestParam("taskId") Long id,
                               Model model) {
        logger.info("Showing the editor of the repeatable task");
        RepeatableTask task = repeatTaskService.findById(id).orElse(null);
        if (task == null) {
            return "index";
        }

        model.addAttribute("repeatTask", task);
        model.addAttribute("priorities", Priority.values());
        model.addAttribute("periodModes", PeriodMode.values());

        return "tasks/addOrEditRepeatTaskPage";
    }

    @PostMapping("/createOrUpdateTask")
    public String createOrUpdateTask(@AuthenticationPrincipal Client client,
                                     @ModelAttribute("repeatTask") RepeatableTask task,
                                     @RequestParam("startDateString") String startDate,
                                     @RequestParam("endDateString") String endDate,
                                     PeriodParameters periodParameters,
                                     Model model) {
        logger.info("Creating or updating task");
        if (startDate == null || endDate == null || startDate.isEmpty() || endDate.isEmpty()) {
            model.addAttribute("dateError", "");
            model.addAttribute("repeatTask", task);

            return "tasks/addOrEditRepeatTaskPage";
        }

        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        if (start.isAfter(end)) {
            model.addAttribute("dateStartError", "");
            model.addAttribute("repeatTask", task);

            return "tasks/addOrEditRepeatTaskPage";
        }

        task.setClient(client);
        task.setStartDate(start);
        task.setEndDate(end);

        PeriodFacade periodFacade = new PeriodFacade(task, periodParameters);
        if (task.getId() == null) {
            task = periodFacade.initTasks();
        } else {
            List<Task> tasksBefore = taskService.findAllByRepeatableTask(task);
            task.setTasks(tasksBefore);
            task = periodFacade.updateTasks();

            List<Task> tasksAfter = task.getTasks();
            tasksBefore.removeAll(tasksAfter);
            tasksBefore.forEach(taskService::delete);

            List<Task> toAdd = new ArrayList<>(tasksAfter);
            toAdd.removeAll(tasksBefore);
            toAdd.forEach(taskService::save);
        }
        repeatTaskService.save(task);

        return "redirect:/repeatTask/list";
    }

    @PostMapping("/delete")
    public String deleteTask(@AuthenticationPrincipal Client client,
                             @RequestParam("taskId") Long id) {
        logger.info("Deleting task");
        RepeatableTask task = repeatTaskService.findById(id).orElse(null);
        if(task != null && repeatTaskService.clientHasRepeatTask(client, task)) {
            repeatTaskService.deleteById(id);
        }

        return "redirect:/repeatTask/list";
    }
}
