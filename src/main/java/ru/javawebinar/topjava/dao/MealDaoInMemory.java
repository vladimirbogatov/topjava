package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MealDaoInMemory implements MealDao {

    private static AtomicInteger mealIdCounter = new AtomicInteger(0);
    private static ConcurrentHashMap<Integer, Meal> mealMap = new ConcurrentHashMap<>();
    static {
        Meal meal =  new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500);
        meal.setId(mealIdCounter.incrementAndGet());
        mealMap.put(mealIdCounter.get(),meal);


        meal = new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000);
        meal.setId(mealIdCounter.incrementAndGet());
        mealMap.put(mealIdCounter.get(),meal);

        meal = new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500);
        meal.setId(mealIdCounter.incrementAndGet());
        mealMap.put(mealIdCounter.get(),meal);

        meal = new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100);
        meal.setId(mealIdCounter.incrementAndGet());
        mealMap.put(mealIdCounter.get(),meal);

        meal = new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000);
        meal.setId(mealIdCounter.incrementAndGet());
        mealMap.put(mealIdCounter.get(),meal);

        meal = new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500);
        meal.setId(mealIdCounter.incrementAndGet());
        mealMap.put(mealIdCounter.get(),meal);

        meal = new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410);
        meal.setId(mealIdCounter.incrementAndGet());
        mealMap.put(mealIdCounter.get(),meal);
    }

    @Override
    public void add(Meal meal) {
        meal.setId(mealIdCounter.incrementAndGet());
        mealMap.put(mealIdCounter.get(),meal);
    }

    @Override
    public Meal delete(int mealId) {
        return mealMap.remove(mealId);
    }

    @Override
    public Meal update(int mealId, Meal meal) {
        mealMap.remove(mealId);
        meal.setId(mealId);
        mealMap.put(mealId, meal);
        return meal;
    }

    @Override
    public Meal getById(int mealId) {
        return mealMap.get(mealId);
    }

    @Override
    public List<Meal> getAllInList() {
        return new ArrayList<>(mealMap.values());
    }
}
