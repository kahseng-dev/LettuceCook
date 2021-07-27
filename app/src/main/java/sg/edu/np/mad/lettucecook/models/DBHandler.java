package sg.edu.np.mad.lettucecook.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "LettuceCook.db";

    // Shopping List Table and Columns
    public static final String TABLE_SHOPPING_LIST = "shoppingList";
    public static final String SHOPPING_LIST_COLUMN_MEALID = "mealId";
    public static final String SHOPPING_LIST_COLUMN_INGREDIENTNAME = "ingredient";
    public static final String SHOPPING_LIST_COLUMN_MEASURE = "measure";
    public static final String USER_COLUMN_ID = "userId";

    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate (SQLiteDatabase db) {
        // Create shopping list table string
        String CREATE_SHOPPING_LIST_TABLE = "CREATE TABLE " + TABLE_SHOPPING_LIST + "("
                + USER_COLUMN_ID + " INTEGER,"
                + SHOPPING_LIST_COLUMN_MEALID + " INTEGER,"
                + SHOPPING_LIST_COLUMN_INGREDIENTNAME + " TEXT,"
                + SHOPPING_LIST_COLUMN_MEASURE + " INTEGER" + ")";

        // execute table creation query string
        db.execSQL(CREATE_SHOPPING_LIST_TABLE);
    }

    public static void deleteDatabase(Context context) {
        context.deleteDatabase(DATABASE_NAME);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHOPPING_LIST);
        onCreate(db);
    }

    // passing the ingredient object and the userId that is logged in,
    // add the ingredient into the shopping list table
    public void addItemToShoppingList(String userID, Ingredient ingredient) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(USER_COLUMN_ID, userID);
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
    public ArrayList<Ingredient> getShoppingList(String userID) {
        String query = "SELECT * FROM " + TABLE_SHOPPING_LIST +
                " WHERE " + USER_COLUMN_ID + "=\"" + userID + "\"";

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
    public void deleteShoppingItem(String userID, Ingredient ingredient) {
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "DELETE FROM " + TABLE_SHOPPING_LIST +
                " WHERE " + USER_COLUMN_ID + "=\"" + userID + "\"" +
                " AND " + SHOPPING_LIST_COLUMN_MEALID + "=\"" + ingredient.getMealId() + "\"" +
                " AND " + SHOPPING_LIST_COLUMN_INGREDIENTNAME + "=\"" + ingredient.getIngredientName() + "\"";

        db.execSQL(query);
        db.close();
    }

    public void clearUserShoppingList(String userID) {
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "DELETE FROM " + TABLE_SHOPPING_LIST +
                " WHERE " + USER_COLUMN_ID + "=\"" + userID + "\"";

        db.execSQL(query);
        db.close();
    }
}
