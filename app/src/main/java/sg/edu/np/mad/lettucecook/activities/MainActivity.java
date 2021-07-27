package sg.edu.np.mad.lettucecook.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.widget.SearchView;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import sg.edu.np.mad.lettucecook.R;
import sg.edu.np.mad.lettucecook.models.CreatedRecipe;
import sg.edu.np.mad.lettucecook.rv.BrowseAdapter;
import sg.edu.np.mad.lettucecook.rv.CommunityRecipesAdapter;
import sg.edu.np.mad.lettucecook.utils.DataSingleton;

public class MainActivity extends AppCompatActivity {
    Context mContext = this;
    DataSingleton dataSingleton = DataSingleton.getInstance();

    RecyclerView browseRV, communityRV;

    private DatabaseReference reference;
    private ArrayList<String> communityRecipeIDList = new ArrayList<>();
    private ArrayList<CreatedRecipe> communityRecipes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set DataSingleton meals to null so that Browse will make a new request
        dataSingleton.setMeals(null);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Setup search within MealDB
        SearchView searchView = findViewById(R.id.main_search);
        searchView.setQueryHint("Search");
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length() == 0) query = "Random";
                dataSingleton.setMealQuery(query);
                startActivity(new Intent(mContext, BrowseActivity.class));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        browseRV = findViewById(R.id.main_browse_rv); // Browse recycler view
        communityRV = findViewById(R.id.main_community_rv);

        BrowseAdapter browseAdapter = new BrowseAdapter(mContext);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);

        browseRV.setLayoutManager(mLayoutManager);
        browseRV.setItemAnimator(new DefaultItemAnimator());
        browseRV.setAdapter(browseAdapter);

        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot usersList : snapshot.getChildren()) {
                    for (DataSnapshot createRecipe : usersList.child("createdRecipesList").getChildren()) {
                        if (createRecipe.hasChild("publishState") && (boolean) createRecipe.child("publishState").getValue()) {
                            communityRecipeIDList.add(createRecipe.getKey());
                            communityRecipes.add(createRecipe.getValue(CreatedRecipe.class));
                        }
                    }
                }

                CommunityRecipesAdapter communityAdapter = new CommunityRecipesAdapter(communityRecipes, communityRecipeIDList,mContext);
                LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
                communityRV.setLayoutManager(mLayoutManager);
                communityRV.setItemAnimator(new DefaultItemAnimator());
                communityRV.setAdapter(communityAdapter);
            }

            // Validation in case cancelled
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(mContext, "Unable to retrieve community information!", Toast.LENGTH_LONG).show();
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

    @Override
    public void onBackPressed() { }
}