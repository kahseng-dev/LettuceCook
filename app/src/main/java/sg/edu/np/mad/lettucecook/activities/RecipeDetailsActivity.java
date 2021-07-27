package sg.edu.np.mad.lettucecook.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Vector;

import sg.edu.np.mad.lettucecook.R;
import sg.edu.np.mad.lettucecook.models.NinjaIngredient;
import sg.edu.np.mad.lettucecook.utils.DataSingleton;
import sg.edu.np.mad.lettucecook.utils.IngredientClickListener;
import sg.edu.np.mad.lettucecook.utils.VolleyResponseListener;
import sg.edu.np.mad.lettucecook.models.ApiMeal;
import sg.edu.np.mad.lettucecook.models.DBHandler;
import sg.edu.np.mad.lettucecook.models.Ingredient;
import sg.edu.np.mad.lettucecook.rv.YoutubeAdapter;
import sg.edu.np.mad.lettucecook.rv.YoutubeVideo;
import sg.edu.np.mad.lettucecook.rv.NinjaIngredientAdapter;
import sg.edu.np.mad.lettucecook.utils.ApiJsonSingleton;
import sg.edu.np.mad.lettucecook.utils.ApiService;
import sg.edu.np.mad.lettucecook.utils.ApiURL;

public class RecipeDetailsActivity extends AppCompatActivity {
    Context mContext = this;
    ApiMeal meal;
    ApiService apiService = new ApiService(this);
    ApiJsonSingleton apiJson = ApiJsonSingleton.getInstance();
    DataSingleton dataSingleton = DataSingleton.getInstance();

    private DBHandler dbHandler = new DBHandler(this , null, null, 1);
    private ImageView mealThumbnail, addToFavourites;
    private TextView mealName, mealCategory, areaText, instructionsText, dateModifiedText;
    private RecyclerView ytRecyclerView;
    private ArrayList<NinjaIngredient> ninjaIngredients;
    private NinjaIngredientAdapter ingredientAdapter;
    private Button addToShoppingList, sourceLinkButton;
    private Vector<YoutubeVideo> youtubeVideos = new Vector<>();
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID, isFavouriteId;
    private boolean isFavourite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v("Meal Recipe", "create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_white_arrow_back);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.findViewById(R.id.app_logo).setVisibility(View.INVISIBLE);

        // Setting ViewById for Meal Details
        mealThumbnail = findViewById(R.id.recipe_details_meal_thumbnail);
        mealName = findViewById(R.id.recipe_details_meal_name);
        mealCategory = findViewById(R.id.recipe_details_meal_category_text);
        areaText = findViewById(R.id.recipe_details_area_text);
        instructionsText = findViewById(R.id.recipe_details_instruction_text);
        dateModifiedText = findViewById(R.id.recipe_details_date_modified_text);

        // Setting ingredients recycler view
        RecyclerView ingredientsRV = findViewById(R.id.recipe_details_ingredients_rv);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        ingredientAdapter = new NinjaIngredientAdapter(ninjaIngredients, mContext, new IngredientClickListener() {
            @Override
            public void onItemClick(NinjaIngredient ingredient) {
                Intent intent = new Intent(getApplicationContext(), IngredientPopup.class);
                intent.putExtra("ingredient", ingredient); // Send NinjaIngredient object to IngredientPopup
                startActivity(intent);
            }
        });

        ingredientsRV.setLayoutManager(mLayoutManager);
        ingredientsRV.setItemAnimator(new DefaultItemAnimator());
        ingredientsRV.setAdapter(ingredientAdapter);

        // Setting ViewById and attributes for YouTube recyclerView
        ytRecyclerView = findViewById(R.id.recipe_details_youtube_rv);
        ytRecyclerView.setHasFixedSize(true);
        ytRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Setting ViewById for buttons
        addToShoppingList = findViewById(R.id.recipe_details_add_to_shopping_list_button);
        sourceLinkButton = findViewById(R.id.recipe_details_source_button);

        // Setting ViewById for favourites
        addToFavourites = findViewById(R.id.add_to_favourites);

        user = FirebaseAuth.getInstance().getCurrentUser();

        meal = dataSingleton.getMeal();
        if (meal != null) {
            Log.v("Meall", meal.getStrCategory());
            setMealDetails(meal);
        } else {
            // get the mealId to be viewed
            String mealId = getIntent().getStringExtra("mealId");
            apiService.get(ApiURL.MealDB, "lookup.php?i=" + mealId, new VolleyResponseListener() {
                @Override
                public void onError(String message) { }

                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray _meals = response.getJSONArray("meals");
                        meal = apiJson.mergeIntoJSONArray(_meals).get(0);
                        dataSingleton.setMeal(meal);
                        setMealDetails(meal);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private void setMealDetails(ApiMeal meal) {
        // Set meal background image
        Picasso
                .with(mContext)
                .load(meal.getStrMealThumb())
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        mealThumbnail.setBackground(new BitmapDrawable(getResources(), bitmap));
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) { }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) { }
                });

        // Setting meal details
        mealName.setText(meal.getStrMeal());
        mealCategory.setText(meal.getStrCategory());
        areaText.setText(meal.getStrArea());
        instructionsText.setText(meal.getStrInstructions());

        if (!(meal.getDateModified() == null)) {
            dateModifiedText.setText(meal.getDateModified());
        }

        String[] mealIngredients = meal.getArrIngredients();
        String[] mealMeasures = meal.getArrMeasures();
        String ninjaQuery = "";
        for (int i = 0; i < mealIngredients.length; i++) {
            ninjaQuery += mealMeasures[i] + " " + mealIngredients[i] + ", ";
        }
        // Remove the trailing ", "
        ninjaQuery = ninjaQuery.substring(0, ninjaQuery.length() - 2);

        apiService.getIngredient(ApiURL.CalorieNinjas, ninjaQuery, new VolleyResponseListener() {

            @Override
            public void onError(String message) {
                Toast.makeText(mContext, "Error retrieving ingredient info", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(JSONObject response) throws JSONException {
                ninjaIngredients = apiJson.parseNinjaIngredients(response.getJSONArray("items"), mealIngredients, mealMeasures);
                ingredientAdapter.setData(ninjaIngredients);
                ingredientAdapter.notifyDataSetChanged();
            }
        });

        // embedding youtube videos
        youtubeVideos.add(new YoutubeVideo(meal.getStrYoutube()));
        YoutubeAdapter videoAdapter = new YoutubeAdapter(youtubeVideos);
        ytRecyclerView.setAdapter(videoAdapter);

        if (user != null) {
            reference = FirebaseDatabase.getInstance().getReference("Users");
            userID = user.getUid();

            addToShoppingList.setEnabled(true);
            findViewById(R.id.add_to_shopping_list_message).setVisibility(View.INVISIBLE);

            addToFavourites.setVisibility(View.VISIBLE);

            addToShoppingList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String[] ingredients = meal.getArrIngredients();
                    String[] measures = meal.getArrMeasures();
                    for (int i = 0; i < ingredients.length; i++) {
                        Ingredient ingredient = new Ingredient(
                                Integer.parseInt(meal.getIdMeal()), ingredients[i], measures[i]);
                        dbHandler.addItemToShoppingList(userID, ingredient);
                    }
                    Toast.makeText(mContext, "Ingredients has been added\nto your shopping list", Toast.LENGTH_LONG).show();
                }
            });

            setFavouriteButtonColor(meal);
            addToFavourites.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    reference.child(userID).child("favouritesList").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            isFavourite = false;

                            for (DataSnapshot recipeSnapshot : snapshot.getChildren()) {
                                if (recipeSnapshot.getValue().toString().equals(meal.getIdMeal())) {
                                    isFavourite = true;
                                    isFavouriteId = recipeSnapshot.getKey();
                                    break;
                                }
                            }

                            if (isFavourite) {
                                reference.child(userID).child("favouritesList").child(isFavouriteId).removeValue();
                                Toast.makeText(RecipeDetailsActivity.this, "Removed from Favourites", Toast.LENGTH_SHORT).show();
                                isFavourite = false;
                                addToFavourites.setColorFilter(Color.argb(255, 255, 255, 255));
                            }

                            else {
                                reference.child(userID).child("favouritesList").push().setValue(meal.getIdMeal())
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(RecipeDetailsActivity.this, "Added to Favourites", Toast.LENGTH_SHORT).show();
                                                    isFavourite = true;
                                                    addToFavourites.setColorFilter(Color.argb(255, 253, 62, 129));
                                                }

                                                else {
                                                    Toast.makeText(RecipeDetailsActivity.this, "Failed to add to favourites", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(RecipeDetailsActivity.this, "Could not retrieve favourites information", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
        }
    }

    private void setFavouriteButtonColor(ApiMeal meal) {
        reference.child(userID).child("favouritesList").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                isFavourite = false;

                for (DataSnapshot recipeSnapshot : snapshot.getChildren()) {
                    isFavourite = recipeSnapshot.getValue().toString().equals(meal.getIdMeal());
                    isFavouriteId = recipeSnapshot.getKey();

                    if (isFavourite) {
                        addToFavourites.setColorFilter(Color.argb(255, 253, 62, 129));
                        break;
                    }

                    else addToFavourites.setColorFilter(Color.argb(255, 255, 255, 255));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(RecipeDetailsActivity.this, "Could not retrieve favourites information", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_notifications_toolbar, menu);
        menu.findItem(R.id.notification).setIcon(R.drawable.ic_notifications_white);
        return true;
    }

    // if the user clicks on the back button in the toolbar, bring them back to main activity.
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent browseIntent = new Intent(getApplicationContext(), MainActivity.class);

                if (getIntent().hasExtra("query")) {
                    browseIntent = new Intent(getApplicationContext(), BrowseActivity.class);
                    Bundle extras = getIntent().getExtras();
                    String query = extras.getString("query");
                    browseIntent.putExtra("query", query);
                }

                startActivity(browseIntent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                return true;

            case R.id.notification:
                Intent intent = new Intent(getApplicationContext(), NotificationActivity.class);
                intent.putExtra("layoutId", R.layout.activity_recipe_details);

                if (getIntent().hasExtra("mealId")) {
                    Bundle extras = getIntent().getExtras();
                    String mealId = extras.getString("mealId");
                    intent.putExtra("mealId", mealId);
                }

                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}