package sg.edu.np.mad.lettucecook;


import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

import sg.edu.np.mad.lettucecook.Models.ApiMeal;

public class ApiMealJsonSingleton {
    private static ApiMealJsonSingleton instance;
    private static Gson gson;

    private ApiMealJsonSingleton() {
        gson = getGson();
    }

    public static synchronized ApiMealJsonSingleton getInstance() {
        if (instance == null) {
            instance = new ApiMealJsonSingleton();
        }
        return instance;
    }

    private Gson getGson() {
        if (gson == null) {
            gson = new Gson();
        }
        return gson;
    }

    // Get a String[] of category or area
    public String[] parseFilterArray(JSONArray _filters) {
        String filtersStr = _filters
                .toString()
                .replaceAll("(\\{\"str\\w+\":)(\"\\w+\")(\\})", "$2");
        return getGson().fromJson(filtersStr, String[].class);
    }

    /* The JSON object is one-dimensional, strMeasure and strIngredients are not stored
       in arrays, but instead there are 20 of each, named from strMeasure1 to strMeasure20,
       and strIngredient1 to strIngredient20. This method parses */
    public ArrayList<ApiMeal> mergeIntoJSONArray(JSONArray _meals) throws JSONException {
        ArrayList<ApiMeal> meals = new ArrayList<ApiMeal>();
        for (int i = 0; i < _meals.length(); i++) {
            ApiMeal meal = getGson().fromJson(_mergeIntoJSONArray(_meals.getJSONObject(i)), ApiMeal.class);
            meals.add(meal);
        }
        return meals;
    }

    private String _mergeIntoJSONArray(JSONObject json) throws JSONException {
        // Merge only if meal has ingredient fields
        if (!json.has("strIngredient1")) {
            return json.toString();
        }

        JSONArray arrIngredients = new JSONArray();
        JSONArray arrMeasures = new JSONArray();

        for (int i = 1; i <= 20; i++) {
            String ingredientKey = "strIngredient" + i;
            String measureKey = "strMeasure" + i;

            String ingredientValue = json.getString(ingredientKey);
            String measureValue = json.getString(measureKey);

            if (!(json.isNull(ingredientKey) || ingredientValue.equals(""))) {
                arrIngredients.put(ingredientValue);
                arrMeasures.put(measureValue);
            }

            json.remove(ingredientKey);
            json.remove(measureKey);
        }

        json.put("arrIngredients", arrIngredients);
        json.put("arrMeasures", arrMeasures);

        return json.toString();
    }
}