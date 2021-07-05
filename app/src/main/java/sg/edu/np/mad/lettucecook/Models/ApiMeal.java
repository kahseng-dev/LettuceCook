package sg.edu.np.mad.lettucecook.Models;

public class ApiMeal {
    private String idMeal;

    private String strMeal;

    private String strDrinkAlternate;

    private String strCategory;

    private String strArea;

    private String strInstructions;

    private String strMealThumb;

    private String strTags;

    private String strYoutube;

    private String[] arrIngredients;

    private String[] arrMeasures;

    private String strSource;

    private String strImageSource;

    private String strCreativeCommonsConfirmed;

    private String dateModified;

    public ApiMeal(String idMeal, String strMeal, String strDrinkAlternate, String strCategory, String strArea, String strInstructions, String strMealThumb, String strTags, String strYoutube, String[] arrIngredients, String[] arrMeasures, String strSource, String strImageSource, String strCreativeCommonsConfirmed, String dateModified) {
        this.idMeal = idMeal;
        this.strMeal = strMeal;
        this.strDrinkAlternate = strDrinkAlternate;
        this.strCategory = strCategory;
        this.strArea = strArea;
        this.strInstructions = strInstructions;
        this.strMealThumb = strMealThumb;
        this.strTags = strTags;
        this.strYoutube = strYoutube;
        this.arrIngredients = arrIngredients;
        this.arrMeasures = arrMeasures;
        this.strSource = strSource;
        this.strImageSource = strImageSource;
        this.strCreativeCommonsConfirmed = strCreativeCommonsConfirmed;
        this.dateModified = dateModified;
    }

    public String getIdMeal() {
        return idMeal;
    }

    public String getStrMeal() {
        return strMeal;
    }

    public String getStrDrinkAlternate() {
        return strDrinkAlternate;
    }

    public String getStrCategory() {
        return strCategory;
    }

    public String getStrArea() {
        return strArea;
    }

    public String getStrInstructions() {
        return strInstructions;
    }

    public String getStrMealThumb() {
        return strMealThumb;
    }

    public String getStrTags() {
        return strTags;
    }

    public String getStrYoutube() {
        return strYoutube;
    }

    public String[] getArrIngredients() {
        return arrIngredients;
    }

    public String[] getArrMeasures() {
        return arrMeasures;
    }

    public String getStrSource() {
        return strSource;
    }

    public String getStrImageSource() {
        return strImageSource;
    }

    public String getStrCreativeCommonsConfirmed() {
        return strCreativeCommonsConfirmed;
    }

    public String getDateModified() {
        return dateModified;
    }
}
