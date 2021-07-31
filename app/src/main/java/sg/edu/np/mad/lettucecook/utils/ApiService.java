package sg.edu.np.mad.lettucecook.utils;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

// This class helps in sending requests to the API, MealDB or CalorieNinjas.
public class ApiService {
    Context context;

    public ApiService(Context context) {
        this.context = context;
    }

    // ApiURL is an enum with two variants: MealDB and CalorieNinjas
    public void get(String query, VolleyResponseListener listener) {
        JsonObjectRequest request = new JsonObjectRequest
                (ApiURL.MealDB + query, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            listener.onResponse(response);
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
        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    // This method sends request with a headers
    // ApiURL is an enum.
    public void getIngredient(String query, VolleyResponseListener listener) {
        // Remove clove(s) from the name, as it doesn't work with CalorieNinjas API
        query = query.replaceAll(" cloves?", "");
        JsonObjectRequest request = new JsonObjectRequest
                (ApiURL.CalorieNinjas + query, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            listener.onResponse(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, error -> {
                    // TODO: Handle error
                    error.printStackTrace();
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("X-Api-Key", "TXyvRpCPgwhOIpFhfB4L3Q==vVHI825lte4zHJ3a");
                return headers;
            }
        };
        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }
}