package sg.edu.np.mad.lettucecook;

import android.content.Context;
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
        ApiMeal meal = data.get(position);
        Picasso
                .with(mContext)
                .load(meal.getStrMealThumb())
                .into(holder.thumbnail);
        holder.name.setText(meal.getStrMeal());
        holder.area.setText(meal.getStrArea());
        holder.category.setText(meal.getStrCategory());
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }
}