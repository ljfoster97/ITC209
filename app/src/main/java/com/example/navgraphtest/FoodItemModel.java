package com.example.navgraphtest;

public class FoodItemModel {
    String itemID;
    String categoryTitle;
    String itemName;
    int calories;

    public FoodItemModel() {

    }



    public FoodItemModel(String itemName, int calories, String categoryTitle) {
        this.itemID = itemID;
        this.itemName = itemName;
        this.calories = calories;
        this.categoryTitle = categoryTitle;
    }




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
        return calories;
    }

    public void setItemCalories(int calories) {
        this.calories = calories;
    }

    public String getItemCategoryTitle() { return categoryTitle; }

    public void setItemCategoryTitle(String categoryTitle) { this.categoryTitle = categoryTitle; }
}

