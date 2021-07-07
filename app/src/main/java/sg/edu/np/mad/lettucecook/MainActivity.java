package sg.edu.np.mad.lettucecook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

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

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sg.edu.np.mad.lettucecook.Models.ApiMeal;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String url = "https://www.themealdb.com/api/json/v1/1/random.php";
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        ArrayList<ApiMeal> meals = new ArrayList<ApiMeal>();
                        try {
                            JSONArray _meals = response.getJSONArray("meals");
                            for (int i = 0; i < _meals.length(); i++) {
                                /*
                                The JSON object is one-dimensional, strMeasure and strIngredients are not stored in
                                arrays, but instead there are 20 of each, named from strMeasure1 to strMeasure20,
                                and strIngredient1 to strIngredient20.

                                In this section, I merge them to arrays: arrMeasures and arrIngredients. The
                                length of both arrays are the same and they are mapped by the same indices.

                                For example, arrIngredients[0]'s measure will be taken from arrMeasures[0]. However,
                                the JSON response is unordered, so ingredients may not be mapped to the same measures
                                using the same index.

                                Therefore, I use I read the JSON object into a TreeMap<String, String> so that the
                                strMeasure and strIngredient are sorted such that arrIngredients[n] would be correctly
                                mapped to arrMeasures[n] after merging.
                                */
                                Gson gson = new Gson();
                                TreeMap<String, String> sortedMealMap = gson.fromJson(_meals.get(i).toString(), TreeMap.class);
                                String mealStr = gson.toJson(sortedMealMap);
                                mealStr = mergeJSON(mealStr);

                                ApiMeal meal = gson.fromJson(mealStr, ApiMeal.class);
                                meals.add(meal);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        error.printStackTrace();
                    }
                });
        queue.add(jsonObjectRequest);
    }

    // Merge all 'strIngredient' to an 'arrIngredients', and likewise for 'strMeasure'.
    private String mergeJSON(String json) {
        // Remove strIngredients/strMeasure that are null or empty, e.g.:
        // "strIngredient": "" , "strMeasure": null,
        // "strIngredient": "" , "strMeasure": null
        json = json.replaceAll("(\"str(Ingredient|Measure)\\d+\":)(\"\"|null)", "");

        // Remove trailing '}'
        json = json.substring(0, json.length() - 1);

        json = _mergeJSON(json, true);
        json = _mergeJSON(json, false);

        // Add back '}'
        json += "}";

        // Remove all duplicate commas, e.g. ',,,,,' -> ','
        json = json.replaceAll(",(?=,)", "");

        return json;
    }

    // This method merges a single meal object's 'strIngredient' or 'strMeasure'
    // to form an 'arrIngredients' or 'arrMeasures'.
    private String _mergeJSON(String json, boolean isIngredient) {
        String str_ = isIngredient ? "Ingredient" : "Measure";
        String regex = "(\"str" + str_ + "\\d+\":)(\".*?\")"; // e.g. ("strIngredient\d+":)(".*?")
        String array = "\"arr" + str_ + "s\": [" ;            // e.g. arrIngredients: [

        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(json);
        json = json.replaceAll(regex, "");

        while (m.find()) {
            // replace ("key": "value") with ("value",)
            array += m.group().replaceAll(regex, "$2,");
        }

        // Replace trailing ',' with ']'
        array = array.substring(0, array.length() - 1) + "]";

        array = json + "," + array;

        return array;
    }
}