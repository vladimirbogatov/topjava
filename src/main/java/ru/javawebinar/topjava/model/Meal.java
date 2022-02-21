package ru.javawebinar.topjava.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Meal extends AbstractBaseEntity {
    private LocalDateTime datetime;

    private String description;

    private int calories;

    public Meal() {
    }

    public Meal(LocalDateTime datetime, String description, int calories) {
        this(null, datetime, description, calories);
    }

    public Meal(Integer id, LocalDateTime datetime, String description, int calories) {
        super(id);
        this.datetime = datetime;
        this.description = description;
        this.calories = calories;
    }

    public LocalDateTime getDateTime() {
        return datetime;
    }

    public String getDescription() {
        return description;
    }

    public int getCalories() {
        return calories;
    }

    public void setDatetime(LocalDateTime datetime) {
        this.datetime = datetime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public LocalDate getDate() {
        return datetime.toLocalDate();
    }

    public LocalTime getTime() {
        return datetime.toLocalTime();
    }

    @Override
    public String toString() {
        return "Meal{" +
                "id=" + id +
                ", dateTime=" + datetime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                '}';
    }
}
