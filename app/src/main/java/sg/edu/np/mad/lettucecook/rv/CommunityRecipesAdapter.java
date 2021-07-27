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

import sg.edu.np.mad.lettucecook.R;
import sg.edu.np.mad.lettucecook.activities.CustomRecipeActivity;
import sg.edu.np.mad.lettucecook.models.CreatedRecipe;

public class CommunityRecipesAdapter extends RecyclerView.Adapter<BrowseViewHolder> {
    ArrayList<CreatedRecipe> data;
    ArrayList<String> recipeIDList;
    Context mContext;

    public CommunityRecipesAdapter(ArrayList<CreatedRecipe> input, ArrayList<String> recipeIDList, Context context) {
        this.data = input;
        this.mContext = context;
        this.recipeIDList = recipeIDList;
    }

    @NonNull
    @Override
    public BrowseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.browse_item,
                parent,
                false);

        return new BrowseViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull BrowseViewHolder holder, int position) {
        CreatedRecipe meal = data.get(position);
        holder.name.setText(meal.getRecipeName()); // Add recipe name
        Picasso.with(mContext).load(meal.recipeImageURL).into(holder.thumbnail); // Add recipe image

        // Make view clickable
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, CustomRecipeActivity.class);
            intent.putExtra("Recipe", data.get(position));
            intent.putExtra("recipeId", recipeIDList.get(position));
            mContext.startActivity(intent);
            ((Activity) mContext).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left); // Animation
        });
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }
}
