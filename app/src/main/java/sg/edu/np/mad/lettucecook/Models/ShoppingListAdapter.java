package sg.edu.np.mad.lettucecook.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import sg.edu.np.mad.lettucecook.R;

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListViewHolder>{
    ArrayList<Ingredient> data;
    int userID;
    Context mContext;

    public ShoppingListAdapter(ArrayList<Ingredient> input, Context context, int userId) {
        data = input;
        mContext = context;
        userID = userId;
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
                notifyItemRemoved(position);
                dbHandler.deleteShoppingItem(userID, ingredient);
            }
        });
    }

    public int getItemCount() {
        return data.size();
    }
}
