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
import java.time.LocalTime;
import java.util.Locale;
import java.util.Objects;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private static final long serialVersionUID = 1L;
    private static final String INSERT_OR_EDIT_MEALS = "/mealsEdit.jsp";
    private static final String LIST_MEALS = "/meals.jsp";
    private static final int caloriesPerDay = 2000;
    private MealDao mealDao = new MealDaoInMemory();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to meals");
        String forward = "";
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action").toLowerCase();
        int mealId = 0;

        switch (action) {
            case ("delete"):
                mealId = Integer.parseInt(request.getParameter("id"));
                mealDao.delete(mealId);
                forward = LIST_MEALS;
                request.setAttribute("meals", MealsUtil.filteredByStreams(mealDao.getAllInList(), LocalTime.MIN, LocalTime.MAX, caloriesPerDay));
                break;
            case ("edit"):
                forward = INSERT_OR_EDIT_MEALS;
                mealId = Integer.parseInt(request.getParameter("id"));
                Meal meal = mealDao.getById(mealId);
                request.setAttribute("meal", meal);
                break;
            case ("listmeals"):
                forward = LIST_MEALS;
                request.setAttribute("meals", MealsUtil.filteredByStreams(mealDao.getAllInList(), LocalTime.MIN, LocalTime.MAX, caloriesPerDay));
                break;
            default:
                forward = INSERT_OR_EDIT_MEALS;
        }

        RequestDispatcher view = request.getRequestDispatcher(forward);
        request.setAttribute("meals", MealsUtil.filteredByStreams(mealDao.getAllInList(), LocalTime.MIN, LocalTime.MAX, caloriesPerDay));
        view.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("submit");
        if (action.equalsIgnoreCase("submit")) {
            LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("dateTime"));
            String description = request.getParameter("description");
            int calories = Integer.parseInt(request.getParameter("calories"));
            Meal meal = new Meal(dateTime, description, calories);
            String mealId = request.getParameter("id");
            if (Objects.isNull(mealId) || mealId.isEmpty()) {
                mealDao.add(meal);
            } else {
                mealDao.update(Integer.parseInt(mealId), meal);
            }
        }

        RequestDispatcher view = request.getRequestDispatcher(LIST_MEALS);
        request.setAttribute("meals", MealsUtil.filteredByStreams(mealDao.getAllInList(), LocalTime.MIN, LocalTime.MAX, caloriesPerDay));
        view.forward(request, response);
    }
}
