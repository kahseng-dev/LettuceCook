package sg.edu.np.mad.lettucecook.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import sg.edu.np.mad.lettucecook.R;
import sg.edu.np.mad.lettucecook.models.DBHandler;
import sg.edu.np.mad.lettucecook.models.User;

public class LoginActivity extends AppCompatActivity {
    DBHandler dbHandler = new DBHandler(this , null, null, 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText username = findViewById(R.id.login_username);
        EditText password = findViewById(R.id.login_password);
        password.setTypeface(Typeface.DEFAULT);

        Button loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidCredentials(username.getText().toString(), password.getText().toString())) {
                    User dbUser = dbHandler.findUser(username.getText().toString());

                    // Store Logged in User ID
                    Intent intent = new Intent (LoginActivity.this, MainActivity.class);
                    intent.putExtra("UserId", dbUser.getUserId());
                    startActivity(intent);
                    Toast.makeText(LoginActivity.this, "Successful Login!", Toast.LENGTH_SHORT).show();
                }

                else {
                    Toast.makeText(LoginActivity.this, "Invalid username or password.\nPlease try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        TextView createAccount = findViewById(R.id.login_create_account_link);
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, CreateAccountActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.account);
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

                    case R.id.shoppingList:
                        Intent shoppingListIntent = new Intent(getApplicationContext(), ShoppingListActivity.class);

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

    public boolean isValidCredentials(String username, String password) {
        User dbUser = dbHandler.findUser(username);
        if (dbUser != null) {
            if (dbUser.getUsername().equals(username) && dbUser.getPassword().equals(password)) {
                return true;
            }
        }

        return false;
    }
}