package com.scheduler.TaskScheduler.Controller;

import com.scheduler.TaskScheduler.Model.Client;
import com.scheduler.TaskScheduler.Model.Task;
import com.scheduler.TaskScheduler.Service.TaskService;
import com.scheduler.TaskScheduler.Util.CalendarUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class MainController {
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("d/yyyy-MM");
    private final TaskService taskService;

    @Autowired
    public MainController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public String mainPage(@AuthenticationPrincipal Client client,
                           @RequestParam(required = false) String date,
                           Model model) {
        logger.info("Showing the main page");

        LocalDate localDate;
        if (date != null && !date.isEmpty()) {
            logger.debug("The attribute date is equal - " + date);
            localDate = LocalDate.parse(1 + "/" + date, dateFormatter);
            logger.debug("Date param is not null - " + localDate);
        } else {
            logger.debug("The attribute date is not set");
            localDate = LocalDate.now().withDayOfMonth(1);
            logger.debug("Date param is null - " + localDate);
        }

        model.addAttribute("calendar", CalendarUtil.calendarByLocalDate(localDate));
        model.addAttribute("now", ZonedDateTime.now().toLocalDate());
        model.addAttribute("localDate", localDate);

        List<Task> tasks = taskService.findByClient(client);
        List<Integer> countOfTasks = CalendarUtil.countTasksByEachDay(localDate, localDate.plusMonths(1).minusDays(1), tasks);
        model.addAttribute("countForEachDay", countOfTasks);

        return "index";
    }
}
