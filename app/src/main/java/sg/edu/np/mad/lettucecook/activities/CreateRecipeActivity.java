package sg.edu.np.mad.lettucecook.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import sg.edu.np.mad.lettucecook.R;
import sg.edu.np.mad.lettucecook.models.CreatedRecipe;
import sg.edu.np.mad.lettucecook.models.NinjaIngredient;
import sg.edu.np.mad.lettucecook.rv.ApiIngredientsAdapter;
import sg.edu.np.mad.lettucecook.utils.IngredientClickListener;
import sg.edu.np.mad.lettucecook.utils.VolleyResponseListener;
import sg.edu.np.mad.lettucecook.models.ApiMeal;
import sg.edu.np.mad.lettucecook.models.CreatedIngredient;
import sg.edu.np.mad.lettucecook.utils.ApiJsonSingleton;
import sg.edu.np.mad.lettucecook.utils.ApiService;
import sg.edu.np.mad.lettucecook.utils.ApiURL;

public class CreateRecipeActivity extends AppCompatActivity {
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    private Context mContext = CreateRecipeActivity.this;

    // Initiate arrays
    ArrayList<ApiMeal> meals;
    ArrayList<CreatedIngredient> ingredientList = new ArrayList<>();

    // Initiate API meal
    ApiService apiService = new ApiService(mContext);
    ApiJsonSingleton apiJson = ApiJsonSingleton.getInstance();

    // Initiate Spinner, Layout, Button, EditTexts & Strings
    Spinner recipeAreaSpinner, recipeCategorySpinner;

    LinearLayout layoutList;
    Button buttonAdd, createRecipeButton;
    EditText recipeName, recipeInstructions;

    String recipeAreaSpinnerValue, recipeCategorySpinnerValue, recipeNameValue, recipeInstructionsValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recipe); // Set ContentView of Activity

        // Get userID from Firebase
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        // Find Spinner IDs
        recipeAreaSpinner = findViewById(R.id.create_recipe_area_spinner);
        recipeCategorySpinner = findViewById(R.id.create_recipe_category_spinner);

        // Find EditText names and instructions
        recipeName = findViewById(R.id.create_recipe_name);
        recipeInstructions = findViewById(R.id.create_recipe_instructions);

        // Find buttons and layout lists
        layoutList = findViewById(R.id.create_recipe_layout_list);
        buttonAdd = findViewById(R.id.addIngredientButton);
        createRecipeButton = findViewById(R.id.create_recipe_create_button);

        // Set string to call Area list for API
        String areaQuery = "list.php?a=list";
        apiService.get(ApiURL.MealDB, areaQuery, new VolleyResponseListener() {
            @Override
            public void onError(String message) {

            }

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray _filters = response.getJSONArray("meals");
                    String[] filters = apiJson.parseFilterArray(_filters);
                    fillSpinner(recipeAreaSpinner, filters); // Fill up spinner with response
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            public void onNothingSelected(AdapterView<?> adapterView) { }
        });

        // Set string to call Category list for API
        String categoryQuery = "list.php?c=list";
        apiService.get(ApiURL.MealDB, categoryQuery, new VolleyResponseListener() {
            @Override
            public void onError(String message) { }

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray _filters = response.getJSONArray("meals");
                    String[] filters = apiJson.parseFilterArray(_filters);
                    fillSpinner(recipeCategorySpinner, filters); // Fill up spinner with response
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            public void onNothingSelected(AdapterView<?> adapterView) { }
        });

        // Set add button on click
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addView();
            }
        });

        // Create recipes button
        createRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // Get values of user inputs
                    recipeAreaSpinnerValue = recipeAreaSpinner.getSelectedItem().toString();
                    recipeCategorySpinnerValue = recipeCategorySpinner.getSelectedItem().toString();
                    recipeNameValue = recipeName.getText().toString();
                    recipeInstructionsValue = recipeInstructions.getText().toString();

                    // Create new CreatedRecipe object
                    CreatedRecipe createdRecipe = new CreatedRecipe(recipeNameValue, recipeAreaSpinnerValue, recipeCategorySpinnerValue, recipeInstructionsValue, ingredientList);

                    String ninjaQuery = checkIfValidAndRead();
                    // Request ingredients' nutritional information
                    apiService.getIngredient(ApiURL.CalorieNinjas, ninjaQuery, new VolleyResponseListener() {

                        @Override
                        public void onError(String message) {
                            Toast.makeText(mContext, "Error retrieving ingredient info", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onResponse(JSONObject response) throws JSONException {
                            ArrayList<NinjaIngredient> ninjaIngredients = apiJson.parseNinjaIngredients
                                    (response.getJSONArray("items"), ingredientList);

                            // Ingredients whose nutritional information could not be found
                            String errorIngredients = "";
                            for (NinjaIngredient i : ninjaIngredients) {
                                if (i.getCalories() < 0) errorIngredients += i.getMeasure() + " " + i.getName() + "\n";
//                                    errorIngredients.add(i);
                            }

                            if (errorIngredients.length() > 0) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                builder
                                        .setTitle("Ingredients' nutritional information not found")
                                        .setMessage(errorIngredients)
                                        .setCancelable(true)
                                        .setNegativeButton("Continue editing", null)
                                        .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                addRecipeToFirebase(createdRecipe);
                                            }
                                        })
                                        .show();
                            } else {
                                addRecipeToFirebase(createdRecipe);
                            }
                        }
                    });
                } catch (Exception e) {
                    Toast.makeText(mContext, "Please fill in any empty fields!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // setting id of navigation bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Switch pages when different navigation buttons are tapped
        bottomNavigationView.setSelectedItemId(R.id.create_recipe);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {

                    // if the user clicks on browse
                    case R.id.browse:
                        // bring user to main activity
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                    // if the user clicks on account
                    case R.id.account:
                        startActivity(new Intent(getApplicationContext(), AccountActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                    // if the user clicks on create recipe, do nothing
                    case R.id.create_recipe:
                        return true;

                    // if the user clicks on shopping list
                    case R.id.shoppingList:
                        // bring user to shopping activity
                        startActivity(new Intent(getApplicationContext(), ShoppingListActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
    }

    // Function to validate user input for Create Recipe form
    private String checkIfValidAndRead() throws Exception {
        ingredientList.clear();
        String ninjaQuery = "";
        // Loop through according to the number of ingredients in the layout list
        for (int i=0; i<layoutList.getChildCount(); i++) {
            View ingredientView = layoutList.getChildAt(i);

            // Find EditText IDs
            EditText editName = ingredientView.findViewById(R.id.row_add_ingredient_edit_name);
            EditText editMeasure = ingredientView.findViewById(R.id.edit_ingredient_measure);

            // Create new CreatedIngredient object
            CreatedIngredient ingredient = new CreatedIngredient();

            // Check if both Recipe Name & Recipe Measure inputs are empty
            if (!editName.getText().toString().equals("") && !editMeasure.getText().toString().equals("")) {
                String ingredientName = editName.getText().toString();
                String ingredientMeasure = editMeasure.getText().toString();
                ingredient.setIngredientName(ingredientName);
                ingredient.setIngredientMeasure(ingredientMeasure);
                ninjaQuery += ingredientMeasure + " " + ingredientName + ", ";
            } else {
                throw new Exception("Enter all details correctly");
            }

            // Add ingredient to ingredientList
            ingredientList.add(ingredient);

            // Check if ingredientList is empty
            if (ingredientList.size() == 0) {
                throw new Exception("Add ingredients first");
            }
        }

        // Remove the trailing ", "
        ninjaQuery = ninjaQuery.substring(0, ninjaQuery.length() - 2);
        return ninjaQuery;
    }

    private void addRecipeToFirebase(CreatedRecipe createdRecipe) {
        // Get child references
        reference.child(userID)
                .child("createdRecipesList").push()
                .setValue(createdRecipe)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(mContext, "Added Recipe Successfully", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(mContext, "Failed to Add Recipe!\n" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

        // Start new intent
        Intent intent = new Intent(mContext, AccountRecipesActivity.class);
        startActivity(intent);
    }
    // Function to addView everytime user clicks on click
    public void addView() {
        View ingredientView = getLayoutInflater().inflate(R.layout.row_add_ingredient, null, false);

        EditText editTextName = ingredientView.findViewById(R.id.row_add_ingredient_edit_name);
        EditText editTextMeasure = ingredientView.findViewById(R.id.edit_ingredient_measure);
        ImageView imageClose = ingredientView.findViewById(R.id.image_remove);

        layoutList.addView(ingredientView); // Add user view to layout list

        // Set on click to image close
        imageClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeView(ingredientView);
            }
        });
    }

    // Delete user ingredient function
    public void removeView(View view) {
        layoutList.removeView(view);
    }

    // Fill spinner
    private void fillSpinner(Spinner spinner, String[] items) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                mContext,
                android.R.layout.simple_spinner_item, items);

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
    }
}