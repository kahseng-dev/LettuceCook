package sg.edu.np.mad.lettucecook.rv;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import sg.edu.np.mad.lettucecook.R;
import sg.edu.np.mad.lettucecook.models.CreatedIngredient;

public class CreatedIngredientAdapter extends RecyclerView.Adapter<CreatedIngredientViewHolder> {
    ArrayList<CreatedIngredient> ingredientsList;
    Context context;

    public CreatedIngredientAdapter(ArrayList<CreatedIngredient> ingredients, Context mContext) {
        this.ingredientsList = ingredients;
        this.context = mContext;
    }

    public CreatedIngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_ingredient, parent, false);
        return new CreatedIngredientViewHolder(item);
    }

    public void onBindViewHolder(CreatedIngredientViewHolder holder, int position) {
        CreatedIngredient ingredient = ingredientsList.get(position);
        holder.ingredientName.setText(ingredient.getIngredientName());
        holder.ingredientMeasure.setText(ingredient.getIngredientMeasure());
    }

    @Override
    public int getItemCount() {
        return ingredientsList.size();
    }
}