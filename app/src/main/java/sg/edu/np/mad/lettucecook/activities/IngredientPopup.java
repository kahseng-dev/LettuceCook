package sg.edu.np.mad.lettucecook.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.TextView;

import sg.edu.np.mad.lettucecook.R;
import sg.edu.np.mad.lettucecook.models.NinjaIngredient;

public class IngredientPopup extends Activity {
    static final String GRAMS = " g";
    static final String MILLIGRAMS = " mg";

    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_ingredient);
        overridePendingTransition(R.anim.slide_in_up, 0);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        // Full width, 80% height
        getWindow().setLayout(dm.widthPixels, (int) (dm.heightPixels * 0.8));

        // Transparent background - allows corner radius to show properly
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams params = getWindow().getAttributes();

        // Attach to bottom
        params.gravity = Gravity.BOTTOM;

        // Dim background
        params.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        params.dimAmount = 0.3f;

        getWindow().setAttributes(params);

        // Setting TextView values
        NinjaIngredient ingredient = (NinjaIngredient) getIntent().getSerializableExtra("ingredient");

        TextView name = findViewById(R.id.popup_ingredient_name);
        name.setText(ingredient.getName());

        if (ingredient.getCalories() > 0) {
            TextView calories = findViewById(R.id.popup_ingredient_calories_value);
            TextView sugar = findViewById(R.id.popup_ingredient_sugar_value);
            TextView fiber = findViewById(R.id.popup_ingredient_fiber_value);
            TextView servingSize = findViewById(R.id.popup_ingredient_serving_size_value);
            TextView sodium = findViewById(R.id.popup_ingredient_sodium_value);
            TextView potassium = findViewById(R.id.popup_ingredient_potassium_value);
            TextView saturatedFat = findViewById(R.id.popup_ingredient_saturated_fat_value);
            TextView totalFat = findViewById(R.id.popup_ingredient_total_fat_value);
            TextView cholesterol = findViewById(R.id.popup_ingredient_cholesterol_value);
            TextView protein = findViewById(R.id.popup_ingredient_protein_value);
            TextView carbohydrates = findViewById(R.id.popup_ingredient_carbohydrates_value);

            calories.setText(ingredient.getCalories() + " kcal");
            sugar.setText(ingredient.getSugar_g() + GRAMS);
            fiber.setText(ingredient.getFiber_g() + GRAMS);
            servingSize.setText(ingredient.getServing_size_g() + GRAMS);
            sodium.setText(ingredient.getSodium_mg() + MILLIGRAMS);
            potassium.setText(ingredient.getPotassium_mg() + MILLIGRAMS);
            saturatedFat.setText(ingredient.getFat_saturated_g() + GRAMS);
            totalFat.setText(ingredient.getFat_total_g() + GRAMS);
            cholesterol.setText(ingredient.getCholesterol_mg() + MILLIGRAMS);
            protein.setText(ingredient.getProtein_g() + GRAMS);
            carbohydrates.setText(ingredient.getCarbohydrates_total_g() + GRAMS);
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.slide_out_down);
    }
}