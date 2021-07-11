package sg.edu.np.mad.lettucecook;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import sg.edu.np.mad.lettucecook.Models.CreatedIngredient;

public class IngredientsActivity extends AppCompatActivity {

    RecyclerView recyclerIngredients;
    ArrayList<CreatedIngredient> ingredientList = new ArrayList<>();
    String recipeNameValue, recipeAreaSpinnerValue, recipeCategorySpinnerValue, recipeInstructionsValue;
    TextView createdRecipeName, createdRecipeArea, createdRecipeInstructions, createdRecipeCategory;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_recipes);

        recyclerIngredients = findViewById(R.id.recycler_ingredients);
        createdRecipeName = findViewById(R.id.createdRecipeName);
        createdRecipeArea = findViewById(R.id.createdRecipeArea);
        createdRecipeInstructions = findViewById(R.id.createdRecipeInstructions);
        createdRecipeCategory = findViewById(R.id.createdRecipeCategory);

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
