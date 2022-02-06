package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.util.MealsUtil;

public class GetMealDao {
    private static final GetMealDao getMealDao = new GetMealDao();
    private MealDao mealDao;
    private GetMealDao() {
        this.mealDao = new MealDaoInMemory(MealsUtil.getMeals());
    }

    public static GetMealDao getInstance (){
        return getMealDao;
    }

    public MealDao getMealDao(){
        return mealDao;
    }


}
