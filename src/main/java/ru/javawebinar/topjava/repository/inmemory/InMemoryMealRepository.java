package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
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
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
            return meal;
        } else {
            if (repository.get(meal.getId()).getUserId() != userId) {
                return null;
            }
        }
        // handle case: update, but not present in storage
        return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        log.info("delete {}", id);
        return repository.get(id) != null && repository.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        log.info("get {}", id);
        Meal meal = repository.get(id);
        if (meal == null) {
            return null;
        }
        return meal.getUserId() == userId ? meal : null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        log.info("getAll");
        return filteredByPredicate(userId, meal -> true);
    }

    @Override
    public List<Meal> getFilteredByDayTime(int userId, LocalDate dateStart, LocalTime timeStart, LocalDate dateEnd, LocalTime timeEnd) {
        log.info("get filtered by day Meals");
        Predicate<Meal> mealPredicate = meal -> true;
        if (Objects.nonNull(dateStart)) {
            mealPredicate = mealPredicate.and(meal -> meal.getDate().compareTo(dateStart) >= 0);
        }
        if (Objects.nonNull(dateEnd)) {
            mealPredicate = mealPredicate.and(meal -> meal.getDate().compareTo(dateEnd) <= 0);
        }
        if (Objects.nonNull(timeStart)) {
            mealPredicate = mealPredicate.and(meal -> meal.getTime().compareTo(timeStart) >= 0);
        }
        if (Objects.nonNull(timeEnd)) {
            mealPredicate = mealPredicate.and(meal -> meal.getTime().compareTo(timeEnd) <= 0);
        }
        return filteredByPredicate(userId, mealPredicate);
    }

    private List<Meal> filteredByPredicate(int userId, Predicate<Meal> filter) {

        return repository.values().stream()
                .filter(meal -> meal.getUserId() == userId)
                .filter(filter)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }
}

