package com.scheduler.TaskScheduler.Controller;

import com.scheduler.TaskScheduler.Util.CalendarUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Controller
public class MainController {
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("d/yyyy-MM");

    @GetMapping
    public String mainPage(@RequestParam(required = false) String date,
                           Model model) {
        LocalDate localDate;
        if (date != null && !date.isEmpty()) {
            localDate = LocalDate.parse(1 + "/" + date, dateFormatter);
            logger.debug("Date param is not null - " + localDate);
        } else {
            localDate = LocalDate.now().withDayOfMonth(1);
            logger.debug("Date param is null - " + localDate);
        }

        model.addAttribute("calendar", CalendarUtil.calendarByLocalDate(localDate));
        model.addAttribute("now", LocalDate.now());
        model.addAttribute("localDate", localDate);

        return "index";
    }
}
