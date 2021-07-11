package sg.edu.np.mad.lettucecook;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import sg.edu.np.mad.lettucecook.Models.Ingredient;

public class ApiIngredientsAdapter extends RecyclerView.Adapter<ApiIngredientViewHolder>{
    ArrayList<Ingredient> data;
    Context mContext;
    ApiService apiService;
    double totalCalories = 0;

    public ApiIngredientsAdapter(ArrayList<Ingredient> input, Context mContext) {
        this.data = input;
        this.mContext = mContext;
        this.apiService = new ApiService(mContext);
    }

    @NonNull
    @Override
    public ApiIngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.api_ingredient,
                parent,
                false);

        return new ApiIngredientViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull ApiIngredientViewHolder holder, int position) {
        Ingredient ingredient = data.get(position);
        String measure = ingredient.getMeasure();
        String name = ingredient.getIngredientName();
        holder.measure.setText(measure);
        holder.name.setText(name);

        String query = (measure.trim() + " " + name.trim());
        // TODO: handle oils

        apiService.getIngredient(ApiURL.CalorieNinjas, query, new VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Log.v("Meal", message);
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject nutrients = response.getJSONArray("items").getJSONObject(0);
                    double calories = nutrients.getDouble("calories");
                    totalCalories += calories;
                    holder.calorie.setText(calories + " kcal");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }
}