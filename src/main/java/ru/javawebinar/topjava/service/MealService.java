package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFound;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealService {

    private MealRepository mealRepository;

    @Autowired
    public MealService(MealRepository mealRepository, UserRepository userRepository) {
        this.mealRepository = mealRepository;
    }

    public Meal create(Meal meal, int userId) {
        mealRepository.save(meal, userId);
        return meal;
    }

    public void delete(int id, int userId) {
        checkNotFoundWithId(mealRepository.delete(id, userId), id);
    }

    public Meal get(int id, int userId) {
        return checkNotFound(mealRepository.get(id, userId), msgForUserId(userId));
    }

    public List<MealTo> getAll(int userId, int caloriesPerDay) {
        return checkNotFound(MealsUtil.getTos(mealRepository.getAll(userId), caloriesPerDay), msgForUserId(userId));
    }

    public List<MealTo> gerFilteredByDayTime(int userId, LocalDate startDate, LocalTime timeStart, LocalDate endDate, LocalTime timeEnd, int caloriesPerDay) {
        return checkNotFound(MealsUtil.getTos(mealRepository.getFilteredByDayTime(userId, startDate, timeStart, endDate, timeEnd), caloriesPerDay), msgForUserId(userId));
    }

    public void update(Meal meal, int userId) {
        checkNotFoundWithId(mealRepository.save(meal, userId), meal.getId());
    }

    private String msgForUserId(int userId) {
        return String.format("can't do with meal for userId = %d", userId);
    }
}