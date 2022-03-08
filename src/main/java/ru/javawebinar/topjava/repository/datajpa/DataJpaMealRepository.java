package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class DataJpaMealRepository implements MealRepository {

    private final CrudMealRepository crudRepository;
    private final EntityManager em;

    public DataJpaMealRepository(CrudMealRepository crudRepository, EntityManager em) {
        this.crudRepository = crudRepository;
        this.em = em;
    }

    @Override
    public Meal save(Meal meal, int userId) {
        meal.setUser(em.getReference(User.class, userId));
        if (meal.isNew()) {
            return crudRepository.save(meal);
        } else if (!isMealUsers(meal.getId(), userId)) {
            return null;
        }
        return crudRepository.save(meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        if (!isMealUsers(id, userId)) {
            return false;
        }
        crudRepository.deleteById(id);
        return true;
    }

    @Override
    public Meal get(int id, int userId) {
        Meal meal = crudRepository.findById(id).orElse(null);
        return meal == null || meal.getUser().getId() == userId ? meal : null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return crudRepository.getAllWithUserId(userId);
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return crudRepository.getBetweenHalfOpen(startDateTime, endDateTime, userId);
    }

    private boolean isMealUsers(Integer id, Integer userId) {
        return get(id, userId) != null;
    }

}
