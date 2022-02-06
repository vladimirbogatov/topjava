package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealDao {
    void addMeal(Meal meal);

    void deleteMeal(int mealID);

    void updateMeal(Meal meal);

    List<Meal> getAllMeal();

    Meal getMealById(int id);
}
