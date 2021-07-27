package sg.edu.np.mad.lettucecook.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import sg.edu.np.mad.lettucecook.R;
import sg.edu.np.mad.lettucecook.models.ApiMeal;
import sg.edu.np.mad.lettucecook.rv.ApiMealAdapter;
import sg.edu.np.mad.lettucecook.utils.ApiJsonSingleton;
import sg.edu.np.mad.lettucecook.utils.ApiService;
import sg.edu.np.mad.lettucecook.utils.ApiURL;
import sg.edu.np.mad.lettucecook.utils.DataSingleton;
import sg.edu.np.mad.lettucecook.utils.VolleyResponseListener;

public class BrowseActivity extends AppCompatActivity {
    Context mContext = this;
    String dataSingletonQuery;
    ArrayList<ApiMeal> meals;
    ApiService apiService = new ApiService(mContext);
    ApiJsonSingleton apiJson = ApiJsonSingleton.getInstance();
    DataSingleton dataSingleton = DataSingleton.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        dataSingleton.setMeal(null);

        RecyclerView browseRV = findViewById(R.id.browse_browse_rv);
        ApiMealAdapter mAdapter = new ApiMealAdapter(mContext);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext);

        browseRV.setLayoutManager(mLayoutManager);
        browseRV.setItemAnimator(new DefaultItemAnimator());
        browseRV.setAdapter(mAdapter);

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_black_arrow_back);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Setup search within results
        SearchView searchView = findViewById(R.id.browse_search);
        dataSingletonQuery = dataSingleton.getMealQuery();
        searchView.setQueryHint("Search within \"" + dataSingletonQuery + "\"");
        searchView.setIconifiedByDefault(false);

        // Search for results within the current data set
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                mAdapter.filter(query);
                showToast();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.filter(newText);
                showToast();
                return true;
            }

            private void showToast() {
                int nResults = mAdapter.getItemCount(); // No. of results
                String text = nResults + (nResults == 1 ? " result" : " results");
                Toast.makeText(getBaseContext(), text, Toast.LENGTH_SHORT).show();
            }
        });

        // Get data from singleton.
        // If null: from MainActivity, make a new request.
        // Else: from RecipeDetails or Notification, display the same meals.
        meals = dataSingleton.getMeals();
        if (meals != null) {
            mAdapter.setData(meals);
            mAdapter.notifyDataSetChanged();
        } else {
            dataSingleton.setMeals(new ArrayList<>());

            String[] browseCategories = getResources().getStringArray(R.array.browse_categories);
            String[] browseAreas = getResources().getStringArray(R.array.browse_areas);
            String[] queryList;

            if (dataSingletonQuery.equals("Random")) {
                queryList = new String[] { "randomselection.php" };
            } else if (dataSingletonQuery.length() == 1) {
                queryList = new String[] { "filter.php?f=" + dataSingletonQuery };
            } else if (isQueryInStrArr(browseCategories, dataSingletonQuery)) {
                queryList = new String[] { "filter.php?c=" + dataSingletonQuery };
            } else if (isQueryInStrArr(browseAreas, dataSingletonQuery)) {
                queryList = new String[] { "filter.php?a=" + dataSingletonQuery };
            } else {
                queryList = new String[] {
                        "search.php?s=" + dataSingletonQuery,
                        "filter.php?i=" + dataSingletonQuery,
                        "filter.php?c=" + dataSingletonQuery,
                        "filter.php?a=" + dataSingletonQuery,
                };
            }
            for (String query : queryList) {
                apiService.get(ApiURL.MealDB, query, new VolleyResponseListener() {
                    @Override
                    public void onError(String message) {

                    }

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray _meals = response.getJSONArray("meals");
                            meals = apiJson.mergeIntoJSONArray(_meals);
                            mAdapter.addData(meals);
                            mAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    }

    private boolean isQueryInStrArr(String[] arr, String query) {
        for (String str : arr) {
            if (str.toLowerCase() == query.toLowerCase()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_notifications_toolbar, menu);
        menu.findItem(R.id.notification).setIcon(R.drawable.ic_notifications_black);
        return true;
    }

    // if the user clicks on the back button in the toolbar, bring them back to main activity.
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                return true;

            case R.id.notification:
                Intent intent = new Intent(getApplicationContext(), NotificationActivity.class);
                intent.putExtra("layoutId", R.layout.activity_browse);

                if (getIntent().hasExtra("mealId")) {
                    Bundle extras = getIntent().getExtras();
                    String mealId = extras.getString("mealId");
                    intent.putExtra("mealId", mealId);
                }

                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}