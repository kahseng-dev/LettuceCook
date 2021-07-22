package sg.edu.np.mad.lettucecook.rv;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import sg.edu.np.mad.lettucecook.R;
import sg.edu.np.mad.lettucecook.models.Ingredient;
import sg.edu.np.mad.lettucecook.utils.ApiService;
import sg.edu.np.mad.lettucecook.utils.ApiURL;
import sg.edu.np.mad.lettucecook.utils.IngredientClickListener;

// Viewholder for Ingredient in recipe details page
public class ApiIngredientViewHolder extends RecyclerView.ViewHolder {
    TextView measure, name, calorie;

    public ApiIngredientViewHolder(View itemView) {
        super(itemView);
        this.measure = itemView.findViewById(R.id.api_ingredient_measure);
        this.name = itemView.findViewById(R.id.api_ingredient_name);
        this.calorie = itemView.findViewById(R.id.api_ingredient_calories);
    }

    public void bind(final Ingredient ingredient, final IngredientClickListener listener) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(ingredient);
            }
        });
    }
}
