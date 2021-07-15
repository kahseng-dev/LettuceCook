package sg.edu.np.mad.lettucecook.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import sg.edu.np.mad.lettucecook.rv.CreatedIngredientAdapter;
import sg.edu.np.mad.lettucecook.R;
import sg.edu.np.mad.lettucecook.models.CreatedIngredient;

public class IngredientsActivity extends AppCompatActivity {

    RecyclerView recyclerIngredients;
    ArrayList<CreatedIngredient> ingredientList = new ArrayList<>();
    String recipeNameValue, recipeAreaSpinnerValue, recipeCategorySpinnerValue, recipeInstructionsValue;
    TextView createdRecipeName, createdRecipeArea, createdRecipeInstructions, createdRecipeCategory;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_recipes);

        recyclerIngredients = findViewById(R.id.custom_recipe_ingredients_rv);
        createdRecipeName = findViewById(R.id.custome_recipe_name);
        createdRecipeArea = findViewById(R.id.custom_recipe_area);
        createdRecipeInstructions = findViewById(R.id.custom_recipe_instructions);
        createdRecipeCategory = findViewById(R.id.custom_recipe_category);

        ingredientList = (ArrayList<CreatedIngredient>) getIntent().getExtras().getSerializable("ARRAYLIST");

        recipeNameValue = getIntent().getExtras().getString("recipeNameValue");
        recipeInstructionsValue = getIntent().getExtras().getString("recipeInstructionsValue");
        recipeAreaSpinnerValue = getIntent().getExtras().getString("recipeAreaSpinnerValue");
        recipeCategorySpinnerValue = getIntent().getExtras().getString("recipeCategorySpinnerValue");

        createdRecipeName.setText(recipeNameValue);
        createdRecipeArea.setText(recipeAreaSpinnerValue);
        createdRecipeCategory.setText(recipeCategorySpinnerValue);
        createdRecipeInstructions.setText(recipeInstructionsValue);

        CreatedIngredientAdapter mAdapter = new CreatedIngredientAdapter(ingredientList, this);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);

        recyclerIngredients.setLayoutManager(mLayoutManager);
        recyclerIngredients.setItemAnimator(new DefaultItemAnimator());
        recyclerIngredients.setAdapter(mAdapter);

    }
}
