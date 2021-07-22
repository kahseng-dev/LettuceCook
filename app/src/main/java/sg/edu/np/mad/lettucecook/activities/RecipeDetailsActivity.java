package sg.edu.np.mad.lettucecook.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
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

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Vector;

import sg.edu.np.mad.lettucecook.R;
import sg.edu.np.mad.lettucecook.utils.IngredientClickListener;
import sg.edu.np.mad.lettucecook.utils.VolleyResponseListener;
import sg.edu.np.mad.lettucecook.models.ApiMeal;
import sg.edu.np.mad.lettucecook.models.DBHandler;
import sg.edu.np.mad.lettucecook.models.Ingredient;
import sg.edu.np.mad.lettucecook.rv.YoutubeAdapter;
import sg.edu.np.mad.lettucecook.rv.YoutubeVideo;
import sg.edu.np.mad.lettucecook.rv.ApiIngredientsAdapter;
import sg.edu.np.mad.lettucecook.utils.ApiMealJsonSingleton;
import sg.edu.np.mad.lettucecook.utils.ApiService;
import sg.edu.np.mad.lettucecook.utils.ApiURL;

public class RecipeDetailsActivity extends AppCompatActivity {
    DBHandler dbHandler = new DBHandler(this , null, null, 1);
    ImageView mealThumbnail;
    TextView mealName, mealCategory, areaText, instructionsText, dateModifiedText;
    RecyclerView ytRecyclerView, ingredientsRV;
    Button addToShoppingList, sourceLinkButton;
    Vector<YoutubeVideo> youtubeVideos = new Vector<>();
    ApiMealJsonSingleton apiMealJson = ApiMealJsonSingleton.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        ingredientsRV = findViewById(R.id.recipe_details_ingredients_rv);
        dateModifiedText = findViewById(R.id.recipe_details_date_modified_text);

        // Setting ViewById and attributes for YouTube recyclerView
        ytRecyclerView = findViewById(R.id.recipe_details_youtube_rv);
        ytRecyclerView.setHasFixedSize(true);
        ytRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Setting ViewById for buttons
        addToShoppingList = findViewById(R.id.recipe_details_add_to_shopping_list_button);
        sourceLinkButton = findViewById(R.id.recipe_details_source_button);

        // get the mealId to be viewed
        Bundle extras = getIntent().getExtras();
        String mealId = extras.getString("mealId");

        ApiService apiService = new ApiService(this);
        apiService.get(ApiURL.MealDB, "lookup.php?i=" + mealId, new VolleyResponseListener() {
            @Override
            public void onError(String message) { }

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray _meals = response.getJSONArray("meals");
                    ApiMeal meal = apiMealJson.mergeIntoJSONArray(_meals).get(0);
                    Log.v("Meal", meal.getIdMeal());
                    Picasso
                            .with(RecipeDetailsActivity.this)
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

                    int mealId = Integer.parseInt(meal.getIdMeal());
                    String[] ingredientNames = meal.getArrIngredients();
                    String[] measures = meal.getArrMeasures();

                    ArrayList<Ingredient> ingredients = new ArrayList<>();
                    for (int i = 0; i < ingredientNames.length; i++) {
                        ingredients.add(new Ingredient(mealId, ingredientNames[i], measures[i]));
                    }
                    ApiIngredientsAdapter adapter = new ApiIngredientsAdapter(ingredients, RecipeDetailsActivity.this, new IngredientClickListener() {
                        @Override
                        public void onItemClick(Ingredient ingredient) {
                            Intent intent = new Intent(getApplicationContext(), IngredientPopup.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_up, 0);
                        }
                    });

                    LinearLayoutManager mLayoutManager = new LinearLayoutManager(RecipeDetailsActivity.this);

                    // setting ingredients recycler view
                    ingredientsRV.setLayoutManager(mLayoutManager);
                    ingredientsRV.setItemAnimator(new DefaultItemAnimator());
                    ingredientsRV.setAdapter(adapter);

                    // embedding youtube videos
                    youtubeVideos.add(new YoutubeVideo(meal.getStrYoutube()));
                    YoutubeAdapter videoAdapter = new YoutubeAdapter(youtubeVideos);
                    ytRecyclerView.setAdapter(videoAdapter);

                    // if user clicks on View Source button, it will lead user to the source website.
                    sourceLinkButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String url = meal.getStrSource();
                            if (url.startsWith("https://") || url.startsWith("http://")) {
                                Uri uri = Uri.parse(url);
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                startActivity(intent);
                            }

                            else {
                                Toast.makeText(RecipeDetailsActivity.this, "Source Not Found", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    // if user clicks on add to shopping list, it will add the ingredients to shopping list.
                    addToShoppingList.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (getIntent().hasExtra("UserId")) {
                                Bundle extras = getIntent().getExtras();
                                int userId = extras.getInt("UserId");
                                String[] ingredients = meal.getArrIngredients();
                                String[] measures = meal.getArrMeasures();
                                for (int i = 0; i < ingredients.length; i++) {
                                    Ingredient ingredient = new Ingredient(
                                            Integer.parseInt(meal.getIdMeal()), ingredients[i], measures[i]);
                                    dbHandler.addItemToShoppingList(userId, ingredient);
                                }
                            }
                            // TODO: Check if user has logged in
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_toolbar, menu);
        menu.findItem(R.id.notification).setIcon(R.drawable.ic_notifications_white);
        return true;
    }

    // if the user clicks on the back button in the toolbar, bring them back to main activity.
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent browseIntent = new Intent(getApplicationContext(), MainActivity.class);

                // if the user is logged in, pass the userId as well.
                if (getIntent().hasExtra("UserId")) {
                    Bundle extras = getIntent().getExtras();
                    int userId = extras.getInt("UserId");
                    browseIntent.putExtra("UserId", userId);
                }

                startActivity(browseIntent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                return true;

            case R.id.notification:
                Intent notificationIntent = new Intent(getApplicationContext(), NotificationActivity.class);

                if (getIntent().hasExtra("UserId")) {
                    Bundle extras = getIntent().getExtras();
                    int userId = extras.getInt("UserId");
                    notificationIntent.putExtra("UserId", userId);
                }

                if (getIntent().hasExtra("mealId")) {
                    Bundle extras = getIntent().getExtras();
                    String mealId = extras.getString("mealId");
                    notificationIntent.putExtra("mealId", mealId);
                }

                startActivity(notificationIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}