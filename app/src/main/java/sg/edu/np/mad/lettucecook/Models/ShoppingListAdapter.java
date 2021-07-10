package sg.edu.np.mad.lettucecook.Models;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import sg.edu.np.mad.lettucecook.R;

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListViewHolder>{
    ArrayList<Ingredient> data;

    public ShoppingListAdapter(ArrayList<Ingredient> input) {
        data = input;
    }

    public ShoppingListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopping_list_recyclerview, parent, false);
        return new ShoppingListViewHolder(item);
    }

    public void onBindViewHolder(ShoppingListViewHolder holder, int position ) {
        Ingredient ingredient = data.get(position);
        holder.ingredientTV.setText(ingredient.ingredientName);
        holder.measureTV.setText(ingredient.measure);
    }

    public int getItemCount() {
        return data.size();
    }
}
