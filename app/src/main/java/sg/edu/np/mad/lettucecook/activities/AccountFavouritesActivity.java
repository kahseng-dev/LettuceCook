package sg.edu.np.mad.lettucecook.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import sg.edu.np.mad.lettucecook.R;
import sg.edu.np.mad.lettucecook.models.ApiMeal;
import sg.edu.np.mad.lettucecook.rv.ApiMealAdapter;
import sg.edu.np.mad.lettucecook.utils.ApiJsonSingleton;
import sg.edu.np.mad.lettucecook.utils.ApiService;
import sg.edu.np.mad.lettucecook.utils.DataSingleton;
import sg.edu.np.mad.lettucecook.utils.VolleyResponseListener;

public class AccountFavouritesActivity extends AppCompatActivity {
    DataSingleton dataSingleton = DataSingleton.getInstance();

    private Toolbar toolbar;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    private Context mContext = this;
    private ApiService apiService = new ApiService(mContext);
    private ApiJsonSingleton apiJson = ApiJsonSingleton.getInstance();
    private RecyclerView favouritesRV;
    private TextView noAccountFavouritesText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_favourites);

        dataSingleton.setReturnTo(2);

        // toolbar setup
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_black_arrow_back);
        toolbar.findViewById(R.id.app_logo).setVisibility(View.INVISIBLE);

        noAccountFavouritesText = findViewById(R.id.account_no_favourites);

        // get current user
        user = FirebaseAuth.getInstance().getCurrentUser();

        // if user is not logged in bring them to login activity
        if (user == null) {
            startActivity(new Intent(AccountFavouritesActivity.this, LoginActivity.class));
            finish();
        }

        else {
            // get user data
            reference = FirebaseDatabase.getInstance().getReference("Users");
            userID = user.getUid();

            // setup favourites recycler view
            favouritesRV = findViewById(R.id.account_favourites_rv);
            LinearLayoutManager mLayoutManger = new LinearLayoutManager(AccountFavouritesActivity.this);
            ApiMealAdapter mAdapter = new ApiMealAdapter(mContext);
            favouritesRV.setLayoutManager(mLayoutManger);
            favouritesRV.setItemAnimator(new DefaultItemAnimator());
            favouritesRV.setAdapter(mAdapter);

            ArrayList<ApiMeal> meals = dataSingleton.getMeals();

            // If coming from RecipeDetails, display same data
            if (meals != null) {
                mAdapter.setData(meals);
                mAdapter.notifyDataSetChanged();
                return;
            }

            // Retrieve data from Firebase
            reference.child(userID).child("favouritesList").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // Loop through each favourite recipe in favouritesList
                    for (DataSnapshot favouritesSnapshot : snapshot.getChildren())
                    {
                        // get each recipe details from MealDB
                        apiService.get("lookup.php?i=" + favouritesSnapshot.getValue().toString(), new VolleyResponseListener() {
                            @Override
                            public void onError(String message) { }

                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONArray _meals = response.getJSONArray("meals");
                                    ApiMeal meal = apiJson.mergeIntoJSONArray(_meals).get(0);

                                    dataSingleton.addMeal(meal);
                                    // add meal data to adapter and display on RV
                                    mAdapter.addData(meal);
                                    mAdapter.notifyDataSetChanged();
                                }

                                catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }

                    // Check if favouritesList is empty
                    if (!snapshot.hasChildren()) {
                        favouritesRV.setVisibility(View.GONE); // Hide recyclerview if empty
                        noAccountFavouritesText.setVisibility(View.VISIBLE); // Show no account favourites message if empty
                    }

                    else {
                        favouritesRV.setVisibility(View.VISIBLE); // Show recyclerview if not empty
                        noAccountFavouritesText.setVisibility(View.GONE); // Hide no account favourites message if not empty
                    }
                }

                // Validation in case cancelled
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(AccountFavouritesActivity.this, "Unable to retrieve favourites information!", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    // if the user clicks on the back button in the toolbar, bring user back to account activity.
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                dataSingleton.setMeals(null);
                startActivity(new Intent(getApplicationContext(), AccountActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}