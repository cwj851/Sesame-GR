package io.github.lazyimmortal.sesame.entity;

import java.util.Objects;

import io.github.lazyimmortal.sesame.util.StringUtil;
import lombok.Getter;

@Getter
public class DishImageEntity {
    private final String beforeMeals;
    private final String afterMeals;

    public DishImageEntity() {
        beforeMeals = afterMeals = null;
    }

    public DishImageEntity(String beforeMeals, String afterMeals) {
        this.beforeMeals = beforeMeals;
        this.afterMeals = afterMeals;
    }

    public String id() {
        return String.valueOf(Objects.hash(beforeMeals, afterMeals));
    }

    public String name() {
        return "Hash:" + id() + "\nbeforeMeals:" + beforeMeals + "\nafterMeals:" + afterMeals;
    }

    public Boolean checkDishImage() {
        return !Objects.equals(beforeMeals, afterMeals)
                && checkDishImage(beforeMeals)
                && checkDishImage(afterMeals);
    }

    public static Boolean checkDishImage(String dishImage) {
        return !StringUtil.isEmpty(dishImage) && dishImage.contains("A*");
    }
}
