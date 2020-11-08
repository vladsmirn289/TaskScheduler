package com.scheduler.TaskScheduler.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Controller
public class MainController {
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("d/MM/yyyy");

    @GetMapping
    public String mainPage(@RequestParam(required = false) String date,
                           Model model) {
        LocalDate localDate;
        if (date != null) {
            localDate = LocalDate.parse(1 + "/" + date, dateFormatter);
            logger.debug("Date param is not null - " + localDate);
        } else {
            localDate = LocalDate.now().withDayOfMonth(1);
            logger.debug("Date param is null - " + localDate);
        }

        model.addAttribute("calendar", calendarByLocalDate(localDate));
        model.addAttribute("now", LocalDate.now());
        model.addAttribute("localDate", localDate);

        return "index";
    }

    private LocalDate[][] calendarByLocalDate(LocalDate localDate) {
        int lastDayOfMonth = localDate.plusMonths(1).minusDays(1).getDayOfMonth();
        int daysInFirstWeek = 8-localDate.getDayOfWeek().getValue();
        int rows = 1 + (int)Math.ceil((lastDayOfMonth - daysInFirstWeek)/7.0);

        LocalDate[][] calendar = new LocalDate[rows][7];
        int month = localDate.getMonthValue();

        DayOfWeek dayOfWeek = localDate.getDayOfWeek();
        int weekDayValue = dayOfWeek.getValue();
        int row = 0, column = 0;
        for (int i = 1; i < weekDayValue; ++i) {
            calendar[row][column] = null;
            ++column;
        }

        while (localDate.getMonthValue() == month) {
            calendar[row][column] = localDate;

            localDate = localDate.plusDays(1);
            if (localDate.getDayOfWeek().getValue() == 1) {
                ++row;
                column = 0;
            } else {
                ++column;
            }
        }

        return calendar;
    }
}
