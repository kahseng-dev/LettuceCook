package sg.edu.np.mad.lettucecook.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import sg.edu.np.mad.lettucecook.models.CreatedRecipe;
import sg.edu.np.mad.lettucecook.rv.CreatedIngredientAdapter;
import sg.edu.np.mad.lettucecook.R;
import sg.edu.np.mad.lettucecook.models.CreatedIngredient;

public class CustomRecipeDetailsActivity extends AppCompatActivity {

    RecyclerView recyclerIngredients;
    ArrayList<CreatedIngredient> ingredientList = new ArrayList<>();
    ArrayList<CreatedRecipe> createdRecipeList = new ArrayList<>();

    String recipeNameValue, recipeAreaSpinnerValue, recipeCategorySpinnerValue, recipeInstructionsValue;
    TextView createdRecipeName, createdRecipeArea, createdRecipeInstructions, createdRecipeCategory;
    Button backToRecipesListButton;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID, recipeID;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_recipes);

        // Get userID from Firebase
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        // Find recyclerview to display each recipe
        recyclerIngredients = findViewById(R.id.custom_recipe_ingredients_rv);

        // Find createdRecipe texts to display info
        createdRecipeName = findViewById(R.id.custom_recipe_name);
        createdRecipeArea = findViewById(R.id.custom_recipe_area);
        createdRecipeInstructions = findViewById(R.id.custom_recipe_instructions);
        createdRecipeCategory = findViewById(R.id.custom_recipe_category);

        // Find button id for addToRecipeListButton
        backToRecipesListButton = findViewById(R.id.backToRecipesListButton);

        // ===========================================================================================================================================
        CreatedRecipe createdRecipe = (CreatedRecipe) getIntent().getSerializableExtra("Recipe");

        // Set recipe information to createdRecipe texts -- CHANGE
        createdRecipeName.setText(createdRecipe.recipeName);
        createdRecipeArea.setText(createdRecipe.recipeArea);
        createdRecipeCategory.setText(createdRecipe.recipeCategory);
        createdRecipeInstructions.setText(createdRecipe.recipeInstructions);

        // Set ingredients list to recycler view -- CHANGE
        CreatedIngredientAdapter mAdapter = new CreatedIngredientAdapter(createdRecipe.ingredientList, CustomRecipeDetailsActivity.this);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(CustomRecipeDetailsActivity.this);

        recyclerIngredients.setLayoutManager(mLayoutManager);
        recyclerIngredients.setItemAnimator(new DefaultItemAnimator());
        recyclerIngredients.setAdapter(mAdapter);

        // ===========================================================================================================================================

        // Back to recipe list
        backToRecipesListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomRecipeDetailsActivity.this, AccountRecipesActivity.class);
                startActivity(intent);
            }
        });
    }
}
