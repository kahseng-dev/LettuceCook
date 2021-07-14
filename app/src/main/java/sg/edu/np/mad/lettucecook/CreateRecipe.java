package sg.edu.np.mad.lettucecook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import sg.edu.np.mad.lettucecook.Models.ApiMeal;
import sg.edu.np.mad.lettucecook.Models.CreatedIngredient;
import sg.edu.np.mad.lettucecook.Models.Ingredient;

public class CreateRecipe extends AppCompatActivity {
    static final String TAG = "CreateRecipe";

    // Initiate arrays
    ArrayList<ApiMeal> meals;
    ArrayList<CreatedIngredient> ingredientList = new ArrayList<>();

    // Initiate API meal
    ApiMealService apiMealService = new ApiMealService(CreateRecipe.this);
    ApiMealJsonSingleton apiMealJson = ApiMealJsonSingleton.getInstance();

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

        // Find Spinner IDs
        recipeAreaSpinner = findViewById(R.id.recipeAreaSpinner);
        recipeCategorySpinner = findViewById(R.id.recipeCategorySpinner);

        // Find EditText names and instructions
        recipeName = findViewById(R.id.recipeName);
        recipeInstructions = findViewById(R.id.recipeInstructions);

        // Find buttons and layoutlists
        layoutList = findViewById(R.id.layout_list);
        buttonAdd = findViewById(R.id.addIngredientButton);
        createRecipeButton = findViewById(R.id.createRecipeButton);

        // Set string to call Area list for API
        String areaQuery = "list.php?a=list";
        apiMealService.getMeals(areaQuery, new VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Log.v(TAG, message);
            }

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray _filters = response.getJSONArray("meals");
                    String[] filters = apiMealJson.parseFilterArray(_filters);
                    fillSpinner(recipeAreaSpinner, filters); // Fill up spinner with response
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            public void onNothingSelected(AdapterView<?> adapterView) { }
        });

        // Set string to call Category list for API
        String categoryQuery = "list.php?c=list";
        apiMealService.getMeals(categoryQuery, new VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Log.v(TAG, message);
            }

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray _filters = response.getJSONArray("meals");
                    String[] filters = apiMealJson.parseFilterArray(_filters);
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

        // Set create recipe on click method
        createRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkIfValidAndRead()) {
                    recipeAreaSpinnerValue = recipeAreaSpinner.getSelectedItem().toString();
                    recipeCategorySpinnerValue = recipeCategorySpinner.getSelectedItem().toString();
                    recipeNameValue = recipeName.getText().toString();
                    recipeInstructionsValue = recipeInstructions.getText().toString();

                    Intent intent = new Intent(CreateRecipe.this, IngredientsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("ARRAYLIST", ingredientList);
                    bundle.putString("recipeAreaSpinnerValue", recipeAreaSpinnerValue);
                    bundle.putString("recipeCategorySpinnerValue", recipeCategorySpinnerValue);
                    bundle.putString("recipeNameValue", recipeNameValue);
                    bundle.putString("recipeInstructionsValue", recipeInstructionsValue);
                    intent.putExtras(bundle);

                    startActivity(intent);
                }
            }

        });
    }

    // Function to validate user input for Create Recipe form
    private boolean checkIfValidAndRead() {
        ingredientList.clear();
        boolean result = true;

        // Loop through according to the number of ingredients in the layoutlist
        for (int i=0; i<layoutList.getChildCount(); i++) {
            View ingredientView = layoutList.getChildAt(i);

            // Find EditText IDs
            EditText editName = ingredientView.findViewById(R.id.edit_ingredient_name);
            EditText editMeasure = ingredientView.findViewById(R.id.edit_ingredient_measure);

            // Create new CreatedIngredient object
            CreatedIngredient ingredient = new CreatedIngredient();

            // Check if both Recipe Name & Recipe Measure inputs are empty
            if (!editName.getText().toString().equals("") && !editMeasure.getText().toString().equals("")) {
                ingredient.setIngredientName(editName.getText().toString());
                ingredient.setIngredientMeasure(editMeasure.getText().toString());
            }
            else { result = false; break; }

            // Add ingredient to ingredientList
            ingredientList.add(ingredient);

            // Check if ingredientList is empty
            if (ingredientList.size() == 0) {
                result = false;
                Toast.makeText(this, "Add Ingredients First!", Toast.LENGTH_SHORT).show();
            }
            else if (!result) {
                Toast.makeText(this, "Enter all details correctly!", Toast.LENGTH_SHORT).show();
            }
        }

        return result;
    }

    // Function to addView everytime user clicks on click
    public void addView() {
        View ingredientView = getLayoutInflater().inflate(R.layout.row_add_ingredient, null, false);

        EditText editTextName = ingredientView.findViewById(R.id.edit_ingredient_name);
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
                CreateRecipe.this,
                android.R.layout.simple_spinner_item, items);

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
    }
}