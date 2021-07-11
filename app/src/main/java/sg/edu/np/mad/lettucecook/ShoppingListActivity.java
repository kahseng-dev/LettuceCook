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
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.w3c.dom.Text;

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

        RecyclerView recyclerView = findViewById(R.id.shoppingListRV);
        ImageView cartIcon = findViewById(R.id.shoppingCartIcon);
        TextView shoppingListText = findViewById(R.id.shoppingListText);

        LinearLayoutManager mLayoutManger = new LinearLayoutManager(this);

        if (getIntent().hasExtra("UserId")) {
            Bundle extras = getIntent().getExtras();
            int userId = extras.getInt("UserId");
            ingredientList = dbHandler.getShoppingList(userId);

            if (ingredientList.size() == 0) {
                recyclerView.setVisibility(View.INVISIBLE);
                cartIcon.setVisibility(View.VISIBLE);
                shoppingListText.setVisibility(View.VISIBLE);
                String emptyShoppingList = "Your shopping list is empty! Please add some!";
                shoppingListText.setText(emptyShoppingList);
            }

            else {
                recyclerView.setVisibility(View.VISIBLE);
                cartIcon.setVisibility(View.INVISIBLE);
                shoppingListText.setVisibility(View.INVISIBLE);
                ShoppingListAdapter sAdapter = new ShoppingListAdapter(ingredientList, this, userId);
                recyclerView.setLayoutManager(mLayoutManger);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(sAdapter);
            }
        }

        else {
            recyclerView.setVisibility(View.INVISIBLE);
            cartIcon.setVisibility(View.VISIBLE);
            shoppingListText.setVisibility(View.VISIBLE);
            String notLoggedInMessage = "Please Sign In to view your shopping items!";
            shoppingListText.setText(notLoggedInMessage);
        }

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
                        return true;
                }
                return false;
            }
        });
    }
}