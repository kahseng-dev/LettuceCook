package sg.edu.np.mad.lettucecook.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sg.edu.np.mad.lettucecook.R;
import sg.edu.np.mad.lettucecook.models.CreatedIngredient;
import sg.edu.np.mad.lettucecook.models.CreatedRecipe;
import sg.edu.np.mad.lettucecook.models.DBHandler;
import sg.edu.np.mad.lettucecook.models.User;
import sg.edu.np.mad.lettucecook.rv.AccountRecipesAdapter;

public class AccountRecipesActivity extends AppCompatActivity {
    ArrayList<CreatedRecipe> createdRecipeList = new ArrayList<>();
    ArrayList<CreatedRecipe> recipeList = new ArrayList<>();
    Button createRecipeButton;
    TextView noAccountRecipeText;
    String addRecipeName, addRecipeArea, addRecipeCategory, addRecipeInstructions;

    private Toolbar toolbar;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID, recipeID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_recipes);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_black_arrow_back);
        toolbar.findViewById(R.id.app_logo).setVisibility(View.INVISIBLE);

        // Find views ids and set to variables
        createRecipeButton = findViewById(R.id.create_recipe_button);
        noAccountRecipeText = findViewById(R.id.account_no_recipe);

        // Call recyclerview
        RecyclerView recipeRecyclerView = findViewById(R.id.account_recipe_rv);
        LinearLayoutManager mLayoutManger = new LinearLayoutManager(this);

        user = FirebaseAuth.getInstance().getCurrentUser();

        // If user is not logged in
        if (user == null) {
            startActivity(new Intent(AccountRecipesActivity.this, LoginActivity.class));
            finish();
        }

        else {
            reference = FirebaseDatabase.getInstance().getReference("Users");
            userID = user.getUid();

            // Retrieve data from Firebase
            reference.child(userID).child("createdRecipesList").addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // HashMap to child
                    Map<String, CreatedRecipe> td = (HashMap<String, CreatedRecipe>) snapshot.getValue();

                    ArrayList<CreatedRecipe> values = new ArrayList<CreatedRecipe>(td.values());

                    // Loop through each createdRecipesList children and append to createdRecipeList
                    for(DataSnapshot dataSnapshot : snapshot.getChildren())
                    {
                        CreatedRecipe createdRecipe = dataSnapshot.getValue(CreatedRecipe.class);
                        createdRecipeList.add(createdRecipe);
                    }

                    // Check if createdRecipeList is empty
                    if (createdRecipeList == null) {
                        Toast.makeText(AccountRecipesActivity.this, "You do not have any created recipes!", Toast.LENGTH_SHORT).show();
                        recipeRecyclerView.setVisibility(View.INVISIBLE); // Hide recyclerview if empty
                        noAccountRecipeText.setVisibility(View.VISIBLE); // Show no account recipe message if empty
                    }

                    else {
                        recipeRecyclerView.setVisibility(View.VISIBLE); // Show recyclerview if not empty
                        noAccountRecipeText.setVisibility(View.INVISIBLE); // Hide no account recipe message if not empty

                        // Add customized created recipes to recycler view
                        AccountRecipesAdapter sAdapter = new AccountRecipesAdapter(createdRecipeList, AccountRecipesActivity.this, userID);
                        recipeRecyclerView.setLayoutManager(mLayoutManger);
                        recipeRecyclerView.setItemAnimator(new DefaultItemAnimator());
                        recipeRecyclerView.setAdapter(sAdapter);
                    }
                }

                // Validation in case cancelled
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(AccountRecipesActivity.this, "Unable to retrieve information!", Toast.LENGTH_LONG).show();
                }
            });
        }

        // Create Recipe Button onClick
        createRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountRecipesActivity.this, CreateRecipeActivity.class);
                startActivity(intent);
            }
        });
    }

    // if the user clicks on the back button in the toolbar, bring them back to login activity.
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(getApplicationContext(), AccountActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), AccountActivity.class));
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}