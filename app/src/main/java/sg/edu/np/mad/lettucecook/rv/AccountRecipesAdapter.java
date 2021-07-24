package sg.edu.np.mad.lettucecook.rv;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import sg.edu.np.mad.lettucecook.R;
import sg.edu.np.mad.lettucecook.activities.AccountRecipesActivity;
import sg.edu.np.mad.lettucecook.activities.IngredientsActivity;
import sg.edu.np.mad.lettucecook.models.CreatedRecipe;
import sg.edu.np.mad.lettucecook.models.Ingredient;

public class AccountRecipesAdapter extends RecyclerView.Adapter<AccountRecipesViewHolder> {
    ArrayList<CreatedRecipe> recipeList;
    String userID;
    Context context;

    // Call account recipes adapter
    public AccountRecipesAdapter(ArrayList<CreatedRecipe> recipeList, Context mContext, String userId) {
        this.recipeList = recipeList;
        this.context = mContext;
        this.userID = userId;
    }

    // ViewHolder for Account Recipes
    public AccountRecipesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_recipe, parent, false);
        return new AccountRecipesViewHolder(item);
    }

    // Bind View Holder
    public void onBindViewHolder(AccountRecipesViewHolder holder, int position) {
        CreatedRecipe recipe = recipeList.get(position);
        holder.accountRecipeName.setText(recipe.recipeName);
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }
}