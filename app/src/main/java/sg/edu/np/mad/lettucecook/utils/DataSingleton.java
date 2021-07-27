package sg.edu.np.mad.lettucecook.utils;

import java.util.ArrayList;

import sg.edu.np.mad.lettucecook.models.ApiMeal;

public class DataSingleton {
    private static DataSingleton instance;
    private String mealQuery;
    private ApiMeal meal;
    private ArrayList<ApiMeal> meals;

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
        this.meals.add(meal);
    }
}
