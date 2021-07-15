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

        // if the user is logged in
        if (getIntent().hasExtra("UserId")) {
            Bundle extras = getIntent().getExtras();

            // get the user id
            int userId = extras.getInt("UserId");

            // get the list of ingredients of the user from the shopping list database.
            ingredientList = dbHandler.getShoppingList(userId);

            // if the user has no items,
            // display a message to ask the user to add some ingredients
            if (ingredientList.size() == 0) {
                recyclerView.setVisibility(View.INVISIBLE);
                cartIcon.setVisibility(View.VISIBLE);
                shoppingListText.setVisibility(View.VISIBLE);
                String emptyShoppingList = "Your shopping list is empty! Please add some!";
                shoppingListText.setText(emptyShoppingList);
            }

            else { // otherwise, if the user has an item
                recyclerView.setVisibility(View.VISIBLE);
                cartIcon.setVisibility(View.INVISIBLE);
                shoppingListText.setVisibility(View.INVISIBLE);

                // display the list of ingredients
                ShoppingListAdapter sAdapter = new ShoppingListAdapter(ingredientList, this, userId);
                recyclerView.setLayoutManager(mLayoutManger);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(sAdapter);
            }
        }

        else { // if the user is not logged in
            recyclerView.setVisibility(View.INVISIBLE);
            cartIcon.setVisibility(View.VISIBLE);
            shoppingListText.setVisibility(View.VISIBLE);

            // display the not logged in message.
            String notLoggedInMessage = "Please Sign In to view your shopping items!";
            shoppingListText.setText(notLoggedInMessage);
        }

        // navigation bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // set the activity that the user is currently viewing, which is the shopping list activity
        bottomNavigationView.setSelectedItemId(R.id.shoppingList);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {

                    // if the user wants to browse, bring them to main activity
                    case R.id.browse:
                        Intent browseIntent = new Intent(getApplicationContext(), MainActivity.class);

                        // if the user is logged in, pass the userId
                        if (getIntent().hasExtra("UserId")) {
                            Bundle extras = getIntent().getExtras();
                            int userId = extras.getInt("UserId");
                            browseIntent.putExtra("UserId", userId);
                        }

                        startActivity(browseIntent);
                        overridePendingTransition(0, 0);
                        return true;

                    // if the user wants to view his/her account, bring them to account activity
                    case R.id.account:
                        Intent accountIntent = new Intent(getApplicationContext(), AccountActivity.class);

                        // if the user is logged in, pass the userId to the activity
                        if (getIntent().hasExtra("UserId")) {
                            Bundle extras = getIntent().getExtras();
                            int userId = extras.getInt("UserId");
                            accountIntent.putExtra("UserId", userId);
                        }

                        startActivity(accountIntent);
                        overridePendingTransition(0, 0);
                        return true;

                    // if the user clicks on the shopping list button, do nothing.
                    case R.id.shoppingList:
                        return true;
                }
                return false;
            }
        });
    }
}