package com.example.navgraphtest.Database;

/**
 * Model for categories.
 * These are their own seperate entity within the DB so that Tabs can created/updated for them dynamically.
 */
public class CategoryModel {

    String categoryID;
    String categoryTitle;

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

}