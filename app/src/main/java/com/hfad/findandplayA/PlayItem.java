package com.hfad.findandplayA;

public class PlayItem {
    private int CategoryID = 0;
    private int ItemID = 0;
    private String ItemName = "";
    private String Description = "";
    //TODO add photo reference when it is implemented in Firestore (need getter/setter too)

    // * Note: Non-parameterized constructor and ALL getter/setter methods required
    // * to convert Firebase document to class object @ SlotMachine.java - PopulateCategories() - DO NOT REMOVE
    public PlayItem() {
    }


    public int getcategoryID() {
        return CategoryID;
    }

    public void setCategoryID(int categoryID) {
        CategoryID = categoryID;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getItemID() {
        return ItemID;
    }

    public void setItemID(int itemID) {
        ItemID = itemID;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }
}
