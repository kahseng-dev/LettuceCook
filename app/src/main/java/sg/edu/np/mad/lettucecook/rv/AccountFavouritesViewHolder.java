package sg.edu.np.mad.lettucecook.rv;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import sg.edu.np.mad.lettucecook.R;

public class AccountFavouritesViewHolder extends RecyclerView.ViewHolder {
    TextView favouriteRecipeName;
    ImageView favouritesThumbnail;

    public AccountFavouritesViewHolder(View itemView) {
        super(itemView);
        favouriteRecipeName = itemView.findViewById(R.id.favourites_name);
        favouritesThumbnail = itemView.findViewById(R.id.favourites_thumbnail);
    }
}
