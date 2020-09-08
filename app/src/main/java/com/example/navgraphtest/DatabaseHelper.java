package com.example.navgraphtest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Foodiva";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_CATEGORIES = "foodCategories";

    private static final String TABLE_ITEMS = "foodItems";

    private static final String KEY_ITEM_ID = "itemID";
    private static final String KEY_ITEM_NAME = "itemName";
    private static final String KEY_ITEM_CALORIES = "itemCalories";
    private static final String KEY_CATEGORY = "categoryTitle";

    private static final String TABLE_ENTRIES = "journalEntries";

    private static final String KEY_ENTRY_ID = "entryID";
    private static final String KEY_DATE = "entryDate";



    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating tables
    @Override
    public void onCreate(SQLiteDatabase database) {
        String CREATE_CATEGORIES, CREATE_ITEMS_TABLE, CREATE_ENTRY_TABLE ;
        //creating table
         CREATE_CATEGORIES =
                 "CREATE TABLE " + TABLE_CATEGORIES
                     + "("

                         + "ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                         + "Title TEXT"

                     + ")";

         CREATE_ITEMS_TABLE =
                 "CREATE TABLE " + TABLE_ITEMS
                     + "("

                     + KEY_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                     + KEY_ITEM_NAME + " TEXT,"
                     + KEY_ITEM_CALORIES + " TEXT,"
                     + KEY_CATEGORY + " TEXT,"

                     + "FOREIGN KEY ("+KEY_CATEGORY+") REFERENCES " + TABLE_CATEGORIES + "(Title)"

                     + ")";

         CREATE_ENTRY_TABLE =
                 "CREATE TABLE " + TABLE_ENTRIES
                     + "("

                     + KEY_ENTRY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                     + KEY_DATE + " TEXT,"
                     + KEY_ITEM_ID + " INTEGER,"
                     + "FOREIGN KEY ("+KEY_ITEM_ID+") REFERENCES " + TABLE_ITEMS + "("+KEY_ITEM_ID+")"

                     + ")";


        database.execSQL(CREATE_CATEGORIES);
        database.execSQL(CREATE_ITEMS_TABLE);
        database.execSQL(CREATE_ENTRY_TABLE);


    }

    //upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_ENTRIES);

        // create tables again
        onCreate(database);
    }

    // add new category
    public void addCategory(String title) {
        SQLiteDatabase database = this .getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Title", title);
        //inserting new row
        database.insert(TABLE_CATEGORIES, null , values);
        //close database connection
        database.close();
    }

    public void addFoodItem (FoodItemModel item) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ITEM_NAME, item.getItemName());
        values.put(KEY_ITEM_CALORIES, item.getItemCalories());
        values.put(KEY_CATEGORY, item.getItemCategoryTitle());

        database.insert(TABLE_ITEMS, null, values);
        database.close();
    }

    public void addJournalEntry(FoodItemModel item) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DATE, String.valueOf(CalendarDay.today()));
        values.put(KEY_ITEM_ID, item.itemID);

    }

    //Getting a FoodItem by ID:
    public FoodItemModel getFoodItem(int id) {
        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = database.query(TABLE_ITEMS, new String[] { KEY_ITEM_ID,
                        KEY_ITEM_NAME, KEY_ITEM_CALORIES, KEY_CATEGORY }, KEY_ITEM_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

            FoodItemModel item = new FoodItemModel();
            item.setItemID(cursor.getString(0));
            item.setItemName(cursor.getString(1));
            item.setItemCalories(Integer.parseInt(cursor.getString(2)));
            item.setItemCategoryTitle(cursor.getString(3));

        return item;
    }

    public int updateFoodItem(FoodItemModel item) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ITEM_NAME, item.getItemName());
        values.put(KEY_ITEM_CALORIES, item.getItemCalories());
        values.put(KEY_CATEGORY, item.getItemCategoryTitle());

        //Update record:
        return database.update(TABLE_ITEMS, values, KEY_ITEM_ID + " = ?",
                new String[] { String.valueOf(item.getItemID()) });
    }

    public void deleteFoodItem(FoodItemModel item) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TABLE_ITEMS, KEY_ITEM_ID + " = ?",
                new String[] { String.valueOf(item.getItemID()) });
        database.close();
    }

    public ArrayList<FoodItemModel> getAllFoodItems() {
        ArrayList<FoodItemModel> items = new ArrayList<FoodItemModel>();

        String selectQuery = "SELECT  * FROM " + TABLE_ITEMS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                //Create empty item, and populate it with tale date from row:
                FoodItemModel item = new FoodItemModel();
                item.setItemID(cursor.getString(0));
                item.setItemName(cursor.getString(1));
                item.setItemCalories(Integer.parseInt(cursor.getString(2)));
                item.setItemCategoryTitle(cursor.getString(3));
                //Add item to list:
                items.add(item);
            } while (cursor.moveToNext());
        }

        return items;
    }

    public ArrayList<FoodItemModel> getFoodItemsByCategory(String category) {
        ArrayList<FoodItemModel> items = new ArrayList<FoodItemModel>();

        String selectQuery = "SELECT * FROM " + TABLE_ITEMS + " WHERE " +
                KEY_CATEGORY + " = '" + category + "'";

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                FoodItemModel item = new FoodItemModel();
                item.setItemID(cursor.getString(0));
                item.setItemName(cursor.getString(1));
                item.setItemCalories(Integer.parseInt(cursor.getString(2)));
                item.setItemCategoryTitle(cursor.getString(3));

                // add item to the list
                items.add(item);
            } while (cursor.moveToNext());
        }

        return items;
    }



    public ArrayList<CategoryModel> getCategories() {
        ArrayList<CategoryModel> arrayList = new ArrayList<>();

        // select all query
        String select_query= "SELECT * FROM " + TABLE_CATEGORIES;

        SQLiteDatabase database = this .getWritableDatabase();
        Cursor cursor = database.rawQuery(select_query, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                CategoryModel categoryModel = new CategoryModel();
                categoryModel.setCategoryID(cursor.getString(0));
                categoryModel.setCategoryTitle(cursor.getString(1));
                arrayList.add(categoryModel);
            }while (cursor.moveToNext());
        }
        return arrayList;
    }

    //delete the note
    public void deleteCategory(String ID) {
        SQLiteDatabase database = this.getWritableDatabase();
        //deleting row
        database.delete(TABLE_CATEGORIES, "ID=" + ID, null);
        database.close();
    }

    //update the note
    public void updateCategory(String title, String ID) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values =  new ContentValues();
        values.put("Title", title);
        //updating row
        database.update(TABLE_CATEGORIES, values, "ID=" + ID, null);
        database.close();
    }


}