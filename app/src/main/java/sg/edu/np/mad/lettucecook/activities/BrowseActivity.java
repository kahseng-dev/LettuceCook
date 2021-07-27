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
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import sg.edu.np.mad.lettucecook.R;
import sg.edu.np.mad.lettucecook.models.ApiMeal;
import sg.edu.np.mad.lettucecook.rv.ApiMealAdapter;
import sg.edu.np.mad.lettucecook.rv.BrowseAdapter;
import sg.edu.np.mad.lettucecook.utils.ApiJsonSingleton;
import sg.edu.np.mad.lettucecook.utils.ApiService;
import sg.edu.np.mad.lettucecook.utils.ApiURL;
import sg.edu.np.mad.lettucecook.utils.VolleyResponseListener;

public class BrowseActivity extends AppCompatActivity {
    Context mContext = this;
    ApiService apiService = new ApiService(mContext);
    ApiJsonSingleton apiJson = ApiJsonSingleton.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        RecyclerView browseRV = findViewById(R.id.browse_browse_rv);

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_black_arrow_back);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // get query from intent
        Intent intent = getIntent();
        String _query = intent.getStringExtra("query");
        String query = _query.equals("Random") ? "randomselection.php" : "filter.php?c=" + _query;

        // Setup search within results
        SearchView searchView = findViewById(R.id.browse_search);
        searchView.setQueryHint("Search within \"" + _query + "\"");
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(getBaseContext(), query, Toast.LENGTH_LONG).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        apiService.get(ApiURL.MealDB, query, new VolleyResponseListener() {
            @Override
            public void onError(String message) {Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show(); }

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray _meals = response.getJSONArray("meals");
                    ArrayList<ApiMeal> meals = apiJson.mergeIntoJSONArray(_meals);

                    ApiMealAdapter mAdapter = new ApiMealAdapter(meals, _query, mContext);

                    LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext);

                    browseRV.setLayoutManager(mLayoutManager);
                    browseRV.setItemAnimator(new DefaultItemAnimator());
                    browseRV.setAdapter(mAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_notifications_toolbar, menu);
        menu.findItem(R.id.notification).setIcon(R.drawable.ic_notifications_black);
        return true;
    }

    // toolbar buttons
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // bring user back to the main activity
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                return true;

            case R.id.notification:
                // bring user to notification activity
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