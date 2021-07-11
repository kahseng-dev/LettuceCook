package sg.edu.np.mad.lettucecook.Models;

import java.io.Serializable;

public class Ingredient {
    public int mealId;
    public String ingredientName;
    public String measure;

    public Ingredient() {

    }

    public Ingredient(int mealId, String ingredientName, String measure) {
        this.mealId = mealId;
        this.ingredientName = ingredientName;
        this.measure = measure;
    }

    public int getMealId() {
        return mealId;
    }

    public void setMealId(int mealId) {
        this.mealId = mealId;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }
}
