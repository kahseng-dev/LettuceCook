package sg.edu.np.mad.lettucecook;

// Enum to make it easier to decide which URL to use when sending requests.
public enum ApiURL {
    MealDB("https://www.themealdb.com/api/json/v1/1/"),
    CalorieNinjas("https://api.calorieninjas.com/v1/nutrition?query="),
    ;

    private final String url;

    ApiURL(final String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return url;
    }
}
