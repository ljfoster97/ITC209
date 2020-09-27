package com.example.navgraphtest.Database;

/**
 * Model of food items within the DB.
 * A "Food Item" can be any meal or object.
 * I.e a piece of fruit such as a Banana, or a Big Mac from McDonald's.
 */
public class FoodItemModel {
    // Declarations.
    private String itemID;
    private String categoryTitle;
    private String itemName;
    int itemCalories;
    private byte[] itemPhoto;

    // Empty constructor.
    public FoodItemModel() {

    }

    // Constructor.
    public FoodItemModel(String itemName, int itemCalories, String categoryTitle, byte[] itemPhoto) {
        this.itemID = itemID;
        this.itemName = itemName;
        this.itemCalories = itemCalories;
        this.categoryTitle = categoryTitle;
        this.itemPhoto = itemPhoto;
    }

    // getter and setter methods
    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getItemCalories() {
        return itemCalories;
    }

    public void setItemCalories(int calories) {
        this.itemCalories = calories;
    }

    public String getItemCategoryTitle() {
        return categoryTitle;
    }

    public void setItemCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public byte[] getPhoto() {
        return itemPhoto;
    }

    public void setPhoto(byte[] photo) {
        this.itemPhoto = photo;
    }
}

