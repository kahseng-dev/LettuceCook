package sg.edu.np.mad.lettucecook.rv;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import sg.edu.np.mad.lettucecook.R;

public class AccountRecipesViewHolder extends RecyclerView.ViewHolder {
    TextView accountRecipeName;
    ImageView accountRecipeImage;

    public AccountRecipesViewHolder(View itemView) {
        super(itemView);
        accountRecipeName = itemView.findViewById(R.id.account_recipe_name);
        accountRecipeImage = itemView.findViewById(R.id.account_recipe_image);
    }
}
