package sg.edu.np.mad.lettucecook;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Vector;

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
        ingredientText = findViewById(R.id.ingredientText);
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

        // Instantiate the RequestQueue.9
        RequestQueue queue = Volley.newRequestQueue(this);
        Bundle extras = getIntent().getExtras();
        String url = "https://www.themealdb.com/api/json/v1/1/lookup.php?i=";
        String mealId = extras.getString("mealId");

        // Request a object response from the provided URL.
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url + mealId, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("meals");
                    LoadImage loadImage = new LoadImage(mealThumbnail);
                    ArrayList<Ingredient> ingredientList = new ArrayList<>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject meal = jsonArray.getJSONObject(i);
                        int mealId = meal.getInt("idMeal");
                        String name = meal.getString("strMeal");
                        loadImage.execute(meal.getString("strMealThumb"));
                        String category = meal.getString("strCategory");
                        String area = meal.getString("strArea");
                        String instructions = meal.getString("strInstructions");
                        String altDrink = meal.getString("strDrinkAlternate");
                        String tags = meal.getString("strTags");
                        String source = meal.getString("strSource");
                        String dateModified = meal.getString("dateModified");

                        setTitle(name);
                        mealName.setText(name);
                        mealCategory.setText(category);
                        areaText.setText(area);
                        instructionsText.setText(instructions);
                        altDrinkText.setText(altDrink);
                        tagText.setText(tags);
                        sourceLink.setText(source);
                        dateModifiedText.setText(dateModified);

                        youtubeVideos.add(new YoutubeVideo(meal.getString("strYoutube")));
                        YoutubeAdapter videoAdapter = new YoutubeAdapter(youtubeVideos);
                        ytRecyclerView.setAdapter(videoAdapter);


                        for (int num = 1; num < 20; num++) {
                            String ingredient = meal.getString("strIngredient" + num);
                            String measure = meal.getString("strMeasure" + num);

                            if (!(ingredient.equals("null") || ingredient.equals(""))) {
                                if (!(measure.equals("null") || measure.equals(""))) {
                                    ingredientText.append(ingredient + " of " + measure + "\n\n");
                                    ingredientList.add(new Ingredient(mealId, ingredient, measure));

                                    addToShoppingList.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (getIntent().hasExtra("UserId")) {
                                                Bundle extras = getIntent().getExtras();
                                                int userId = extras.getInt("UserId");

                                                for (Ingredient item : ingredientList) {
                                                    dbHandler.addItemToShoppingList(userId, item);
                                                }
                                            }
                                            // TODO: Check if user has logged in
                                        }
                                    });
                                }
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        // Add the request to the RequestQueue.
        queue.add(request);
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