package sg.edu.np.mad.lettucecook.models;

public class RecipeImage {

    private String imageUrl;
    public RecipeImage() {

    }

    public RecipeImage(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
