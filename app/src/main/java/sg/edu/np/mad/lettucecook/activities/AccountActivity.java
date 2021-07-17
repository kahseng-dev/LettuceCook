package sg.edu.np.mad.lettucecook.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import sg.edu.np.mad.lettucecook.R;
import sg.edu.np.mad.lettucecook.models.DBHandler;
import sg.edu.np.mad.lettucecook.models.User;

public class AccountActivity extends AppCompatActivity {
    DBHandler dbHandler = new DBHandler(this , null, null, 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        // if the user is not logged in, bring them to the login activity.
        if (!(getIntent().hasExtra("UserId"))) {
            Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
            startActivity(intent);
        }

        else { // else if the user is logged in
            TextView greeting = findViewById(R.id.account_greeting);
            TextView logout = findViewById(R.id.account_logout);

            setTitle("LettuceCook Account");

            Bundle extras = getIntent().getExtras();

            int userId = extras.getInt("UserId"); // getting the userId that is logged in
            User user = dbHandler.getDetails(userId); // get the details of the user that is logged in
            greeting.setText("Hello " + user.getUsername() + "!"); // setting welcome message in account activity.

            // if the user clicks on the log out section in the account activity
            // bring them to the login activity without parsing the userId
            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent (AccountActivity.this, LoginActivity.class);
                    startActivity(intent);

                    // create a toast message to say goodbye to user
                    Toast.makeText(AccountActivity.this, "Goodbye " + user.getUsername() + "!", Toast.LENGTH_SHORT).show();
                }
            });

            // navigation bar
            BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

            // set which activity the user is currently viewing which is the account activity
            bottomNavigationView.setSelectedItemId(R.id.account);
            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch(item.getItemId()) {

                        // if the user clicks on browse
                        case R.id.browse:

                            // bring user to main activity
                            Intent browseIntent = new Intent(getApplicationContext(), MainActivity.class);

                            // pass the userId as well
                            if (getIntent().hasExtra("UserId")) {
                                Bundle extras = getIntent().getExtras();
                                int userId = extras.getInt("UserId");
                                browseIntent.putExtra("UserId", userId);
                            }

                            startActivity(browseIntent);
                            overridePendingTransition(0, 0);
                            return true;

                        // if the user clicks on account which is this activity, do nothing
                        case R.id.account:
                            return true;

                        // if the user clicks on create recipe
                        case R.id.create_recipe:

                            // bring user to create recipe activity
                            Intent createRecipeIntent = new Intent(getApplicationContext(), CreateRecipeActivity.class);

                            // pass the userId as well
                            if (getIntent().hasExtra("UserId")) {
                                Bundle extras = getIntent().getExtras();
                                int userId = extras.getInt("UserId");
                                createRecipeIntent.putExtra("UserId", userId);
                            }

                            startActivity(createRecipeIntent);
                            overridePendingTransition(0, 0);
                            return true;

                        // if the user clicks on shopping list
                        case R.id.shoppingList:

                            // bring user to shopping activity
                            Intent shoppingListIntent = new Intent(getApplicationContext(), ShoppingListActivity.class);

                            // pass the userId as well
                            if (getIntent().hasExtra("UserId")) {
                                Bundle extras = getIntent().getExtras();
                                int userId = extras.getInt("UserId");
                                shoppingListIntent.putExtra("UserId", userId);
                            }

                            startActivity(shoppingListIntent);
                            overridePendingTransition(0, 0);
                            return true;
                    }
                    return false;
                }
            });
        }
    }
}