package sg.edu.np.mad.lettucecook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

import sg.edu.np.mad.lettucecook.Models.YoutubeAdapter;
import sg.edu.np.mad.lettucecook.Models.YoutubeVideo;

public class RecipeDetails extends AppCompatActivity {
    TextView mealName, mealCategory, areaText, instructionsText, altDrinkText, tagText, sourceLink, dateModifiedText;
    RecyclerView recyclerView;
    ImageView mealThumbnail;
    Vector<YoutubeVideo> youtubeVideos = new Vector<YoutubeVideo>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        // set texts for meal data
        mealThumbnail = findViewById(R.id.mealThumbnail);
        mealName = findViewById(R.id.mealName);
        mealCategory = findViewById(R.id.mealCategoryText);
        areaText = findViewById(R.id.areaText);
        instructionsText = findViewById(R.id.instructionsText);
        altDrinkText = findViewById(R.id.altDrinkText);
        tagText = findViewById(R.id.tagText);
        sourceLink = findViewById(R.id.sourceLink);
        dateModifiedText = findViewById(R.id.dateModifiedText);

        // Youtube Video Guides Recycler View
        recyclerView = findViewById(R.id.ytRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Instantiate the RequestQueue.9
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://www.themealdb.com/api/json/v1/1/lookup.php?i=";
        String id = "52772";

        // Request a object response from the provided URL.
        JsonObjectRequest request = new JsonObjectRequest (Request.Method.GET, url + id, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("meals");
                    LoadImage loadImage = new LoadImage(mealThumbnail);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject meal = jsonArray.getJSONObject(i);
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

                        // TODO: GET INGREDIENT LIST

                        youtubeVideos.add(new YoutubeVideo(meal.getString("strYoutube")));
                        YoutubeAdapter videoAdapter = new YoutubeAdapter(youtubeVideos);
                        recyclerView.setAdapter(videoAdapter);
                    }
                } catch(JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mealCategory.setText("Unavailable");
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
}