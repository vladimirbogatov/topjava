package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.inmemory.InMemoryMealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

    //    private MealService mealService;
    private MealRepository repository;

    @Override
    public void init() {
        repository = new InMemoryMealRepository();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");
        if (!id.isEmpty()) {
            if (repository.get(Integer.parseInt(id), SecurityUtil.authUserId()) != null) {
                Meal meal = new Meal(Integer.valueOf(id),
                        LocalDateTime.parse(request.getParameter("dateTime")),
                        request.getParameter("description"),
                        Integer.parseInt(request.getParameter("calories")), SecurityUtil.authUserId());
                log.info(meal.isNew() ? "Create {}" : "Update {}", meal);
                repository.save(meal, SecurityUtil.authUserId());
            }
        } else {
            Meal meal = new Meal(null,
                    LocalDateTime.parse(request.getParameter("dateTime")),
                    request.getParameter("description"),
                    Integer.parseInt(request.getParameter("calories")), SecurityUtil.authUserId());
            log.info(meal.isNew() ? "Create {}" : "Update {}", meal);
            repository.save(meal, SecurityUtil.authUserId());
        }
        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action == null ? "all" : action) {
            case "delete":
                int id = getId(request);
                log.info("Delete {}", id);
                repository.delete(id, SecurityUtil.authUserId());
                response.sendRedirect("meals");
                break;
            case "create":
                request.setAttribute("meal", repository.save(
                        new Meal(null, LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES),
                                request.getParameter("description"), SecurityUtil.authUserCaloriesPerDay(),
                                SecurityUtil.authUserId()), SecurityUtil.authUserId()));
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "update":
                request.setAttribute("meal", repository.get(
                        Integer.parseInt(request.getParameter("id")), SecurityUtil.authUserId()));
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "all":
            default:
                log.info("getAll");
                request.setAttribute("meals",
                        MealsUtil.getTos(repository.getAll(SecurityUtil.authUserId()), SecurityUtil.authUserCaloriesPerDay()));
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}
