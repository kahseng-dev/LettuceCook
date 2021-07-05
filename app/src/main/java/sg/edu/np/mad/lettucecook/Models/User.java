package sg.edu.np.mad.lettucecook.Models;

import java.util.ArrayList;

public class User {
    public int userId;
    public String username;
    public String password;
    public ArrayList<Recipe> favouritesList;
    public ArrayList<Ingredient> shoppingList;
    public ArrayList<Recipe> recipesMade;

    public User() {
        favouritesList = new ArrayList<Recipe>();
        shoppingList = new ArrayList<Ingredient>();
        recipesMade = new ArrayList<Recipe>();
    }

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

    public ArrayList<Recipe> getFavouritesList() {
        return favouritesList;
    }

    public void setFavouritesList(ArrayList<Recipe> favouritesList) {
        this.favouritesList = favouritesList;
    }

    public ArrayList<Ingredient> getShoppingList() {
        return shoppingList;
    }

    public void setShoppingList(ArrayList<Ingredient> shoppingList) {
        this.shoppingList = shoppingList;
    }

    public ArrayList<Recipe> getRecipesMade() {
        return recipesMade;
    }

    public void setRecipesMade(ArrayList<Recipe> recipesMade) {
        this.recipesMade = recipesMade;
    }
}
