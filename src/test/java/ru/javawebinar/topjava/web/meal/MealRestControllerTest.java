package ru.javawebinar.topjava.web.meal;

import org.assertj.core.matcher.AssertionMatcher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.SecurityUtil;
import ru.javawebinar.topjava.web.json.JsonUtil;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.web.meal.MealRestController.REST_URL;

class MealRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL_MEAL_ID1 = REST_URL + "/" + MEAL1_ID;


    @Autowired
    private MealService mealService;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_MEAL_ID1))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEAL_MATCHER.contentJson(meal1));

    }

    @Test
    void getBetween() throws Exception {
        LocalDateTime startDateTime = LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2020, Month.JANUARY, 31, 14, 1);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME;
        perform(MockMvcRequestBuilders.get(REST_URL + "/filter")
                .param("startDateTime", startDateTime.format(dateTimeFormatter))
                .param("endDateTime", endDateTime.format(dateTimeFormatter)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("meals.jsp"))
                .andExpect(model().attribute("meals",
                        new AssertionMatcher<List<Meal>>() {
                            @Override
                            public void assertion(List<Meal> actual) throws AssertionError {
                                MEAL_MATCHER.assertMatch(actual, meal7, meal6, meal5, meal4);
                            }
                        }));
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL_MEAL_ID1))
                .andExpect(status().isNoContent());
        MEAL_MATCHER.assertMatch(mealService.getAll(SecurityUtil.authUserId()), meal7, meal6, meal5, meal4, meal3, meal2);
    }

    @Test
    void create() throws Exception {
        Meal newMeal = getNew();
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newMeal)))
                .andDo(print())
                .andExpect(status().isCreated());
        MEAL_MATCHER.assertMatch(mealService.getAll(SecurityUtil.authUserId()).stream()
                .filter(meal -> meal.getDescription().equals(newMeal.getDescription())).findFirst().orElse(null), newMeal);
    }

    @Test
    void update() throws Exception {
        Meal updated = getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL_MEAL_ID1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isNoContent());

        MEAL_MATCHER.assertMatch(mealService.get(MEAL1_ID, SecurityUtil.authUserId()), updated);
    }
}