package sg.edu.np.mad.lettucecook.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

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

import sg.edu.np.mad.lettucecook.R;
import sg.edu.np.mad.lettucecook.utils.VolleyResponseListener;
import sg.edu.np.mad.lettucecook.models.ApiMeal;
import sg.edu.np.mad.lettucecook.rv.ApiMealAdapter;
import sg.edu.np.mad.lettucecook.utils.ApiJsonSingleton;
import sg.edu.np.mad.lettucecook.utils.ApiService;
import sg.edu.np.mad.lettucecook.utils.ApiURL;

public class MainActivity extends AppCompatActivity {
    ArrayList<ApiMeal> meals;
    ApiService apiService = new ApiService(MainActivity.this);
    ApiJsonSingleton apiJson = ApiJsonSingleton.getInstance();

    int browseType;
    Spinner browseTypeSpinner, browseTypeChoiceSpinner;
    Button browseButton;
    RecyclerView browseRV;
    ImageView featuredImage;
    TextView featuredName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // First spinner
        browseTypeSpinner = findViewById(R.id.main_browse_type_spinner); // First spinner, types of browse filters
        browseTypeChoiceSpinner = findViewById(R.id.main_browse_type_choice_spinner); // Second spinner, filters based on the first spinner
        browseButton = findViewById(R.id.main_browse_button); // Browse button
        browseRV = findViewById(R.id.main_browse_rv); // Browse recycler view

        featuredImage = findViewById(R.id.featured_image);
        featuredImage.setVisibility(View.VISIBLE);
        featuredName = findViewById(R.id.main_featured_meal_name);
        featuredName.setVisibility(View.VISIBLE);

        String query = "random.php";
        apiService.get(ApiURL.MealDB, query, new VolleyResponseListener() {

            @Override
            public void onError(String message) {

            }

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray _meals = response.getJSONArray("meals");

                    // Make arrays from the flat JSON structure
                    meals = apiJson.mergeIntoJSONArray(_meals);

                    Picasso.with(MainActivity.this)
                            .load(meals.get(0).getStrMealThumb())
                            .into(featuredImage);
                    featuredName.setText(meals.get(0).getStrMeal());

                    featuredImage.setOnClickListener(view -> {
                        Intent intent = new Intent(MainActivity.this, RecipeDetailsActivity.class);
                        intent.putExtra("mealId", meals.get(0).getIdMeal());
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        // Populate the first spinner with the types of browse filters
        fillSpinner(browseTypeSpinner, getResources().getStringArray(R.array.browse_types));
        
        // Changes the contents of the second dropdown list when a different item
        // is selected in the first dropdown list.
        browseTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                browseType = i;
                int resource = i == 0 ? R.array.browse_categories : R.array.browse_areas;
                fillSpinner(browseTypeChoiceSpinner, getResources().getStringArray(resource));
            }

            public void onNothingSelected(AdapterView<?> adapterView) { }
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

                    }

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray _meals = response.getJSONArray("meals");
                            meals = apiJson.mergeIntoJSONArray(_meals);
                            Log.v("Meal", String.valueOf(meals.get(0)));

                            Bundle extras = getIntent().getExtras();
                            ApiMealAdapter mAdapter = new ApiMealAdapter(meals, MainActivity.this);

                            LinearLayoutManager mLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);

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
                        startActivity(new Intent(getApplicationContext(), AccountActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.create_recipe:
                        startActivity(new Intent(getApplicationContext(), CreateRecipeActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.shoppingList:
                        startActivity(new Intent(getApplicationContext(), ShoppingListActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_notifications_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.notification:
                startActivity(new Intent(getApplicationContext(), NotificationActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return true;
        }
        return false;
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

    @Override
    public void onBackPressed() { }
}