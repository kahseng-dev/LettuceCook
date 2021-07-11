package sg.edu.np.mad.lettucecook;

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

import sg.edu.np.mad.lettucecook.Models.ApiMeal;

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

        browseTypeSpinner = findViewById(R.id.main_browse_type_spinner);
        browseTypeChoiceSpinner = findViewById(R.id.main_browse_type_choice_spinner);
        browseButton = findViewById(R.id.main_browse_button);
        browseRV = findViewById(R.id.main_browse_rv);

        fillSpinner(browseTypeSpinner, getResources().getStringArray(R.array.browse_types));

        featuredImage = findViewById(R.id.featured_image);
        featuredImage .setVisibility(View.VISIBLE);
        featuredName = findViewById(R.id.featured_meal_text);
        featuredName.setVisibility(View.VISIBLE);

        createRecipeButton = findViewById(R.id.create_recipe_button);

        String query = "random.php";
        apiService.get(ApiURL.MealDB, query, null, new VolleyResponseListener() {

            @Override
            public void onError(String message) {
                Log.v(TAG, message);
            }

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray _meals = response.getJSONArray("meals");
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

        browseTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                browseType = i;
                String query = i == 0 ? "list.php?c=list" : "list.php?a=list";
                apiService.get(ApiURL.MealDB, query, null, new VolleyResponseListener() {
                    @Override
                    public void onError(String message) {
                        Log.v(TAG, message);
                    }

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray _filters = response.getJSONArray("meals");
                            String[] filters = apiMealJson.parseFilterArray(_filters);
                            fillSpinner(browseTypeChoiceSpinner, filters);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        browseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filter = browseTypeChoiceSpinner.getSelectedItem().toString();
                String query = "filter.php?" + (browseType == 0 ? "c=" : "a=") + filter;
                apiService.get(ApiURL.MealDB, query, null, new VolleyResponseListener() {
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
                                mAdapter = new ApiMealAdapter(meals, userId,MainActivity.this);
                            }

                            else mAdapter = new ApiMealAdapter(meals,MainActivity.this);

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

    private void fillSpinner(Spinner spinner, String[] items) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                MainActivity.this,
                android.R.layout.simple_spinner_item,
                items);

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
    }
}