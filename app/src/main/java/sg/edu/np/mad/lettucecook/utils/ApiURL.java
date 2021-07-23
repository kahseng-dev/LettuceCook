package sg.edu.np.mad.lettucecook.utils;

// Enum to make it easier to decide which URL to use when sending requests.
public enum ApiURL {
    MealDB("https://www.themealdb.com/api/json/v2/9973533/"),
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
