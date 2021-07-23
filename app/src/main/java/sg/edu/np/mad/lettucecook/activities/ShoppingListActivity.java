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
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import sg.edu.np.mad.lettucecook.R;
import sg.edu.np.mad.lettucecook.models.DBHandler;
import sg.edu.np.mad.lettucecook.models.Ingredient;
import sg.edu.np.mad.lettucecook.rv.ShoppingListAdapter;

public class ShoppingListActivity extends AppCompatActivity {
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    private DBHandler dbHandler = new DBHandler(this , null, null, 1);
    private ArrayList<Ingredient> ingredientList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        RecyclerView recyclerView = findViewById(R.id.shopping_list_rv);
        ImageView cartIcon = findViewById(R.id.shopping_list_cart_icon);
        TextView shoppingListText = findViewById(R.id.shopping_list_text);

        LinearLayoutManager mLayoutManger = new LinearLayoutManager(this);

        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) { // if the user is not logged in
            recyclerView.setVisibility(View.INVISIBLE);
            cartIcon.setVisibility(View.VISIBLE);
            shoppingListText.setVisibility(View.VISIBLE);

            // display the not logged in message.
            String notLoggedInMessage = "Please Sign In to view your shopping items!";
            shoppingListText.setText(notLoggedInMessage);
        }

        else {
            reference = FirebaseDatabase.getInstance().getReference("Users");
            userID = user.getUid();

            // get the list of ingredients of the user from the shopping list database.
            ingredientList = dbHandler.getShoppingList(userID);

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
                ShoppingListAdapter sAdapter = new ShoppingListAdapter(ingredientList, this, userID);
                recyclerView.setLayoutManager(mLayoutManger);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(sAdapter);
            }

        }

        // navigation bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // set the activity that the user is currently viewing, which is the shopping list activity
        bottomNavigationView.setSelectedItemId(R.id.shoppingList);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.browse:
                        // bring user to main activity
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.account:
                        // bring user to account activity
                        startActivity(new Intent(getApplicationContext(), AccountActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.create_recipe:
                        // bring user to create recipe activity
                        startActivity(new Intent(getApplicationContext(), CreateRecipeActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.shoppingList: // if the user clicks on the shopping list button, do nothing.
                        return true;
                }
                return false;
            }
        });
    }
}