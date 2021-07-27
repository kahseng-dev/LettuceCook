package sg.edu.np.mad.lettucecook.rv;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import sg.edu.np.mad.lettucecook.R;

public class BrowseViewHolder extends RecyclerView.ViewHolder {
    ImageView thumbnail;
    TextView name;

    public BrowseViewHolder(View itemView) {
        super(itemView);
        this.thumbnail = itemView.findViewById(R.id.browse_thumbnail);
        this.name = itemView.findViewById(R.id.browse_name);
    }
}
