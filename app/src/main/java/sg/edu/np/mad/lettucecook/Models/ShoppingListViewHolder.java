package sg.edu.np.mad.lettucecook.Models;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import sg.edu.np.mad.lettucecook.R;

public class ShoppingListViewHolder extends RecyclerView.ViewHolder {
    TextView measureTV;
    TextView ingredientTV;

    public ShoppingListViewHolder(View itemView) {
        super(itemView);
        ingredientTV = itemView.findViewById(R.id.ingredientTV);
        measureTV = itemView.findViewById(R.id.measureTV);
    }
}
