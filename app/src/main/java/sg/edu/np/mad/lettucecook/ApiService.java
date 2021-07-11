package sg.edu.np.mad.lettucecook;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import sg.edu.np.mad.lettucecook.Models.ApiMeal;

public class ApiService {
    Context context;

    public ApiService(Context context) {
        this.context = context;
    }

    public void get(ApiURL URL, String query, JSONObject json, VolleyResponseListener listener) {
        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, URL.toString() + query, json, new Response.Listener<JSONObject>() {
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
}