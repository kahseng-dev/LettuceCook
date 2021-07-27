package sg.edu.np.mad.lettucecook.models;

import java.io.Serializable;
import java.util.ArrayList;

public class CreatedRecipe implements Serializable {

    public String recipeName;
    public String recipeArea;
    public String recipeCategory;
    public String recipeInstructions;
    public String recipeImageURL;
    public ArrayList<CreatedIngredient> ingredientList;
    public boolean publishState;

    public CreatedRecipe(String recipeName, String recipeArea, String recipeCategory, String recipeInstructions, String recipeImageURL, ArrayList<CreatedIngredient> ingredientList) {
        this.recipeName = recipeName;
        this.recipeArea = recipeArea;
        this.recipeCategory = recipeCategory;
        this.recipeInstructions = recipeInstructions;
        this.recipeImageURL = recipeImageURL;
        this.ingredientList = ingredientList;
        this.publishState = false;
    }

    public CreatedRecipe(String recipeName, String recipeArea, String recipeCategory, String recipeInstructions, ArrayList<CreatedIngredient> ingredientList) {
        this.recipeName = recipeName;
        this.recipeArea = recipeArea;
        this.recipeCategory = recipeCategory;
        this.recipeInstructions = recipeInstructions;
        this.ingredientList = ingredientList;
        this.publishState = false;
    }

    public CreatedRecipe() { }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getRecipeArea() {
        return recipeArea;
    }

    public void setRecipeArea(String recipeArea) {
        this.recipeArea = recipeArea;
    }

    public String getRecipeCategory() {
        return recipeCategory;
    }

    public void setRecipeCategory(String recipeCategory) {
        this.recipeCategory = recipeCategory;
    }

    public String getRecipeInstructions() {
        return recipeInstructions;
    }

    public void setRecipeInstructions(String recipeInstructions) {
        this.recipeInstructions = recipeInstructions;
    }

    public ArrayList<CreatedIngredient> getIngredientList() {
        return ingredientList;
    }

    public void setIngredientList(ArrayList<CreatedIngredient> ingredientList) {
        this.ingredientList = ingredientList;
    }

    public boolean isPublishState() {
        return publishState;
    }

    public void setPublishState(boolean publishState) {
        this.publishState = publishState;
    }
}
