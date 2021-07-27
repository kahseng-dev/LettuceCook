package sg.edu.np.mad.lettucecook.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import sg.edu.np.mad.lettucecook.models.CreatedRecipe;
import sg.edu.np.mad.lettucecook.models.NinjaIngredient;
import sg.edu.np.mad.lettucecook.rv.NinjaIngredientAdapter;
import sg.edu.np.mad.lettucecook.R;
import sg.edu.np.mad.lettucecook.utils.ApiJsonSingleton;
import sg.edu.np.mad.lettucecook.utils.ApiService;
import sg.edu.np.mad.lettucecook.utils.ApiURL;
import sg.edu.np.mad.lettucecook.utils.IngredientClickListener;
import sg.edu.np.mad.lettucecook.utils.VolleyResponseListener;

public class CustomRecipeActivity extends AppCompatActivity {
    Context mContext = CustomRecipeActivity.this;
    ApiJsonSingleton apiJson = ApiJsonSingleton.getInstance();
    ApiService apiService = new ApiService(mContext);

    TextView createdRecipeName, createdRecipeArea, createdRecipeInstructions, createdRecipeCategory;
    ImageView createdRecipeImage;
    ToggleButton publishStateButton;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID, recipeID;
    private Toolbar toolbar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_recipes);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_black_arrow_back);
        toolbar.findViewById(R.id.app_logo).setVisibility(View.INVISIBLE);

        // Setting publish button
        publishStateButton = (ToggleButton) findViewById(R.id.publish_recipe);

        // Find createdRecipe texts to display info
        createdRecipeName = findViewById(R.id.custom_recipe_name);
        createdRecipeArea = findViewById(R.id.custom_recipe_area);
        createdRecipeInstructions = findViewById(R.id.custom_recipe_instructions);
        createdRecipeCategory = findViewById(R.id.custom_recipe_category);
        createdRecipeImage = findViewById(R.id.custom_recipe_image);

        // Get created recipe
        CreatedRecipe createdRecipe = (CreatedRecipe) getIntent().getSerializableExtra("Recipe");

        // Set recipe information to createdRecipe texts -- CHANGE
        createdRecipeName.setText(createdRecipe.recipeName);
        createdRecipeArea.setText(createdRecipe.recipeArea);
        createdRecipeCategory.setText(createdRecipe.recipeCategory);
        createdRecipeInstructions.setText(createdRecipe.recipeInstructions);
        Picasso.with(CustomRecipeActivity.this).load(createdRecipe.recipeImageURL).into(createdRecipeImage); // Add recipe image

        String ninjaQuery = apiJson.createNinjaQuery(createdRecipe.getIngredientList());
        
        apiService.getIngredient(ApiURL.CalorieNinjas, ninjaQuery, new VolleyResponseListener() {

            @Override
            public void onError(String message) {
                Toast.makeText(mContext, "Error retrieving ingredient info", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(JSONObject response) throws JSONException {
                ArrayList<NinjaIngredient> ninjaIngredients = apiJson.parseNinjaIngredients(
                        response.getJSONArray("items"), createdRecipe.getIngredientList()
                );
                for (NinjaIngredient i: ninjaIngredients) {
                    Log.v("Meal", String.valueOf(i.getCalories()));
                }
                NinjaIngredientAdapter adapter = new NinjaIngredientAdapter(ninjaIngredients, mContext, new IngredientClickListener() {
                    @Override
                    public void onItemClick(NinjaIngredient ingredient) {
                        Intent intent = new Intent(getApplicationContext(), IngredientPopup.class);
                        intent.putExtra("ingredient", ingredient); // Send NinjaIngredient object to IngredientPopup
                        startActivity(intent);
//                            overridePendingTransition(R.anim.slide_in_up, 0);
                    }
                });
                RecyclerView ingredientsRV = findViewById(R.id.custom_recipe_ingredients_rv);
                LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext);

                // Setting ingredients recycler view
                ingredientsRV.setLayoutManager(mLayoutManager);
                ingredientsRV.setItemAnimator(new DefaultItemAnimator());
                ingredientsRV.setAdapter(adapter);
            }
        });

        // Get userID from Firebase
        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            reference = FirebaseDatabase.getInstance().getReference("Users");
            userID = user.getUid();

            recipeID = getIntent().getExtras().getString("recipeId");
            if (recipeID != null) {
                reference.child(userID).child("createdRecipesList").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild(recipeID)) {
                            publishStateButton.setVisibility(View.VISIBLE);

                            publishStateButton.setChecked(createdRecipe.publishState);
                            publishStateButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick (View v){
                                    if (publishStateButton.isChecked()) createdRecipe.publishState = true;
                                    else createdRecipe.publishState = false;
                                    reference.child(userID).child("createdRecipesList").child(recipeID).child("publishState").setValue(createdRecipe.publishState);
                                }
                            });
                        }
                    }

                    // Validation in case cancelled
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                });
            }
        }
    }

    // if the user clicks on the back button in the toolbar, bring them back to login activity.
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (getIntent().hasExtra("userID")) startActivity(new Intent(getApplicationContext(), AccountRecipesActivity.class));
                else startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (getIntent().hasExtra("userID")) startActivity(new Intent(getApplicationContext(), AccountRecipesActivity.class));
        else startActivity(new Intent(getApplicationContext(), MainActivity.class));
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}