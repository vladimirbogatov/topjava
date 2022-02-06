package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MealDaoInMemory implements MealDao {

    private List<Meal> meals;

    public MealDaoInMemory(List<Meal> meals) {
        this.meals = new CopyOnWriteArrayList<>(meals);
    }

    @Override
    public void addMeal(Meal meal) {
        meals.add(meal);
    }

    @Override
    public void deleteMeal(int mealId) {
        meals.removeIf(meal -> meal.getMealId().intValue() == mealId);
    }

    @Override
    public void updateMeal(Meal meal) {
        meals.removeIf(m -> m.getMealId().intValue() == meal.getMealId().intValue());
        addMeal(meal);
    }

    @Override
    public List<Meal> getAllMeal() {
        return meals;
    }

    @Override
    public Meal getMealById(int id) {
        return meals.parallelStream().filter(meal -> meal.getMealId().intValue() == id).findFirst().orElse(null);
    }
}
