package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.javawebinar.topjava.model.Meal;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Controller
@RequestMapping("/meals")
public class JspMealController extends AbstractMealController {

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("meals", super.getAll());
        return "meals";
    }

    @GetMapping("/filter")
    public String filter(Model model, HttpServletRequest request) {
        log.info("Get parameters for filter from request");
        LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
        LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
        LocalTime endTime = parseLocalTime(request.getParameter("endTime"));
        if (startDate != null || endDate != null || startTime != null || endTime != null) {
            model.addAttribute("meals", super.getBetween(startDate, startTime, endDate, endTime));
        } else {
            return "redirect:/meals";
        }
        return "meals";
    }

    @PostMapping
    public String save(HttpServletRequest request) throws UnsupportedEncodingException {
        log.info("Made meal from request ");
        Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
        if (StringUtils.hasLength(request.getParameter("id"))) {
            super.update(meal, getId(request));
        } else {
            super.create(meal);
        }
        return "redirect:/meals";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable String id) {
        super.delete(Integer.parseInt(id));
        return "redirect:/meals";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable String id, Model model) {
        model.addAttribute("meal", super.get(Integer.parseInt(id)));
        return "mealForm";
    }

    @GetMapping("/add")
    public String add(Model model) {
        Meal meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
        model.addAttribute("meal", meal);
        return "mealForm";
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}
