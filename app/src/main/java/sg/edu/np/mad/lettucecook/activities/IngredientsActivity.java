package sg.edu.np.mad.lettucecook.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import sg.edu.np.mad.lettucecook.models.CreatedRecipe;
import sg.edu.np.mad.lettucecook.models.DBHandler;
import sg.edu.np.mad.lettucecook.models.Ingredient;
import sg.edu.np.mad.lettucecook.models.User;
import sg.edu.np.mad.lettucecook.rv.AccountRecipesAdapter;
import sg.edu.np.mad.lettucecook.rv.CreatedIngredientAdapter;
import sg.edu.np.mad.lettucecook.R;
import sg.edu.np.mad.lettucecook.models.CreatedIngredient;

public class IngredientsActivity extends AppCompatActivity {

    RecyclerView recyclerIngredients;
    ArrayList<CreatedIngredient> ingredientList = new ArrayList<>();
    ArrayList<CreatedRecipe> createdRecipeList = new ArrayList<>();

    String recipeNameValue, recipeAreaSpinnerValue, recipeCategorySpinnerValue, recipeInstructionsValue;
    TextView createdRecipeName, createdRecipeArea, createdRecipeInstructions, createdRecipeCategory;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID, recipeID;
    private Toolbar toolbar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_recipes);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_black_arrow_back);
        toolbar.findViewById(R.id.app_logo).setVisibility(View.INVISIBLE);

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

        // ===========================================================================================================================================
        CreatedRecipe createdRecipe = (CreatedRecipe) getIntent().getSerializableExtra("Recipe");

        // Set recipe information to createdRecipe texts -- CHANGE
        createdRecipeName.setText(createdRecipe.recipeName);
        createdRecipeArea.setText(createdRecipe.recipeArea);
        createdRecipeCategory.setText(createdRecipe.recipeCategory);
        createdRecipeInstructions.setText(createdRecipe.recipeInstructions);

        // Set ingredients list to recycler view -- CHANGE
        CreatedIngredientAdapter mAdapter = new CreatedIngredientAdapter(createdRecipe.ingredientList, IngredientsActivity.this);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(IngredientsActivity.this);

        recyclerIngredients.setLayoutManager(mLayoutManager);
        recyclerIngredients.setItemAnimator(new DefaultItemAnimator());
        recyclerIngredients.setAdapter(mAdapter);
    }

    // if the user clicks on the back button in the toolbar, bring them back to login activity.
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(getApplicationContext(), AccountRecipesActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), AccountRecipesActivity.class));
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}