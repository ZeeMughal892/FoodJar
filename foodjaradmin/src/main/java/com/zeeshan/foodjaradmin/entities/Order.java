package com.zeeshan.foodjaradmin.entities;

public class Order {

    private String ItemID;
    private String ItemName;
    private String ItemCategory;
    private String ItemPrice;
    private String ItemQuantity;
    private String ItemUnit;
    private String ItemImage;
    private String UserID;

    public Order() {

    }

    public Order(String itemID, String itemName, String itemCategory, String itemPrice, String itemQuantity, String itemUnit, String itemImage,String userID) {
        ItemID = itemID;
        ItemName = itemName;
        ItemCategory = itemCategory;
        ItemPrice = itemPrice;
        ItemQuantity = itemQuantity;
        ItemUnit = itemUnit;
        ItemImage = itemImage;
        UserID = userID;
    }

    public String getItemID() {
        return ItemID;
    }

    public void setItemID(String itemID) {
        ItemID = itemID;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getItemCategory() {
        return ItemCategory;
    }

    public void setItemCategory(String itemCategory) {
        ItemCategory = itemCategory;
    }

    public String getItemPrice() {
        return ItemPrice;
    }

    public void setItemPrice(String itemPrice) {
        ItemPrice = itemPrice;
    }

    public String getItemQuantity() {
        return ItemQuantity;
    }

    public void setItemQuantity(String itemQuantity) {
        ItemQuantity = itemQuantity;
    }

    public String getItemUnit() {
        return ItemUnit;
    }

    public void setItemUnit(String itemUnit) {
        ItemUnit = itemUnit;
    }

    public String getItemImage() {
        return ItemImage;
    }

    public void setItemImage(String itemImage) {
        ItemImage = itemImage;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }
}
