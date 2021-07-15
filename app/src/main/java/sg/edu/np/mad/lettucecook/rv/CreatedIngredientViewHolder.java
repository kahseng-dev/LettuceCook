package sg.edu.np.mad.lettucecook.rv;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import sg.edu.np.mad.lettucecook.R;

public class CreatedIngredientViewHolder extends RecyclerView.ViewHolder {
    TextView ingredientName, ingredientMeasure;

    public CreatedIngredientViewHolder(View itemView) {
        super(itemView);
        ingredientName = itemView.findViewById(R.id.row_ingredient_name);
        ingredientMeasure = itemView.findViewById(R.id.row_ingredient_measure);
    }
}