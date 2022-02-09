package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealDao {
    void add(Meal meal);

    Meal delete(int mealId);

    Meal update(int mealId, Meal meal);

    Meal getById(int id);

    List<Meal> getAllInList();
}
