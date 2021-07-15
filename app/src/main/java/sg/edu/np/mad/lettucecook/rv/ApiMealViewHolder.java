package sg.edu.np.mad.lettucecook.rv;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import sg.edu.np.mad.lettucecook.R;

public class ApiMealViewHolder extends RecyclerView.ViewHolder {
    private ImageView thumbnail;
    private TextView name, area, category;

    public ApiMealViewHolder(View itemView) {
        super(itemView);
        this.thumbnail = itemView.findViewById(R.id.api_meal_thumbnail);
        this.name = itemView.findViewById(R.id.api_meal_name);
        this.area = itemView.findViewById(R.id.api_meal_area);
        this.category = itemView.findViewById(R.id.api_meal_category);
    }
}
