package sg.edu.np.mad.lettucecook.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import sg.edu.np.mad.lettucecook.R;
import sg.edu.np.mad.lettucecook.models.User;

public class AccountActivity extends AppCompatActivity {
    Context mContext = this;

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    private LinearLayout favourites, recipes, logout;
    private TextView greeting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        // get current user that is logged in
        user = FirebaseAuth.getInstance().getCurrentUser();

        // if user is not logged in bring them to login activity
        if (user == null) {
            startActivity(new Intent(mContext, LoginActivity.class));
            finish();
        }

        else {
            // get User data
            reference = FirebaseDatabase.getInstance().getReference("Users");
            userID = user.getUid();

            // set ViewIds
            greeting = (TextView) findViewById(R.id.account_greeting);
            favourites = (LinearLayout) findViewById(R.id.account_favourites);
            recipes = (LinearLayout) findViewById(R.id.account_recipes);
            logout = (LinearLayout) findViewById(R.id.account_logout);

            reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // convert user snapshot to user class
                    User userProfile = snapshot.getValue(User.class);

                    // if user is in database
                    if (userProfile != null) {
                        String username = userProfile.username;
                        String greetingMessage = "Hello " + username + "!";
                        greeting.setText(greetingMessage);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(mContext, "Oops! something went wrong", Toast.LENGTH_LONG).show();
                }
            });

            // if user clicks on view favourites section bring them to the account favourites activity
            favourites.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent (mContext, AccountFavouritesActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            });

            // if the user clicks on the recipes section in account activity bring them to the account recipes activity
            recipes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent (mContext, AccountRecipesActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            });

            // if the user clicks on the log out section in account activity
            // logout from firebase and bring them to the login activity
            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent (mContext, LoginActivity.class));
                }
            });

            // Navigation Bar
            BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
            bottomNavigationView.setSelectedItemId(R.id.account); // set which activity the user is currently viewing which is the account activity
            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.browse:
                            // bring user to main activity
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            overridePendingTransition(0, 0);
                            return true;

                        case R.id.account:
                            // if the user clicks on account which is this activity, do nothing
                            return true;

                        case R.id.create_recipe:
                            // bring user to CreateRecipeActivity
                            startActivity(new Intent(getApplicationContext(), CreateRecipeActivity.class));
                            overridePendingTransition(0, 0);
                            return true;

                        // if the user clicks on shopping list
                        case R.id.shoppingList:
                            // bring user to shopping activity
                            startActivity(new Intent(getApplicationContext(), ShoppingListActivity.class));
                            overridePendingTransition(0, 0);
                            return true;
                    }
                    return false;
                }
            });
        }
    }
}