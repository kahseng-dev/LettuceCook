package sg.edu.np.mad.lettucecook.rv;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import sg.edu.np.mad.lettucecook.R;
import sg.edu.np.mad.lettucecook.activities.CreateAccountActivity;
import sg.edu.np.mad.lettucecook.activities.LoginActivity;
import sg.edu.np.mad.lettucecook.activities.RecipeDetailsActivity;
import sg.edu.np.mad.lettucecook.activities.ShoppingListActivity;
import sg.edu.np.mad.lettucecook.models.DBHandler;
import sg.edu.np.mad.lettucecook.models.Ingredient;

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListViewHolder>{
    ArrayList<Ingredient> data;
    String userID;
    Context mContext;

    public ShoppingListAdapter(ArrayList<Ingredient> input, Context context, String userID) {
        this.data = input;
        this.mContext = context;
        this.userID = userID;
    }

    public ShoppingListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopping_list_item, parent, false);
        return new ShoppingListViewHolder(item);
    }

    public void onBindViewHolder(ShoppingListViewHolder holder, int position ) {
        DBHandler dbHandler = new DBHandler(mContext , null, null, 1);

        Ingredient ingredient = data.get(position);
        holder.ingredientTV.setText(ingredient.ingredientName);
        holder.measureTV.setText(ingredient.measure);

        // if the user clicks on the cross image, it will delete the ingredient from the shopping list
        // and also feedback the deletion using the notifyItemRemoved back to the user.
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.remove(position);
                dbHandler.deleteShoppingItem(userID, ingredient);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,data.size());

                if (data.isEmpty()) {
                    mContext.startActivity(new Intent (mContext, ShoppingListActivity.class));
                    ((Activity) mContext).overridePendingTransition(0, 0);
                }
            }
        });

        holder.viewMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, RecipeDetailsActivity.class);
                intent.putExtra("mealId", Integer.toString(ingredient.getMealId()));
                mContext.startActivity(intent);
                ((Activity) mContext).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    public int getItemCount() {
        return data.size();
    }
}
