package sg.edu.np.mad.lettucecook;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ApiMealViewHolder extends RecyclerView.ViewHolder {
    ImageView thumbnail;
    TextView name, area, category;

    public ApiMealViewHolder(View itemView) {
        super(itemView);
        this.thumbnail = itemView.findViewById(R.id.featured_meal_thumbnail);
        this.name = itemView.findViewById(R.id.featured_meal_name);
        this.area = itemView.findViewById(R.id.featured_meal_area);
        this.category = itemView.findViewById(R.id.featured_meal_category);
    }
}
