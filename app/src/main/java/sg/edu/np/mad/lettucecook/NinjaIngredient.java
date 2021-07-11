package sg.edu.np.mad.lettucecook;

public class NinjaIngredient {
    private float sugar_g;
    private float fiber_g;
    private float serving_size_g;
    private float sodium_mg;
    private String name;
    private float potassium_mg;
    private float fat_saturated_g;
    private float fat_total_g;
    private float calories;
    private float cholesterol_mg;
    private float protein_g;
    private float carbohydrates_total_g;

    public NinjaIngredient(float sugar_g, float fiber_g, float serving_size_g, float sodium_mg, String name, float potassium_mg, float fat_saturated_g, float fat_total_g, float calories, float cholesterol_mg, float protein_g, float carbohydrates_total_g) {
        this.sugar_g = sugar_g;
        this.fiber_g = fiber_g;
        this.serving_size_g = serving_size_g;
        this.sodium_mg = sodium_mg;
        this.name = name;
        this.potassium_mg = potassium_mg;
        this.fat_saturated_g = fat_saturated_g;
        this.fat_total_g = fat_total_g;
        this.calories = calories;
        this.cholesterol_mg = cholesterol_mg;
        this.protein_g = protein_g;
        this.carbohydrates_total_g = carbohydrates_total_g;
    }

    public float getSugar_g() {
        return sugar_g;
    }

    public float getFiber_g() {
        return fiber_g;
    }

    public float getServing_size_g() {
        return serving_size_g;
    }

    public float getSodium_mg() {
        return sodium_mg;
    }

    public String getName() {
        return name;
    }

    public float getPotassium_mg() {
        return potassium_mg;
    }

    public float getFat_saturated_g() {
        return fat_saturated_g;
    }

    public float getFat_total_g() {
        return fat_total_g;
    }

    public float getCalories() {
        return calories;
    }

    public float getCholesterol_mg() {
        return cholesterol_mg;
    }

    public float getProtein_g() {
        return protein_g;
    }

    public float getCarbohydrates_total_g() {
        return carbohydrates_total_g;
    }
}