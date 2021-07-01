package sg.edu.np.mad.lettucecook.Models;

import java.util.List;

public class User {
    public int userId;
    public String username;
    public String password;
    public List<Recipe> favouritesList;
    public List<Ingredient> shoppingList;
    public List<Recipe> recipesMade;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Recipe> getFavouritesList() {
        return favouritesList;
    }

    public void setFavouritesList(List<Recipe> favouritesList) {
        this.favouritesList = favouritesList;
    }

    public List<Ingredient> getShoppingList() {
        return shoppingList;
    }

    public void setShoppingList(List<Ingredient> shoppingList) {
        this.shoppingList = shoppingList;
    }

    public List<Recipe> getRecipesMade() {
        return recipesMade;
    }

    public void setRecipesMade(List<Recipe> recipesMade) {
        this.recipesMade = recipesMade;
    }
}
