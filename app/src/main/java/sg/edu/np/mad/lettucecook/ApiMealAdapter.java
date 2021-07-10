package sg.edu.np.mad.lettucecook;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import sg.edu.np.mad.lettucecook.Models.ApiMeal;

public class ApiMealAdapter extends RecyclerView.Adapter<ApiMealViewHolder>{
    ArrayList<ApiMeal> data;
    int userId;
    Context mContext;

    public ApiMealAdapter(ArrayList<ApiMeal> input, Context mContext) {
        this.data = input;
        this.mContext = mContext;
    }

    public ApiMealAdapter(ArrayList<ApiMeal> input, int userId,Context mContext) {
        this.data = input;
        this.userId = userId;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ApiMealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.api_meal,
                parent,
                false);

        return new ApiMealViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull ApiMealViewHolder holder, int position) {
        ApiMeal meal = data.get(position);
        String area = meal.getStrArea();
        String category = meal.getStrCategory();
        Picasso
                .with(mContext)
                .load(meal.getStrMealThumb())
                .into(holder.thumbnail);
        holder.name.setText(meal.getStrMeal());
        if (area != null) holder.area.setText(area);
        if (category != null) holder.category.setText(category);

        holder.thumbnail.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, RecipeDetailsActivity.class);
            intent.putExtra("mealId", meal.getIdMeal());
            if (userId != 0) {
                intent.putExtra("UserId", userId);
            }
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }
}