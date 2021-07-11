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

import java.util.ArrayList;

import sg.edu.np.mad.lettucecook.Models.ApiMeal;
import sg.edu.np.mad.lettucecook.Models.CreatedIngredient;

public class CreateRecipe extends AppCompatActivity {
    static final String TAG = "CreateRecipe";

    ArrayList<ApiMeal> meals;
    ArrayList<CreatedIngredient> ingredientList = new ArrayList<>();
    ApiService apiService = new ApiService(CreateRecipe.this);
    ApiMealJsonSingleton apiMealJson = ApiMealJsonSingleton.getInstance();

    Spinner recipeAreaSpinner, recipeCategorySpinner;

    LinearLayout layoutList;
    Button buttonAdd, createRecipeButton;
    EditText recipeName, recipeInstructions;

    String recipeAreaSpinnerValue, recipeCategorySpinnerValue, recipeNameValue, recipeInstructionsValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recipe);

        recipeAreaSpinner = findViewById(R.id.recipeAreaSpinner);
        recipeCategorySpinner = findViewById(R.id.recipeCategorySpinner);

        recipeName = findViewById(R.id.recipeName);
        recipeInstructions = findViewById(R.id.recipeInstructions);

        layoutList = findViewById(R.id.layout_list);
        buttonAdd = findViewById(R.id.addIngredientButton);
        createRecipeButton = findViewById(R.id.createRecipeButton);

        String areaQuery = "list.php?a=list";
        apiService.get(ApiURL.MealDB, areaQuery, new VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Log.v(TAG, message);
            }

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray _filters = response.getJSONArray("meals");
                    String[] filters = apiMealJson.parseFilterArray(_filters);
                    fillSpinner(recipeAreaSpinner, filters);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            public void onNothingSelected(AdapterView<?> adapterView) { }
        });

        String categoryQuery = "list.php?c=list";
        apiService.get(ApiURL.MealDB, categoryQuery, new VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Log.v(TAG, message);
            }

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray _filters = response.getJSONArray("meals");
                    String[] filters = apiMealJson.parseFilterArray(_filters);
                    fillSpinner(recipeCategorySpinner, filters);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            public void onNothingSelected(AdapterView<?> adapterView) { }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addView();
            }
        });

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

    private boolean checkIfValidAndRead() {
        ingredientList.clear();
        boolean result = true;

        for (int i=0; i<layoutList.getChildCount(); i++) {
            View ingredientView = layoutList.getChildAt(i);

            EditText editName = ingredientView.findViewById(R.id.edit_ingredient_name);
            EditText editMeasure = ingredientView.findViewById(R.id.edit_ingredient_measure);

            CreatedIngredient ingredient = new CreatedIngredient();

            if (!editName.getText().toString().equals("") && !editMeasure.getText().toString().equals("")) {
                ingredient.setIngredientName(editName.getText().toString());
                ingredient.setIngredientMeasure(editMeasure.getText().toString());
            }
            else { result = false; break; }

            ingredientList.add(ingredient);

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

    public void addView() {
        View ingredientView = getLayoutInflater().inflate(R.layout.row_add_ingredient, null, false);

        EditText editTextName = ingredientView.findViewById(R.id.edit_ingredient_name);
        EditText editTextMeasure = ingredientView.findViewById(R.id.edit_ingredient_measure);
        ImageView imageClose = ingredientView.findViewById(R.id.image_remove);

        layoutList.addView(ingredientView);

        imageClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeView(ingredientView);
            }
        });
    }

    public void removeView(View view) {
        layoutList.removeView(view);
    }

    private void fillSpinner(Spinner spinner, String[] items) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                CreateRecipe.this,
                android.R.layout.simple_spinner_item, items);

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
    }
}