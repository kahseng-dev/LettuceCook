package sg.edu.np.mad.lettucecook.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import sg.edu.np.mad.lettucecook.CreateRecipe;
import sg.edu.np.mad.lettucecook.R;
import sg.edu.np.mad.lettucecook.VolleyResponseListener;
import sg.edu.np.mad.lettucecook.models.ApiMeal;
import sg.edu.np.mad.lettucecook.api.ApiMealAdapter;
import sg.edu.np.mad.lettucecook.api.ApiMealJsonSingleton;
import sg.edu.np.mad.lettucecook.api.ApiService;
import sg.edu.np.mad.lettucecook.api.ApiURL;

public class MainActivity extends AppCompatActivity {
    static final String TAG = "MainActivity";

    ArrayList<ApiMeal> meals;
    ApiService apiService = new ApiService(MainActivity.this);
    ApiMealJsonSingleton apiMealJson = ApiMealJsonSingleton.getInstance();

    int browseType;
    Spinner browseTypeSpinner;
    Spinner browseTypeChoiceSpinner;
    Button browseButton, createRecipeButton;
    RecyclerView browseRV;
    ImageView featuredImage;
    TextView featuredName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // First spinner
        browseTypeSpinner = findViewById(R.id.main_browse_type_spinner); // First spinner, types of browse filters
        browseTypeChoiceSpinner = findViewById(R.id.main_browse_type_choice_spinner); // Second spinner, filters based on the first spinner
        browseButton = findViewById(R.id.main_browse_button); // Browse button
        browseRV = findViewById(R.id.main_browse_rv); // Browse recycler view

        // Populate the first spinner with the types of browse filters
        fillSpinner(browseTypeSpinner, getResources().getStringArray(R.array.browse_types));

        featuredImage = findViewById(R.id.featured_image);
        featuredImage .setVisibility(View.VISIBLE);
        featuredName = findViewById(R.id.main_featured_meal_name);
        featuredName.setVisibility(View.VISIBLE);

        createRecipeButton = findViewById(R.id.main_create_recipe_button);

        String query = "random.php";
        apiService.get(ApiURL.MealDB, query, new VolleyResponseListener() {

            @Override
            public void onError(String message) {
                Log.v(TAG, message);
            }

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray _meals = response.getJSONArray("meals");

                    // Make arrays from the flat JSON structure
                    meals = apiMealJson.mergeIntoJSONArray(_meals);

                    Picasso.with(MainActivity.this)
                            .load(meals.get(0).getStrMealThumb())
                            .into(featuredImage);
                    featuredName.setText(meals.get(0).getStrMeal());

                    featuredImage.setOnClickListener(view -> {
                        Intent intent = new Intent(MainActivity.this, RecipeDetailsActivity.class);
                        intent.putExtra("mealId", meals.get(0).getIdMeal());

                        if (getIntent().hasExtra("UserId")) {
                            Bundle extras = getIntent().getExtras();
                            int userId = extras.getInt("UserId");
                            intent.putExtra("UserId", userId);
                        }

                        startActivity(intent);
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        // Changes the contents of the second dropdown list when a different item
        // is selected in the first dropdown list.
        browseTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                browseType = i;
                String query = i == 0 ? "list.php?c=list" : "list.php?a=list";
                apiService.get(ApiURL.MealDB, query, new VolleyResponseListener() {
                    @Override
                    public void onError(String message) {
                        Log.v(TAG, message);
                    }

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray _filters = response.getJSONArray("meals");
                            String[] filters = apiMealJson.parseFilterArray(_filters);
                            fillSpinner(browseTypeChoiceSpinner, filters); // Populate second spinner
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        // Retrieves meals from the API when the button is tapped.
        browseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filter = browseTypeChoiceSpinner.getSelectedItem().toString();
                String query = "filter.php?" + (browseType == 0 ? "c=" : "a=") + filter; // Query for filtering

                apiService.get(ApiURL.MealDB, query, new VolleyResponseListener() {
                    @Override
                    public void onError(String message) {
                        Log.v(TAG, message);
                    }

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray _meals = response.getJSONArray("meals");
                            meals = apiMealJson.mergeIntoJSONArray(_meals);
                            Log.v("Meal", String.valueOf(meals.get(0)));

                            Bundle extras = getIntent().getExtras();
                            ApiMealAdapter mAdapter;

                            if (getIntent().hasExtra("UserId")) {
                                int userId = extras.getInt("UserId");
                                mAdapter = new ApiMealAdapter(meals, userId, MainActivity.this);
                            }
                            else mAdapter = new ApiMealAdapter(meals, MainActivity.this);

                            LinearLayoutManager mLayoutManager = new LinearLayoutManager(MainActivity.this);

                            browseRV.setLayoutManager(mLayoutManager);
                            browseRV.setItemAnimator(new DefaultItemAnimator());
                            browseRV.setAdapter(mAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.browse);

        // Switch pages when different navigation buttons are tapped
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.browse:
                        return true;

                    case R.id.account:
                        Intent accountIntent = new Intent(getApplicationContext(), AccountActivity.class);

                        if (getIntent().hasExtra("UserId")) {
                            Bundle extras = getIntent().getExtras();
                            int userId = extras.getInt("UserId");
                            accountIntent.putExtra("UserId", userId);
                        }

                        startActivity(accountIntent);
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.shoppingList:
                        Intent shoppingListIntent = new Intent(getApplicationContext(), ShoppingListActivity.class);

                        if (getIntent().hasExtra("UserId")) {
                            Bundle extras = getIntent().getExtras();
                            int userId = extras.getInt("UserId");
                            shoppingListIntent.putExtra("UserId", userId);
                        }

                        startActivity(shoppingListIntent);
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });

        createRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateRecipe.class);
                startActivity(intent);
            }
        });
    }

    // Populating a spinner
    private void fillSpinner(Spinner spinner, String[] items) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                MainActivity.this,
                android.R.layout.simple_spinner_item,
                items);

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
    }
}