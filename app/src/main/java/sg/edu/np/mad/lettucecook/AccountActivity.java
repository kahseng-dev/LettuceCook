package sg.edu.np.mad.lettucecook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import sg.edu.np.mad.lettucecook.Models.DBHandler;
import sg.edu.np.mad.lettucecook.Models.User;

public class AccountActivity extends AppCompatActivity {
    DBHandler dbHandler = new DBHandler(this , null, null, 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        if (!(getIntent().hasExtra("UserId"))) {
            Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
            startActivity(intent);
        }

        else {
            TextView greeting = findViewById(R.id.greeting);
            TextView logout = findViewById(R.id.logout);

            setTitle("LettuceCook Account");

            Bundle extras = getIntent().getExtras();
            int userId = extras.getInt("UserId");
            User user = dbHandler.getDetails(userId);
            greeting.setText("Hello " + user.getUsername() + "!");

            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent (AccountActivity.this, LoginActivity.class);
                    startActivity(intent);
                    Toast.makeText(AccountActivity.this, "Goodbye " + user.getUsername() + "!", Toast.LENGTH_SHORT).show();
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
    }
}