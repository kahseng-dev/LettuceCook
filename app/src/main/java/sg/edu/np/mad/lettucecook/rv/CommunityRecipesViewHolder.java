package sg.edu.np.mad.lettucecook.rv;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import sg.edu.np.mad.lettucecook.R;

public class CommunityRecipesViewHolder extends RecyclerView.ViewHolder {
    TextView name;
    ImageView thumbnail;

    public CommunityRecipesViewHolder(View itemView) {
        super(itemView);
        this.name = itemView.findViewById(R.id.meal_name);
        this.thumbnail = itemView.findViewById(R.id.meal_thumbnail);
    }
}
