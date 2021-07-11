package sg.edu.np.mad.lettucecook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Vector;

import sg.edu.np.mad.lettucecook.Models.ApiMeal;
import sg.edu.np.mad.lettucecook.Models.DBHandler;
import sg.edu.np.mad.lettucecook.Models.Ingredient;
import sg.edu.np.mad.lettucecook.Models.YoutubeAdapter;
import sg.edu.np.mad.lettucecook.Models.YoutubeVideo;

public class RecipeDetailsActivity extends AppCompatActivity {
    DBHandler dbHandler = new DBHandler(this , null, null, 1);
    ImageView mealThumbnail;
    TextView mealName, mealCategory, areaText, instructionsText, ingredientText, altDrinkText, tagText, sourceLink, dateModifiedText;
    RecyclerView ytRecyclerView;
    Button addToShoppingList, addRecipeWidget;
    Vector<YoutubeVideo> youtubeVideos = new Vector<>();
    ApiMealJsonSingleton apiMealJson = ApiMealJsonSingleton.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        // Setting ViewById for Meal Details
        mealThumbnail = findViewById(R.id.mealThumbnail);
        mealName = findViewById(R.id.mealName);
        mealCategory = findViewById(R.id.mealCategoryText);
        areaText = findViewById(R.id.areaText);
        instructionsText = findViewById(R.id.instructionsText);
        RecyclerView ingredientsRV = findViewById(R.id.ingredientsRV);
        altDrinkText = findViewById(R.id.altDrinkText);
        tagText = findViewById(R.id.tagText);
        sourceLink = findViewById(R.id.sourceLink);
        dateModifiedText = findViewById(R.id.dateModifiedText);

        // Setting ViewById and attributes for YouTube recyclerView
        ytRecyclerView = findViewById(R.id.ytRecyclerView);
        ytRecyclerView.setHasFixedSize(true);
        ytRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Setting add to shopping list button
        addToShoppingList = findViewById(R.id.addShoppingListButton);

        Bundle extras = getIntent().getExtras();
        String mealId = extras.getString("mealId");

        ApiService apiService = new ApiService(this);
        apiService.get(ApiURL.MealDB, "lookup.php?i=" + mealId, new VolleyResponseListener() {
            @Override
            public void onError(String message) {

            }

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray _meals = response.getJSONArray("meals");
                    ApiMeal meal = apiMealJson.mergeIntoJSONArray(_meals).get(0);
                    Log.v("Meal", meal.getIdMeal());
                    Picasso
                            .with(RecipeDetailsActivity.this)
                            .load(meal.getStrMealThumb())
                            .into(mealThumbnail);

                    setTitle(meal.getStrMeal());
                    mealName.setText(meal.getStrMeal());
                    mealCategory.setText(meal.getStrCategory());
                    areaText.setText(meal.getStrArea());
                    instructionsText.setText(meal.getStrInstructions());
                    altDrinkText.setText(meal.getStrDrinkAlternate());
                    tagText.setText(meal.getStrTags());
                    sourceLink.setText(meal.getStrSource());
                    dateModifiedText.setText(meal.getDateModified());

                    int mealId = Integer.parseInt(meal.getIdMeal());
                    String[] ingredientNames = meal.getArrIngredients();
                    String[] measures = meal.getArrMeasures();

                    ArrayList<Ingredient> ingredients = new ArrayList<>();
                    for (int i = 0; i < ingredientNames.length; i++) {
                        ingredients.add(new Ingredient(mealId, ingredientNames[i], measures[i]));
                    }
                    ApiIngredientsAdapter adapter = new ApiIngredientsAdapter(ingredients,RecipeDetailsActivity.this);

                    LinearLayoutManager mLayoutManager = new LinearLayoutManager(RecipeDetailsActivity.this);

                    ingredientsRV.setLayoutManager(mLayoutManager);
                    ingredientsRV.setItemAnimator(new DefaultItemAnimator());
                    ingredientsRV.setAdapter(adapter);

                    youtubeVideos.add(new YoutubeVideo(meal.getStrYoutube()));
                    YoutubeAdapter videoAdapter = new YoutubeAdapter(youtubeVideos);
                    ytRecyclerView.setAdapter(videoAdapter);

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

    private class LoadImage extends AsyncTask<String, Void, Bitmap> {
        ImageView thumbnail;

        public LoadImage(ImageView mealThumbnail) {
            this.thumbnail = mealThumbnail;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            String url = strings[0];
            Bitmap bitmap = null;

            try {
                InputStream inputStream = new java.net.URL(url).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch(IOException e) {
                e.printStackTrace();
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            mealThumbnail.setImageBitmap(bitmap);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent browseIntent = new Intent(getApplicationContext(), MainActivity.class);

                if (getIntent().hasExtra("UserId")) {
                    Bundle extras = getIntent().getExtras();
                    int userId = extras.getInt("UserId");
                    browseIntent.putExtra("UserId", userId);
                }

                startActivity(browseIntent);
                overridePendingTransition(0, 0);

                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}