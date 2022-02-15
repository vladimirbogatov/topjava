package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.repository.inmemory.InMemoryMealRepository;
import ru.javawebinar.topjava.repository.inmemory.InMemoryUserRepository;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;


import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFound;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealService {

    private MealRepository mealRepository;
    private UserRepository userRepository;

    @Autowired
    public MealService(MealRepository mealRepository, UserRepository userRepository) {
        this.mealRepository = mealRepository;
        this.userRepository = userRepository;
    }

    public MealTo create(MealTo mealTo, int userId) {
        Meal meal = MealsUtil.convertMealTo2Meal(mealTo);
        checkNotFound(mealRepository.save(meal, userId), meal.toString());
        return get(meal.getId(), userId);
    }

    public void delete(int id, int userId) {
        checkNotFoundWithId(mealRepository.delete(id, userId), userId);
    }

    public MealTo get(int id, int userId) {
        return convertMeal2MealTo(checkNotFound(mealRepository.get(id, userId), msgForUserId(userId)), userId);
    }

    public List<MealTo> getAll(int userId) {
        return checkNotFound(MealsUtil.getFilteredTos(mealRepository.getAll(userId), userRepository.get(userId).getCaloriesPerDay(), LocalTime.MIN, LocalTime.MAX), msgForUserId(userId));
    }

    public void update(MealTo mealTo, int userId) {
        Meal meal = MealsUtil.convertMealTo2Meal(mealTo);
        checkNotFoundWithId(mealRepository.save(meal, userId), meal.getId());
    }

    private MealTo convertMeal2MealTo(Meal meal, int userId) {
        List<MealTo> mealTos = MealsUtil.getFilteredTos(mealRepository.getFilteredByDay(meal.getDate(), meal.getDate(), userId), userRepository.get(userId).getCaloriesPerDay(), LocalTime.MIN, LocalTime.MAX);
        return mealTos.stream().filter(mt -> mt.getId().equals(meal.getId())).findFirst().orElse(null);
    }

    private String msgForUserId(int userId) {
        return String.format("can't get list for userId = %d", userId);
    }
}