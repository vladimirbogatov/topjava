package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;

import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

//        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO return filtered list with excess. Implement by cycles
        List<UserMeal> userMealBetweenTime = new ArrayList<>();
        //Карта, в которую собираем количество калорий за день
        Map<LocalDate, Integer> caloriesPerDayMap = new HashMap<>();
        //Пробегаем по переданному листу
        //одновременно заполняем карту с калориями за день
        // добавляем в лист только те meals, которые попадают в заданный интервал
        for (UserMeal userMeal : meals) {
            fillOutMapWithCal(caloriesPerDayMap, userMeal);
            if (TimeUtil.isBetweenHalfOpen(userMeal.getDateTime().toLocalTime(), startTime, endTime)) {
                userMealBetweenTime.add(userMeal);
            }
            }
        // создаём результирующий лист, в который мы будем добавлять новые объекты и проверять,
        // есть ли превышения по калориям за день
        List<UserMealWithExcess> userMealWithExcessList = new ArrayList<>();
        for (UserMeal userMeal: userMealBetweenTime
             ) {
            userMealWithExcessList.add(new UserMealWithExcess(
                    userMeal.getDateTime(),
                    userMeal.getDescription(),
                    userMeal.getCalories(),
                    isCalPerDayExcessThreshold(caloriesPerDayMap,
                            userMeal.getDateTime().toLocalDate(),
                            caloriesPerDay
                    )
            ));
        }
        return userMealWithExcessList;
    }

    /**
     * Карта с ключом - дата и значение - количество калорий за день
     * @param map - карта в которой хранится суммарное значение калорий за данный день
     * @param userMeal - новые данные по приёму пищи
     * @return туЖе карту, но изменённую
     */
    public static Map<LocalDate, Integer> fillOutMapWithCal(Map<LocalDate, Integer> map, UserMeal userMeal) {
        if (map.containsKey(userMeal.getDateTime().toLocalDate())) {
            map.put(userMeal.getDateTime().toLocalDate(),
                    map.get(userMeal.getDateTime().toLocalDate()) + userMeal.getCalories()
            );
        } else map.put(userMeal.getDateTime().toLocalDate(), userMeal.getCalories());
        return map;
    }

    /**
     * Превышает ли количество калорий за данный день пороговое значение?
     * @param map - карта с данными калориям за день
     * @param ld - дата, которая нас интересует
     * @param threshold - пороговое значение
     * @return если значение превышает - tru
     */
    public static boolean isCalPerDayExcessThreshold(Map<LocalDate,Integer> map, LocalDate ld, Integer threshold) {
        if (Objects.isNull(map.get(ld))) {
            return false;
        }
        return map.get(ld).compareTo(threshold) <= 0;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO Implement by streams
        //Получение карты с датами и количествами калорий в день
        Map<LocalDate, Integer> caloriesPerDayMap = meals.stream().collect(
                Collectors.groupingBy(UserMeal::getDate,
                        Collectors.summingInt(UserMeal::getCalories)));
        return meals.stream().
                    filter(userMeal -> TimeUtil.isBetweenHalfOpen(userMeal.getDateTime().toLocalTime(),startTime,endTime)).
                map(userMeal ->
                        new UserMealWithExcess(userMeal,isCalPerDayExcessThreshold(caloriesPerDayMap,userMeal.getDate(),caloriesPerDay))).
                collect(Collectors.toList());


    }
}
