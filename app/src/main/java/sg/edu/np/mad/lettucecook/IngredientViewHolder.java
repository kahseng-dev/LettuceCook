package sg.edu.np.mad.lettucecook;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class IngredientViewHolder extends RecyclerView.ViewHolder {
    TextView ingredientName, ingredientMeasure;

    public IngredientViewHolder(View itemView) {
        super(itemView);
        ingredientName = itemView.findViewById(R.id.text_ingredient_name);
        ingredientMeasure = itemView.findViewById(R.id.text_ingredient_measure);
    }
}