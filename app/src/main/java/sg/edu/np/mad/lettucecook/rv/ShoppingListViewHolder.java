package sg.edu.np.mad.lettucecook.rv;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import sg.edu.np.mad.lettucecook.R;

public class ShoppingListViewHolder extends RecyclerView.ViewHolder {
    TextView measureTV;
    TextView ingredientTV;
    ImageView delete;
    ImageView viewMeal;

    public ShoppingListViewHolder(View itemView) {
        super(itemView);
        ingredientTV = itemView.findViewById(R.id.shopping_list_item_ingredient);
        measureTV = itemView.findViewById(R.id.shopping_list_item_measure);
        delete = itemView.findViewById(R.id.shopping_list_item_delete);
        viewMeal = itemView.findViewById(R.id.shopping_list_item_view_meal);
    }
}
