package com.scheduler.TaskScheduler.Controller;

import com.scheduler.TaskScheduler.Model.Client;
import com.scheduler.TaskScheduler.Model.PeriodMode;
import com.scheduler.TaskScheduler.Model.Priority;
import com.scheduler.TaskScheduler.Model.RepeatableTask;
import com.scheduler.TaskScheduler.Service.RepeatTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/repeatTask")
@PreAuthorize("hasRole('USER')")
public class RepeatTaskController {
    private final static Logger logger = LoggerFactory.getLogger(RepeatTaskController.class);
    private final RepeatTaskService repeatTaskService;

    @Autowired
    public RepeatTaskController(RepeatTaskService repeatTaskService) {
        this.repeatTaskService = repeatTaskService;
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

    /*TODO: add logic related with creating list of tasks*/
    @PostMapping("/createOrUpdateTask")
    public String createOrUpdateTask(@AuthenticationPrincipal Client client,
                                     @ModelAttribute("repeatTask") RepeatableTask task,
                                     @RequestParam("startDateString") String startDate,
                                     @RequestParam("endDateString") String endDate,
                                     Model model) {
        logger.info("Creating or updating task");
        if (startDate == null || endDate == null || startDate.isEmpty() || endDate.isEmpty()) {
            model.addAttribute("dateError", "");
            model.addAttribute("repeatTask", task);
            model.addAttribute("priorities", Priority.values());
            model.addAttribute("periodModes", PeriodMode.values());

            return "tasks/addOrEditRepeatTaskPage";
        }

        task.setClient(client);
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        task.setStartDate(start);
        task.setEndDate(end);
        repeatTaskService.save(task);

        return "redirect:/repeatTask/list";
    }

    /*TODO: add logic related with deleting list of tasks*/
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
