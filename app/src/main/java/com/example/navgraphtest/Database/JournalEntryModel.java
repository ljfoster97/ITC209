package com.example.navgraphtest.Database;

public class JournalEntryModel {
    String entryID;
    String entryDate;
    String itemID;

    public JournalEntryModel() {
    }

    public JournalEntryModel(String entryID, String entryDate, String itemID) {
        this.entryID = entryID;
        this.entryDate = entryDate;
        this.itemID = itemID;
    }

    public String getEntryID() {
        return entryID;
    }

    public void setEntryID(String entryID) {
        this.entryID = entryID;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }
}
