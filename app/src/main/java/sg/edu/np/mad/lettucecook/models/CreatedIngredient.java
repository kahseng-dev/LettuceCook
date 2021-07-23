package sg.edu.np.mad.lettucecook.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class CreatedIngredient implements Parcelable {

    public String ingredientName;
    public String ingredientMeasure;

    public CreatedIngredient() { }

    public CreatedIngredient(String ingredientName, String ingredientMeasure) {
        this.ingredientName = ingredientName;
        this.ingredientMeasure = ingredientMeasure;
    }

    protected CreatedIngredient(Parcel in) {
        ingredientName = in.readString();
        ingredientMeasure = in.readString();
    }

    // CREATOR for Parcelable
    public static final Creator<CreatedIngredient> CREATOR = new Creator<CreatedIngredient>() {
        @Override
        public CreatedIngredient createFromParcel(Parcel in) {
            return new CreatedIngredient(in);
        }

        @Override
        public CreatedIngredient[] newArray(int size) {
            return new CreatedIngredient[size];
        }
    };

    // Getter Setter
    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public String getIngredientMeasure() {
        return ingredientMeasure;
    }

    public void setIngredientMeasure(String ingredientMeasure) {
        this.ingredientMeasure = ingredientMeasure;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ingredientName);
        dest.writeString(ingredientMeasure);
    }
}
