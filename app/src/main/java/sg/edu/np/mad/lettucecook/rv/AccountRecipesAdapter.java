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

public class AccountRecipesAdapter extends RecyclerView.Adapter<AccountRecipesViewHolder> {
    ArrayList<CreatedRecipe> recipeList;
    ArrayList<String> recipeIDList;
    String userID;
    Context context;

    // Call account recipes adapter
    public AccountRecipesAdapter(ArrayList<CreatedRecipe> recipeList, Context mContext, ArrayList<String> recipeIDList, String userID) {
        this.recipeList = recipeList;
        this.context = mContext;
        this.recipeIDList = recipeIDList;
        this.userID = userID;
    }

    // ViewHolder for Account Recipes
    public AccountRecipesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_recipe, parent, false);
        return new AccountRecipesViewHolder(item);
    }

    // Bind View Holder
    public void onBindViewHolder(AccountRecipesViewHolder holder, int position) {
        CreatedRecipe recipe = recipeList.get(position);

        holder.accountRecipeName.setText(recipe.recipeName); // Set recipe name
        Picasso.with(context).load(recipe.recipeImageURL).into(holder.accountRecipeImage); // Add recipe image

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CustomRecipeActivity.class);
                intent.putExtra("userID", userID);
                intent.putExtra("Recipe", recipeList.get(position));
                intent.putExtra("recipeId", recipeIDList.get(position));
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }
}
