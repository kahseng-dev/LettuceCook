package sg.edu.np.mad.lettucecook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = findViewById(R.id.textView);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RecipeDetailsActivity.class);
                if (getIntent().hasExtra("UserId")) {
                    Bundle extras = getIntent().getExtras();
                    int userId = extras.getInt("UserId");
                    intent.putExtra("UserId", userId);
                }
                startActivity(intent);
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.browse);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.browse:
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