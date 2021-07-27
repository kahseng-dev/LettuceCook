package sg.edu.np.mad.lettucecook.utils;

import android.app.Activity;

import java.util.ArrayList;

import sg.edu.np.mad.lettucecook.models.ApiMeal;

public class DataSingleton {
    private static DataSingleton instance;
    private String mealQuery;
    private ApiMeal meal;
    private ArrayList<ApiMeal> meals;

    // Which activity to return to
    // 0: Main
    // 1: Browse
    // 2: AccountFavourites
    // 3: ShoppingList
    private int returnTo;

    private DataSingleton() { }

    public static synchronized DataSingleton getInstance() {
        if (instance == null)
            instance = new DataSingleton();
        return instance;
    }

    public String getMealQuery() {
        return mealQuery;
    }

    public void setMealQuery(String mealQuery) {
        this.mealQuery = mealQuery;
    }

    public ApiMeal getMeal() {
        return meal;
    }

    public void setMeal(ApiMeal meal) {
        this.meal = meal;
    }

    public ArrayList<ApiMeal> getMeals() {
        return meals;
    }

    public void setMeals(ArrayList<ApiMeal> meals) {
        this.meals = meals;
    }

    // For adding an individual meal to meals
    public void addMeal(ApiMeal meal) {
        if (meals == null) {
            meals = new ArrayList<>();
        }
        meals.add(meal);
    }

    public int getReturnTo() {
        return returnTo;
    }

    public void setReturnTo(int returnTo) {
        this.returnTo = returnTo;
    }

}
