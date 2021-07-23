package sg.edu.np.mad.lettucecook.rv;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import sg.edu.np.mad.lettucecook.R;
import sg.edu.np.mad.lettucecook.models.NinjaIngredient;
import sg.edu.np.mad.lettucecook.utils.ApiService;
import sg.edu.np.mad.lettucecook.utils.IngredientClickListener;

public class ApiIngredientsAdapter extends RecyclerView.Adapter<ApiIngredientViewHolder>{
    ArrayList<NinjaIngredient> data;
    Context mContext;
    ApiService apiService;
    private final IngredientClickListener listener;
    double totalCalories = 0;

    public ApiIngredientsAdapter(ArrayList<NinjaIngredient> input, Context mContext, IngredientClickListener ingredientClickListener) {
        this.data = input;
        this.mContext = mContext;
        this.apiService = new ApiService(mContext);
        this.listener = ingredientClickListener;
    }

    @NonNull
    @Override
    public ApiIngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.api_ingredient,
                parent,
                false);

        return new ApiIngredientViewHolder(item);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ApiIngredientViewHolder holder, int position) {
        NinjaIngredient ingredient = data.get(position);
        String measure = ingredient.getMeasure();
        String name = ingredient.getName();
        float calories = ingredient.getCalories();
        holder.name.setText(name);
        holder.measure.setText(measure);
        if (calories >= 0) holder.calorie.setText(calories + " kcal");
        holder.bind(ingredient, listener);
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }
}