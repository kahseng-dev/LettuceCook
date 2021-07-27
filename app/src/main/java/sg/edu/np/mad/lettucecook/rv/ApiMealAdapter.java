package sg.edu.np.mad.lettucecook.rv;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

import sg.edu.np.mad.lettucecook.models.ApiMeal;
import sg.edu.np.mad.lettucecook.R;
import sg.edu.np.mad.lettucecook.activities.RecipeDetailsActivity;

public class ApiMealAdapter extends RecyclerView.Adapter<ApiMealViewHolder>{
    ArrayList<ApiMeal> data;
    String query;
    Context mContext;

    public ApiMealAdapter(ArrayList<ApiMeal> input, String query, Context mContext) {
        this.data = input;
        this.query = query;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ApiMealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.meal_item,
                parent,
                false);

        return new ApiMealViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull ApiMealViewHolder holder, int position) {
        ApiMeal meal = data.get(position);
        String area = meal.getStrArea(); // Get area
        String category = meal.getStrCategory(); // Get category

        // Load image via URL into ImageView
        Picasso
                .with(mContext)
                .load(meal.getStrMealThumb())
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        holder.thumbnail.setBackground(new BitmapDrawable(mContext.getResources(), bitmap));
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) { }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) { }
                });

        holder.name.setText(meal.getStrMeal());

        // Show the category and area if they are not null
        if (area != null) holder.area.setText(area);
        if (category != null) holder.category.setText(category);

        // Make view clickable, show recipe details page when clicked
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, RecipeDetailsActivity.class);
            intent.putExtra("mealId", meal.getIdMeal());
            intent.putExtra("query", query);
            mContext.startActivity(intent);
            ((Activity) mContext).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }
}