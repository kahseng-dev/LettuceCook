package sg.edu.np.mad.lettucecook.Models;

public class Ingredient {
    public String ingredientName;
    public String measure;

    public Ingredient(String ingredientName, String measure) {
        this.ingredientName = ingredientName;
        this.measure = measure;
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
