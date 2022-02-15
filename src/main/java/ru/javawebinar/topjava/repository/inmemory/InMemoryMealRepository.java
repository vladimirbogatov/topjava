package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.ValidationUtil;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class InMemoryMealRepository implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepository.class);
    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> save(meal, meal.getUserId()));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        log.info("save {}", meal);
        if (meal.getUserId() != userId) {
            return null;
        }
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        log.info("delete {}", id);
        return repository.get(id).getUserId() == userId && repository.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        log.info("get {}", id);
        Meal meal = repository.get(id);
        return meal.getUserId() == userId ? meal : null;
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        log.info("getAll");
        List<Meal> resul = new ArrayList<>(repository.values());
        return resul.stream().filter(meal -> meal.getUserId() == userId).sorted(Comparator.comparing(Meal::getDateTime).reversed()).collect(Collectors.toList());
    }

    @Override
    public Collection<Meal> getFilteredByDay(LocalDate dateStart, LocalDate dateEnd, int userId) {
        log.info("get filtered by day Meals");
        return getAll(userId).stream()
                .filter(meal -> (meal.getDate().isEqual(dateStart) || meal.getDate().isAfter(dateStart)) && (meal.getDate().isEqual(dateEnd) || meal.getDate().isBefore(dateEnd)))
                .collect(Collectors.toList());
    }
}

