package sg.edu.np.mad.lettucecook.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    private TextView greeting;
    private LinearLayout logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            startActivity(new Intent(AccountActivity.this, LoginActivity.class));
            finish();
        }

        else {
            reference = FirebaseDatabase.getInstance().getReference("Users");
            userID = user.getUid();

            greeting = (TextView) findViewById(R.id.account_greeting);
            logout = (LinearLayout) findViewById(R.id.account_logout);

            reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User userProfile = snapshot.getValue(User.class);

                    if (userProfile != null) {
                        String username = userProfile.username;
                        String greetingMessage = "Hello " + username + "!";
                        greeting.setText(greetingMessage);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(AccountActivity.this, "Oops! something went wrong", Toast.LENGTH_LONG).show();
                }
            });

            // if the user clicks on the log out section in the account activity
            // bring them to the login activity without parsing the userId
            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent (AccountActivity.this, LoginActivity.class));
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
                            // if the user clicks on create recipe, bring user to create recipe activity
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