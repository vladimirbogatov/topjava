package ru.javawebinar.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDateTime;

import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
public class MealRestController {
    private MealService service;

    @Autowired
    public MealRestController(MealService service) {
        this.service = service;
    }

    public MealTo get(int id) {
        return service.get(id, authUserId());
    }

    public void delete(int id) {
        service.delete(id, authUserId());
    }

    public void update(MealTo mealTo) {
        service.update(mealTo, authUserId());
    }

    public MealTo create(MealTo mealTo, int userId) {
        return service.create(mealTo, userId);
    }
}