package sg.edu.np.mad.lettucecook;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.ArrayList;

import sg.edu.np.mad.lettucecook.Models.ApiMeal;

public class ApiMealService {
    Context context;

    static String URL = "https://www.themealdb.com/api/json/v1/1/";

    public ApiMealService(Context context) {
        this.context = context;
    }

    public void getMeals(String url, VolleyResponseListener listener) {
        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, URL + url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        listener.onResponse(response);
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
}
