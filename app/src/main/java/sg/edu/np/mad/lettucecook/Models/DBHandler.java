package sg.edu.np.mad.lettucecook.Models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "LettuceCook.db";

    // User Table and Columns
    public static final String TABLE_USERS = "user";
    public static final String USER_COLUMN_ID = "userId";
    public static final String USER_COLUMN_USERNAME = "username";
    public static final String USER_COLUMN_PASSWORD = "password";

    // Shopping List Table and Columns
    public static final String TABLE_SHOPPING_LIST = "shoppingList";
    public static final String SHOPPING_LIST_COLUMN_MEALID = "mealId";
    public static final String SHOPPING_LIST_COLUMN_INGREDIENTNAME = "ingredient";
    public static final String SHOPPING_LIST_COLUMN_MEASURE = "measure";

    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate (SQLiteDatabase db) {
        // Create user table string
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + USER_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + USER_COLUMN_USERNAME + " TEXT,"
                + USER_COLUMN_PASSWORD + " TEXT" + ")";

        // Create shopping list table string
        String CREATE_SHOPPING_LIST_TABLE = "CREATE TABLE " + TABLE_SHOPPING_LIST + "("
                + USER_COLUMN_ID + " INTEGER,"
                + SHOPPING_LIST_COLUMN_MEALID + " INTEGER,"
                + SHOPPING_LIST_COLUMN_INGREDIENTNAME + " TEXT,"
                + SHOPPING_LIST_COLUMN_MEASURE + " INTEGER" + ")";

        // execute table creation query string
        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_SHOPPING_LIST_TABLE);
    }

    public static void deleteDatabase(Context context) {
        context.deleteDatabase(DATABASE_NAME);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHOPPING_LIST);
        onCreate(db);
    }

    public ArrayList<User> getUsers() {
        ArrayList<User> userList = new ArrayList<User>();

        String selectQuery = "SELECT * FROM " + TABLE_USERS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setUserId(Integer.parseInt(cursor.getString(0)));
                user.setUsername(cursor.getString(1));
                user.setPassword(cursor.getString(2));
                userList.add(user);
            } while (cursor.moveToNext());
        }

        return userList;
    }

    // adding user to user table
    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USER_COLUMN_USERNAME, user.getUsername());
        values.put(USER_COLUMN_PASSWORD, user.getPassword());

        db.insert(TABLE_USERS, null, values);
        db.close();
    }

    // finding a user through their username
    public User findUser(String username) {
        String query = "SELECT * FROM " + TABLE_USERS +
                        " WHERE " + USER_COLUMN_USERNAME + "=\"" + username + "\"";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        User user = new User();

        if (cursor.moveToFirst()) {
            user.setUserId(cursor.getInt(0));
            user.setUsername(cursor.getString(1));
            user.setPassword(cursor.getString(2));
            cursor.close();
        }

        else { // if user is not found, return user is null
            user = null;
        }

        db.close();
        return user;
    }

    // get the details of the user from the userID
    public User getDetails(int userId) {
        String query = "SELECT * FROM " + TABLE_USERS +
                " WHERE " + USER_COLUMN_ID + "=\"" + userId + "\"";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        User user = new User();

        if (cursor.moveToFirst()) {
            user.setUserId(cursor.getInt(0));
            user.setUsername(cursor.getString(1));
            user.setPassword(cursor.getString(2));
            cursor.close();
        }

        else { // if user is not found, return user is null
            user = null;
        }

        db.close();
        return user;
    }

    // passing the ingredient object and the userId that is logged in,
    // add the ingredient into the shopping list table
    public void addItemToShoppingList(int userId, Ingredient ingredient) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(USER_COLUMN_ID, userId);
        values.put(SHOPPING_LIST_COLUMN_MEALID, ingredient.getMealId());
        values.put(SHOPPING_LIST_COLUMN_INGREDIENTNAME, ingredient.getIngredientName());
        values.put(SHOPPING_LIST_COLUMN_MEASURE, ingredient.getMeasure());

        db.insert(TABLE_SHOPPING_LIST, null, values);
        db.close();
    }

    // getting all items in the shopping list table
    // this is used to view the table
    public ArrayList<Ingredient> getAllShoppingList() {
        ArrayList<Ingredient> shoppingLists = new ArrayList<Ingredient>();

        String selectQuery = "SELECT * FROM " + TABLE_SHOPPING_LIST;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Ingredient ingredient = new Ingredient(
                        cursor.getInt(1),
                        cursor.getString(2),
                        cursor.getString(3)
                        );

                shoppingLists.add(ingredient);
            } while (cursor.moveToNext());
        }

        return shoppingLists;
    }

    // get the shopping list of a user by passing the userId
    public ArrayList<Ingredient> getShoppingList(int userId) {
        String query = "SELECT * FROM " + TABLE_SHOPPING_LIST +
                " WHERE " + USER_COLUMN_ID + "=\"" + userId + "\"";

        ArrayList<Ingredient> shoppingList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Ingredient ingredient = new Ingredient(
                        cursor.getInt(1),
                        cursor.getString(2),
                        cursor.getString(3)
                );

                shoppingList.add(ingredient);
            } while (cursor.moveToNext());
        }

        db.close();
        return shoppingList;
    }

    // delete a shopping item by passing the userId from the user that is logged in
    // and the ingredient that is to be deleted.
    public void deleteShoppingItem(int userId, Ingredient ingredient) {
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "DELETE FROM " + TABLE_SHOPPING_LIST +
                " WHERE " + USER_COLUMN_ID + "=\"" + userId + "\"" +
                " AND " + SHOPPING_LIST_COLUMN_MEALID + "=\"" + ingredient.getMealId() + "\"" +
                " AND " + SHOPPING_LIST_COLUMN_INGREDIENTNAME + "=\"" + ingredient.getIngredientName() + "\"";

        db.execSQL(query);
        db.close();
    }
}
