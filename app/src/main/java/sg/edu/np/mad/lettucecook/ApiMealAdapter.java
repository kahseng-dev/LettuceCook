package sg.edu.np.mad.lettucecook;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
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
    Context mContext;

    public ApiMealAdapter(ArrayList<ApiMeal> input, Context mContext) {
        this.data = input;
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
        Context context = holder.itemView.getContext();
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
            Intent intent = new Intent(context, RecipeDetailsActivity.class);
            intent.putExtra("mealId", meal.getIdMeal());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }
}