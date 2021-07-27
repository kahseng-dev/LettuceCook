package sg.edu.np.mad.lettucecook.rv;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

import sg.edu.np.mad.lettucecook.R;
import sg.edu.np.mad.lettucecook.activities.RecipeDetailsActivity;
import sg.edu.np.mad.lettucecook.models.ApiMeal;

public class AccountFavouritesAdapter extends RecyclerView.Adapter<AccountFavouritesViewHolder> {
    ArrayList<ApiMeal> data;
    Context mContext;

    public AccountFavouritesAdapter(ArrayList<ApiMeal> input, Context mContext) {
        this.data = input;
        this.mContext = mContext;
    }

    public AccountFavouritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.favourites_item, parent, false);
        return new AccountFavouritesViewHolder(item);
    }

    // Bind View Holder
    public void onBindViewHolder(AccountFavouritesViewHolder holder, int position) {
        ApiMeal meal = data.get(position);

        // Load image via URL into ImageView
        Picasso
                .with(mContext)
                .load(meal.getStrMealThumb())
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    holder.favouritesThumbnail.setBackground(new BitmapDrawable(mContext.getResources(), bitmap));
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) { }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) { }
        });

        holder.favouriteRecipeName.setText(meal.getStrMeal());

        // Make image clickable, show recipe details page when clicked
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, RecipeDetailsActivity.class);
            intent.putExtra("mealId", meal.getIdMeal());
            mContext.startActivity(intent);
            ((Activity) mContext).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
    }

    public void addData(ApiMeal meal) {
        this.data.add(meal);
    }

    @Override
    public int getItemCount() { return data == null ? 0 : data.size(); }
}
