package sg.edu.np.mad.lettucecook.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import sg.edu.np.mad.lettucecook.R;
import sg.edu.np.mad.lettucecook.models.DBHandler;
import sg.edu.np.mad.lettucecook.models.Ingredient;
import sg.edu.np.mad.lettucecook.models.User;
import sg.edu.np.mad.lettucecook.rv.ShoppingListAdapter;

public class ShoppingListActivity extends AppCompatActivity {
    private RecyclerView shoppingListRV;
    private ImageView cartIcon;
    private TextView shoppingListText;
    private Switch offlineStateSwitch;

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    private DBHandler dbHandler = new DBHandler(this, null, null, 1);
    private ArrayList<Ingredient> ingredientList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        user = FirebaseAuth.getInstance().getCurrentUser();

        shoppingListRV = (RecyclerView) findViewById(R.id.shopping_list_rv);
        cartIcon = (ImageView) findViewById(R.id.shopping_list_cart_icon);
        shoppingListText = (TextView) findViewById(R.id.shopping_list_text);

        LinearLayoutManager mLayoutManger = new LinearLayoutManager(this);

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences offlineStatePreferences = getSharedPreferences("offlineState", MODE_PRIVATE);
        boolean switchValue = offlineStatePreferences.getBoolean("switchValue", false);

        if (user == null) { // if the user is not logged in
            if (switchValue) {
                String userId = offlineStatePreferences.getString("userID", null);

                if (userId == null) {
                    shoppingListRV.setVisibility(View.INVISIBLE);
                    cartIcon.setVisibility(View.VISIBLE);
                    shoppingListText.setVisibility(View.VISIBLE);

                    // display the not logged in message.
                    String notLoggedInMessage = "Please Sign In to view your shopping items!";
                }

                else {
                    shoppingListRV.setVisibility(View.VISIBLE);
                    cartIcon.setVisibility(View.INVISIBLE);
                    shoppingListText.setVisibility(View.INVISIBLE);

                    ingredientList = dbHandler.getShoppingList(userId);

                    ShoppingListAdapter sAdapter = new ShoppingListAdapter(ingredientList, this, userId);
                    shoppingListRV.setLayoutManager(mLayoutManger);
                    shoppingListRV.setItemAnimator(new DefaultItemAnimator());
                    shoppingListRV.setAdapter(sAdapter);

                    if (ingredientList.isEmpty()) {
                        shoppingListRV.setVisibility(View.INVISIBLE);
                        cartIcon.setVisibility(View.VISIBLE);
                        shoppingListText.setVisibility(View.VISIBLE);

                        // display the not logged in message.
                        String emptyShoppingList = "Your shopping list is empty!";
                        shoppingListText.setText(emptyShoppingList);
                    }
                }
            }

            else {
                shoppingListRV.setVisibility(View.INVISIBLE);
                cartIcon.setVisibility(View.VISIBLE);
                shoppingListText.setVisibility(View.VISIBLE);

                // display the not logged in message.
                String notLoggedInMessage = "Please Sign In to view your shopping items!";
                shoppingListText.setText(notLoggedInMessage);
            }
        }

        else {
            reference = FirebaseDatabase.getInstance().getReference("Users");
            userID = user.getUid();

            // get the list of ingredients of the user from the shopping list database.
            ingredientList = dbHandler.getShoppingList(userID);

            // if the user has no items,
            // display a message to ask the user to add some ingredients
            if (ingredientList.size() == 0) {
                shoppingListRV.setVisibility(View.INVISIBLE);
                cartIcon.setVisibility(View.VISIBLE);
                shoppingListText.setVisibility(View.VISIBLE);
                String emptyShoppingList = "Your shopping list is empty! Please add some!";
                shoppingListText.setText(emptyShoppingList);
            }

            else { // otherwise, if the user has an item
                shoppingListRV.setVisibility(View.VISIBLE);
                cartIcon.setVisibility(View.INVISIBLE);
                shoppingListText.setVisibility(View.INVISIBLE);

                // display the list of ingredients
                ShoppingListAdapter sAdapter = new ShoppingListAdapter(ingredientList, this, userID);
                shoppingListRV.setLayoutManager(mLayoutManger);
                shoppingListRV.setItemAnimator(new DefaultItemAnimator());
                shoppingListRV.setAdapter(sAdapter);
            }

        }

        // navigation bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // set the activity that the user is currently viewing, which is the shopping list activity
        bottomNavigationView.setSelectedItemId(R.id.shoppingList);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_shoppinglist_toolbar, menu);
        return true;
    }

    // if the user clicks on the back button in the toolbar, bring them back to main activity.
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                return true;

            case R.id.setting:
                if (user == null) Toast.makeText(ShoppingListActivity.this, "Please Login to use this feature", Toast.LENGTH_SHORT).show();

                else {
                    final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                            ShoppingListActivity.this, R.style.BottomSheetDialogTheme
                    );

                    View bottomSheetView = LayoutInflater.from(getApplicationContext())
                            .inflate(
                                    R.layout.shopping_list_bottom_sheet,
                                    (LinearLayout) findViewById(R.id.shopping_list_settings_container)
                            );

                    SharedPreferences offlineStatePreferences = getSharedPreferences("offlineState", MODE_PRIVATE);
                    boolean switchValue = offlineStatePreferences.getBoolean("switchValue", false);

                    offlineStateSwitch = (Switch) bottomSheetView.findViewById(R.id.offline_state_switch);

                    if (userID != offlineStatePreferences.getString("userID", null)) {
                        SharedPreferences.Editor editor = getSharedPreferences("offlineState", MODE_PRIVATE).edit();
                        editor.putBoolean("switchValue", false);
                        editor.putString("userID", userID);
                        editor.apply();
                        offlineStateSwitch.setChecked(false);
                    }

                    else {
                        offlineStateSwitch.setChecked(switchValue);
                    }

                    bottomSheetView.findViewById(R.id.offline_state_switch).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (offlineStateSwitch.isChecked()) {
                                SharedPreferences.Editor editor = getSharedPreferences("offlineState", MODE_PRIVATE).edit();
                                editor.putBoolean("switchValue", true);
                                editor.putString("userID", userID);
                                editor.apply();
                                offlineStateSwitch.setChecked(true);
                            }

                            else {
                                SharedPreferences.Editor editor = getSharedPreferences("offlineState", MODE_PRIVATE).edit();
                                editor.putBoolean("switchValue", false);
                                editor.apply();
                                offlineStateSwitch.setChecked(false);
                            }
                        }
                    });

                    bottomSheetView.findViewById(R.id.shopping_list_backup).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            reference = FirebaseDatabase.getInstance().getReference("Users");

                            userID = user.getUid();

                            ingredientList = dbHandler.getShoppingList(userID);

                            if (ingredientList.isEmpty()) {
                                Toast.makeText(ShoppingListActivity.this, "There is nothing to backup", Toast.LENGTH_SHORT).show();
                            } else {
                                reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .child("shoppingList").setValue(ingredientList)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(ShoppingListActivity.this, "Backup Successfully!", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(ShoppingListActivity.this, "Failed to Backup!\n" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                            }
                        }
                    });

                    bottomSheetView.findViewById(R.id.shopping_list_restore).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            reference = FirebaseDatabase.getInstance().getReference("Users");
                            userID = user.getUid();

                            reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    User userProfile = snapshot.getValue(User.class);

                                    if (userProfile != null) {
                                        if (userProfile.shoppingList == null) {
                                            Toast.makeText(ShoppingListActivity.this, "Could not find previous backup", Toast.LENGTH_LONG).show();
                                        } else {
                                            ingredientList = userProfile.shoppingList;

                                            if (dbHandler.getShoppingList(userID).size() != 0) {
                                                dbHandler.clearUserShoppingList(userID);
                                            }

                                            for (int i = 0; i < ingredientList.size(); i++) {
                                                dbHandler.addItemToShoppingList(userID, ingredientList.get(i));
                                            }

                                            Toast.makeText(ShoppingListActivity.this, "Successfully Restored", Toast.LENGTH_LONG).show();
                                            startActivity(getIntent());
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(ShoppingListActivity.this, "Failed to restore", Toast.LENGTH_LONG).show();
                                }
                            });
                        }

                    });

                    bottomSheetView.findViewById(R.id.shopping_list_clear_all).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dbHandler.clearUserShoppingList(userID);
                            startActivity(getIntent());
                        }
                    });

                    bottomSheetDialog.setContentView(bottomSheetView);
                    bottomSheetDialog.show();
                }

                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}