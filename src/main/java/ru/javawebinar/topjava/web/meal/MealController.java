package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
public class MealController extends AbstractMealController {

    private static final Logger log = LoggerFactory.getLogger(MealController.class);

    @RequestMapping(method = RequestMethod.GET)
    public String getMeals(Model model, HttpServletRequest request) {
        log.info("Get all meals method");
        LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
        LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
        LocalTime endTime = parseLocalTime(request.getParameter("endTime"));
        if (Objects.nonNull(startDate) || Objects.nonNull(endDate) || Objects.nonNull(startTime) || Objects.nonNull(endTime)) {
            model.addAttribute("meals", super.getBetween(startDate, startTime, endDate, endTime));
        } else {
            model.addAttribute("meals", super.getAll());
        }
        return "meals";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String saveMeals(HttpServletRequest request) throws UnsupportedEncodingException {
        log.info("Save meal method");
        request.setCharacterEncoding("UTF-8");
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

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public String deleteMeal(@PathVariable(name = "id") String id) {

        log.info("Delete meal method");
        super.delete(Integer.parseInt(id));
        return "redirect:/meals";
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
    public String updateMeal(@PathVariable(name = "id") String id, Model model) {
        log.info("Update meal method");
        model.addAttribute("meal", super.get(Integer.parseInt(id)));
        return "mealForm";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String addMeal(Model model) {
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
