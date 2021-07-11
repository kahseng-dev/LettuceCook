package sg.edu.np.mad.lettucecook;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ApiIngredientViewHolder extends RecyclerView.ViewHolder {
    TextView measure, name, calorie;

    public ApiIngredientViewHolder(View itemView) {
        super(itemView);
        this.measure = itemView.findViewById(R.id.api_ingredient_measure);
        this.name = itemView.findViewById(R.id.api_ingredient_name);
        this.calorie = itemView.findViewById(R.id.api_ingredient_calorie);
    }
}
