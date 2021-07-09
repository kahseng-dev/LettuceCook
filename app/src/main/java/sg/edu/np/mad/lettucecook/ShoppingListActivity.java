package sg.edu.np.mad.lettucecook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import sg.edu.np.mad.lettucecook.Models.DBHandler;
import sg.edu.np.mad.lettucecook.Models.Ingredient;
import sg.edu.np.mad.lettucecook.Models.ShoppingListAdapter;
import sg.edu.np.mad.lettucecook.Models.User;

public class ShoppingListActivity extends AppCompatActivity {
    DBHandler dbHandler = new DBHandler(this , null, null, 1);
    ArrayList<Ingredient> ingredientList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        setTitle("Shopping List");

        if (getIntent().hasExtra("UserId")) {
            Bundle extras = getIntent().getExtras();
            int userId = extras.getInt("UserId");
            ingredientList = dbHandler.getShoppingList(userId);
        }

        RecyclerView recyclerView = findViewById(R.id.shoppingListRV);
        ShoppingListAdapter sAdapter = new ShoppingListAdapter(ingredientList);
        LinearLayoutManager mLayoutManger = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManger);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(sAdapter);

        // TODO: DO SOMETHING IF DATABASE IS EMPTY

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.shoppingList);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.browse:
                        Intent browseIntent = new Intent(getApplicationContext(), MainActivity.class);

                        if (getIntent().hasExtra("UserId")) {
                            Bundle extras = getIntent().getExtras();
                            int userId = extras.getInt("UserId");
                            browseIntent.putExtra("UserId", userId);
                        }

                        startActivity(browseIntent);
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.login:
                        Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);

                        if (getIntent().hasExtra("UserId")) {
                            Bundle extras = getIntent().getExtras();
                            int userId = extras.getInt("UserId");
                            loginIntent.putExtra("UserId", userId);
                        }

                        startActivity(loginIntent);
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.shoppingList:
                        return true;
                }
                return false;
            }
        });
    }
}