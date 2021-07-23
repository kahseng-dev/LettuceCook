package sg.edu.np.mad.lettucecook.models;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import sg.edu.np.mad.lettucecook.models.CreatedIngredient;

public class CreatedRecipe {

    public String recipeName;
    public String recipeArea;
    public String recipeCategory;

    public CreatedRecipe(String recipeName, String recipeArea, String recipeCategory, String recipeInstructions, ArrayList<CreatedIngredient> ingredientList) {
        this.recipeName = recipeName;
        this.recipeArea = recipeArea;
        this.recipeCategory = recipeCategory;
        this.recipeInstructions = recipeInstructions;
        this.ingredientList = ingredientList;
    }

    public String recipeInstructions;
    public ArrayList<CreatedIngredient> ingredientList;

    public CreatedRecipe(String string, String string1, String string2, String string3, String string4) {
    }

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
}
