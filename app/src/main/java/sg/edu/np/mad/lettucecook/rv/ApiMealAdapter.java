package sg.edu.np.mad.lettucecook.rv;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;

import sg.edu.np.mad.lettucecook.models.ApiMeal;
import sg.edu.np.mad.lettucecook.R;
import sg.edu.np.mad.lettucecook.activities.RecipeDetailsActivity;
import sg.edu.np.mad.lettucecook.utils.DataSingleton;

public class ApiMealAdapter extends RecyclerView.Adapter<ApiMealViewHolder>{
    ArrayList<ApiMeal> data;
    ArrayList<ApiMeal> dataCopy;
    HashSet<String> mealIdHashSet; // For checking duplicates
    Context mContext;
    DataSingleton dataSingleton = DataSingleton.getInstance();

    public ApiMealAdapter(Context mContext) {
        this.data = new ArrayList<>();
        this.dataCopy = new ArrayList<>();
        this.mealIdHashSet = new HashSet<>();
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
                .into(holder.thumbnail);

        holder.name.setText(meal.getStrMeal());

        // Show the category and area if they are not null
        if (area != null) holder.area.setText(area);
        if (category != null) holder.category.setText(category);

        // Make view clickable, show recipe details page when clicked
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, RecipeDetailsActivity.class);
            intent.putExtra("mealId", meal.getIdMeal());
            mContext.startActivity(intent);
            ((Activity) mContext).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public void setData(ArrayList<ApiMeal> data) {
        this.data.addAll(data);

        // Used as a comparison list when searching
        // Data in this list should not be modified
        this.dataCopy.addAll(data);
    }

    public void addData(ArrayList<ApiMeal> data) {
        for (ApiMeal meal : data) {
            // Check for duplicates
            if (mealIdHashSet.add(meal.getIdMeal())) {
                this.data.add(meal);

                // Used as a comparison list when searching
                // Data in this list should not be modified
                this.dataCopy.add(meal);
                dataSingleton.addMeal(meal);
            }
        }
    }
    public void filter(String query) {
        // Clear data, meals will be added when user searches
        data.clear();

        if (query.isEmpty()) {
            data.addAll(dataCopy);
        } else {
            query = query.toLowerCase();

            // Do checks for each meal in dataCopy
            for (ApiMeal meal: dataCopy) {
                String[] checks = new String[] {
                        meal.getIdMeal(),
                        meal.getStrMeal(),
                        meal.getStrArea(),
                        meal.getStrCategory(),
                        meal.getStrDrinkAlternate(),
                        meal.getStrTags(),
                        meal.getDateModified()
                };

                // If query meets check criteria, add to data and break loop
                 for (String check : checks) {
                     if (check != null && !check.isEmpty() && check.toLowerCase().contains(query)) {
                         data.add(meal);
                         break;
                     }
                 }
            }
        }
        notifyDataSetChanged();
    }
}