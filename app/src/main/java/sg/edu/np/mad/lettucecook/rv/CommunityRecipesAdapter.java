package sg.edu.np.mad.lettucecook.rv;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

import sg.edu.np.mad.lettucecook.R;
import sg.edu.np.mad.lettucecook.activities.CustomRecipeActivity;
import sg.edu.np.mad.lettucecook.activities.RecipeDetailsActivity;
import sg.edu.np.mad.lettucecook.models.CreatedRecipe;

public class CommunityRecipesAdapter extends RecyclerView.Adapter<CommunityRecipesViewHolder> {
    ArrayList<CreatedRecipe> data;
    Context mContext;

    public CommunityRecipesAdapter(ArrayList<CreatedRecipe> input, Context context) {
        this.data = input;
        this.mContext = context;
    }

    @NonNull
    @Override
    public CommunityRecipesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.meal_item,
                parent,
                false);

        return new CommunityRecipesViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull CommunityRecipesViewHolder holder, int position) {
        CreatedRecipe meal = data.get(position);
        holder.name.setText(meal.getRecipeName());

        holder.name.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, CustomRecipeActivity.class);
            intent.putExtra("Recipe", data.get(position));
            mContext.startActivity(intent);
            ((Activity) mContext).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }
}
