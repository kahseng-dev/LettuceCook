package sg.edu.np.mad.lettucecook.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
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

import sg.edu.np.mad.lettucecook.R;
import sg.edu.np.mad.lettucecook.models.CreatedRecipe;
import sg.edu.np.mad.lettucecook.rv.AccountRecipesAdapter;

public class AccountRecipesActivity extends AppCompatActivity {
    ArrayList<CreatedRecipe> createdRecipeList = new ArrayList<>();
    ArrayList<CreatedRecipe> recipeList = new ArrayList<>();
    ArrayList<String> recipeIDList = new ArrayList<>();

    Button createRecipeButton;
    TextView noAccountRecipeText;

    private Toolbar toolbar;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_recipes);

        // setup toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_black_arrow_back);
        toolbar.findViewById(R.id.app_logo).setVisibility(View.INVISIBLE);

        // Find views ids and set to variables
        createRecipeButton = findViewById(R.id.create_recipe_button);
        noAccountRecipeText = findViewById(R.id.account_no_recipe);

        // findviewby recipe recycler view
        RecyclerView recipeRecyclerView = findViewById(R.id.account_recipe_rv);
        // create linear layout manager
        LinearLayoutManager mLayoutManger = new LinearLayoutManager(this);

        // get user that is logged in
        user = FirebaseAuth.getInstance().getCurrentUser();

        // check if user is not logged in
        if (user == null) {
            // bring them to login activity
            startActivity(new Intent(AccountRecipesActivity.this, LoginActivity.class));
            finish();
        }

        else {
            // get user data
            reference = FirebaseDatabase.getInstance().getReference("Users");
            userID = user.getUid();

            // Retrieve data from Firebase
            reference.child(userID).child("createdRecipesList").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    // Loop through each created recipes list in the snapshot
                    for(DataSnapshot dataSnapshot : snapshot.getChildren())
                    {
                        // add the created recipes' key into the recipeID list
                        recipeIDList.add(dataSnapshot.getKey());
                        // add created recipe object into created recipe list
                        CreatedRecipe createdRecipe = dataSnapshot.getValue(CreatedRecipe.class);
                        createdRecipeList.add(createdRecipe);
                    }

                    // Check if createdRecipeList is empty
                    if (createdRecipeList == null) {
                        Toast.makeText(AccountRecipesActivity.this, "You do not have any created recipes!", Toast.LENGTH_SHORT).show();
                        recipeRecyclerView.setVisibility(View.INVISIBLE); // Hide recyclerview if empty
                        noAccountRecipeText.setVisibility(View.VISIBLE); // Show no account recipe message if empty
                    } else {
                        recipeRecyclerView.setVisibility(View.VISIBLE); // Show recyclerview if not empty
                        noAccountRecipeText.setVisibility(View.INVISIBLE); // Hide no account recipe message if not empty

                        // Add created recipes to recycler view
                        AccountRecipesAdapter sAdapter = new AccountRecipesAdapter(createdRecipeList, AccountRecipesActivity.this, recipeIDList, userID);
                        recipeRecyclerView.setLayoutManager(mLayoutManger);
                        recipeRecyclerView.setItemAnimator(new DefaultItemAnimator());
                        recipeRecyclerView.setAdapter(sAdapter);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(AccountRecipesActivity.this, "Unable to retrieve information!", Toast.LENGTH_LONG).show();
                }
            });
        }

        // if create recipe button is clicked, bring user to create recipe activity
        createRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountRecipesActivity.this, CreateRecipeActivity.class);
                startActivity(intent);
            }
        });
    }

    // if the user clicks on the back button in the toolbar, bring user back to account activity.
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

    // if the user clicks on the back button in the bottom navigation buttons, bring user back to account activity.
    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), AccountActivity.class));
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}