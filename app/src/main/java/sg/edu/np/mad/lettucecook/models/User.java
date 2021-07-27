package sg.edu.np.mad.lettucecook.models;

import java.util.ArrayList;
import java.util.HashMap;

public class User {
    public String username;
    public String email;
    public ArrayList<Ingredient> shoppingList;
    public HashMap<String, CreatedRecipe> createdRecipesList;
    public HashMap<String, String> favouritesList;

    public User () { }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public HashMap<String, String> getFavouritesList() {
        return favouritesList;
    }

    public void setFavouritesList(HashMap<String, String> favouritesList) {
        this.favouritesList = favouritesList;
    }

    public HashMap<String, CreatedRecipe> getCreatedRecipesList() {
        return createdRecipesList;
    }

    public void setCreatedRecipesList(HashMap<String, CreatedRecipe> createdRecipesList) {
        this.createdRecipesList = createdRecipesList;
    }


    public ArrayList<Ingredient> getShoppingList() {
        return shoppingList;
    }

    public void setShoppingList(ArrayList<Ingredient> shoppingList) {
        this.shoppingList = shoppingList;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
