package sg.edu.np.mad.lettucecook.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

import sg.edu.np.mad.lettucecook.models.ApiMeal;
import sg.edu.np.mad.lettucecook.models.NinjaIngredient;

// A Singleton design is used as only one instance is needed for the entire application.
// This is more efficient since there is no need to recreate the object when needed.
public class ApiJsonSingleton {
    private static ApiJsonSingleton instance;
    private static Gson gson;

    private ApiJsonSingleton() {
        gson = getGson();
    }

    public static synchronized ApiJsonSingleton getInstance() {
        // Gets instance. If null, makes a new instance
        // and returns the same instance in the future.
        if (instance == null) {
            instance = new ApiJsonSingleton();
        }
        return instance;
    }

    private Gson getGson() {
        // Gets gson property. If null, creates a new Gson object
        // and returns the same instance in the future.
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

    /* The JSON object (MealDB) is one-dimensional, strMeasure and strIngredients are not stored
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

            String ingredientValue = json.getString(ingredientKey).toLowerCase();
            String measureValue = json.getString(measureKey).toLowerCase();

            // Add items to array if not null or empty
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

    public ArrayList<NinjaIngredient> parseNinjaIngredients(JSONArray json, String[] ingredientNames, String[] measures) {
        ArrayList<NinjaIngredient> _ninjaIngredients = getGson().fromJson
                (json.toString(), new TypeToken<ArrayList<NinjaIngredient>>(){}.getType());
        ArrayList<NinjaIngredient> ninjaIngredients = new ArrayList<>();

        for (int i = 0; i < ingredientNames.length; i++) {
            String ingredientName = ingredientNames[i];
            NinjaIngredient ninjaIngredient = searchNinjaIngredient(_ninjaIngredients, ingredientName);

            // If ninjaIngredient is null, create a new NinjaIngredient with just name and measure.
            if (ninjaIngredient == null)
                ninjaIngredient = new NinjaIngredient(ingredientName);

            ninjaIngredient.setMeasure(measures[i]);
            ninjaIngredients.add(ninjaIngredient);
        }

        return ninjaIngredients;
    }

    private NinjaIngredient searchNinjaIngredient(ArrayList<NinjaIngredient> ninjaIngredients, String ingredientName) {
        for (NinjaIngredient i : ninjaIngredients)
            if (ingredientName.contains(i.getName()))
                return i;
        return null;
    }
}