package sg.edu.np.mad.lettucecook.rv;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.Random;

import sg.edu.np.mad.lettucecook.R;
import sg.edu.np.mad.lettucecook.activities.BrowseActivity;

public class BrowseAdapter extends RecyclerView.Adapter<BrowseViewHolder>{
    Context mContext;
    String[] data;
    String[] images;

    public BrowseAdapter(Context mContext) {
        this.mContext = mContext;
        data = mContext.getResources().getStringArray(R.array.browse_categories);
        images = mContext.getResources().getStringArray(R.array.browse_categories_images);

        String[] browseRandomImages = mContext.getResources().getStringArray(R.array.browse_random_images);
        int i = new Random().nextInt(browseRandomImages.length);
        images[0] = browseRandomImages[i]; // Change first image to a random one
    }

    @NonNull
    @Override
    public BrowseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.browse_item,
                parent,
                false);

        return new BrowseViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull BrowseViewHolder holder, int position) {
        String meal = data[position];
        String url = "https://www.themealdb.com/images/media/meals/" + images[position] + ".jpg";

        // Load image via URL into ImageView
        Picasso
                .with(mContext)
                .load(url)
                .into(holder.thumbnail);

        holder.name.setText(meal);

        // Make image clickable, show recipe details page when clicked
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, BrowseActivity.class);
            intent.putExtra("query", meal);
            mContext.startActivity(intent);
            ((Activity) mContext).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            Toast.makeText(mContext, meal, Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.length;
    }
}