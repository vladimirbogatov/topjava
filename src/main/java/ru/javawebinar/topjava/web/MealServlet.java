package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.dao.MealDaoInMemory;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private static final long serialVersionUID = 1L;
    private static final String INSERT_OR_EDIT_MEALS = "/mealsEdit.jsp";
    private static final String LIST_MEALS = "/meals.jsp";
    private static final int caloriesPerDay = 2000;
    private static final MealDao mealDao = new MealDaoInMemory(MealsUtil.getMeals());

    public MealServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to meals");
        String forward = "";
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        if (action.equalsIgnoreCase("delete")) {
            int mealId = Integer.parseInt(request.getParameter("mealToId"));
            mealDao.deleteMeal(mealId);
            forward = LIST_MEALS;
            request.setAttribute("meals", MealsUtil.getMealToWithoutFiltration(mealDao.getAllMeal(), caloriesPerDay));
        } else if (action.equalsIgnoreCase("edit")) {
            forward = INSERT_OR_EDIT_MEALS;
            int mealId = Integer.parseInt(request.getParameter("mealToId"));
            Meal meal = mealDao.getMealById(mealId);
            request.setAttribute("meal", meal);
        } else if (action.equalsIgnoreCase("listMeals")) {
            forward = LIST_MEALS;
            request.setAttribute("meals", MealsUtil.getMealToWithoutFiltration(mealDao.getAllMeal(), caloriesPerDay));
        } else {
            forward = INSERT_OR_EDIT_MEALS;
        }

        RequestDispatcher view = request.getRequestDispatcher(forward);
        request.setAttribute("meals", MealsUtil.getMealToWithoutFiltration(mealDao.getAllMeal(), caloriesPerDay));
        view.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("submit");
        if (action.equalsIgnoreCase("submit")) {
            LocalDateTime dob = LocalDateTime.parse(request.getParameter("dob"));
            String description = request.getParameter("description");
            int calories = Integer.parseInt(request.getParameter("calories"));
            Meal meal = new Meal(dob, description, calories);
            String mealId = request.getParameter("mealToId");
            if (Objects.isNull(mealId) || mealId.isEmpty()) {
                mealDao.addMeal(meal);
            } else {
                meal.setMealId(new AtomicInteger(Integer.parseInt(mealId)));
                mealDao.updateMeal(meal);
            }
        }

        RequestDispatcher view = request.getRequestDispatcher(LIST_MEALS);
        request.setAttribute("meals", MealsUtil.getMealToWithoutFiltration(mealDao.getAllMeal(), caloriesPerDay));
        view.forward(request, response);
    }
}
