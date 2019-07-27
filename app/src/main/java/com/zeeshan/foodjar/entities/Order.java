package com.zeeshan.foodjar.entities;

public class Order {

    private String ItemID;
    private String ItemName;
    private String ItemCategory;
    private String ItemPrice;
    private String ItemQuantity;
    private String ItemQuantityPerPack;
    private String ItemUnit;
    private String ItemImage;
    private String UserID;
    private String ItemDescription;

    public Order() {

    }

    public Order(String itemID, String itemName, String itemCategory, String itemPrice, String itemQuantity, String itemQuantityPerPack, String itemUnit, String itemImage, String userID, String itemDescription) {
        ItemID = itemID;
        ItemName = itemName;
        ItemCategory = itemCategory;
        ItemPrice = itemPrice;
        ItemQuantity = itemQuantity;
        ItemQuantityPerPack = itemQuantityPerPack;
        ItemUnit = itemUnit;
        ItemImage = itemImage;
        UserID = userID;
        ItemDescription = itemDescription;
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

    public String getItemQuantityPerPack() {
        return ItemQuantityPerPack;
    }

    public void setItemQuantityPerPack(String itemQuantityPerPack) {
        ItemQuantityPerPack = itemQuantityPerPack;
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

    public String getItemDescription() {
        return ItemDescription;
    }

    public void setItemDescription(String itemDescription) {
        ItemDescription = itemDescription;
    }
}
