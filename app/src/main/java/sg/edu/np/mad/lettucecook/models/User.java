package sg.edu.np.mad.lettucecook.models;

import java.util.ArrayList;

public class User {
    public String username;
    public String email;
    public ArrayList<Ingredient> shoppingList;

    public User () { }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
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
