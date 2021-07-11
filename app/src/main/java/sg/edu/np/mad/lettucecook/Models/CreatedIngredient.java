package sg.edu.np.mad.lettucecook.Models;

import java.io.Serializable;

import sg.edu.np.mad.lettucecook.CreateRecipe;

public class CreatedIngredient implements Serializable {

    public String ingredientName;
    public String ingredientMeasure;

    public CreatedIngredient() { }

    public CreatedIngredient(String ingredientName, String ingredientMeasure) {
        this.ingredientName = ingredientName;
        this.ingredientMeasure = ingredientMeasure;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public String getIngredientMeasure() {
        return ingredientMeasure;
    }

    public void setIngredientMeasure(String ingredientMeasure) {
        this.ingredientMeasure = ingredientMeasure;
    }
}
