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
@RequestMapping(value = "/meals")
public class JspMealController extends AbstractMealController {

    @GetMapping
    public String getAll(Model model, HttpServletRequest request) {
        log.info("Get all meals method");
        model.addAttribute("meals", super.getAll());
        return "meals";
    }

    @GetMapping(value = "/filter")
    public String filter(Model model, HttpServletRequest request) {
        LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
        LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
        LocalTime endTime = parseLocalTime(request.getParameter("endTime"));
        if (Objects.nonNull(startDate) || Objects.nonNull(endDate) || Objects.nonNull(startTime) || Objects.nonNull(endTime)) {
            model.addAttribute("meals", super.getBetween(startDate, startTime, endDate, endTime));
        } else {
            return "redirect:/meals";
        }
        return "meals";
    }

    @PostMapping
    public String save(HttpServletRequest request) throws UnsupportedEncodingException {
        log.info("Save meal method");
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

    @GetMapping(value = "/delete/{id}")
    public String delete(@PathVariable String id) {
        log.info("Delete meal method");
        super.delete(Integer.parseInt(id));
        return "redirect:/meals";
    }

    @GetMapping(value = "/update/{id}")
    public String update(@PathVariable(name = "id") String id, Model model) {
        log.info("Update meal method");
        model.addAttribute("meal", super.get(Integer.parseInt(id)));
        return "mealForm";
    }

    @GetMapping(value = "/add")
    public String add(Model model) {
        log.info("Add meal method");
        Meal meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
        model.addAttribute("meal", meal);
        return "mealForm";
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}
