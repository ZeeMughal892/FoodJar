package com.zeeshan.foodjar.entities;

public class Products {

    private String ItemId;
    private String ItemName;
    private String ItemCategory;
    private String ItemStock;
    private String ItemUnit;
    private String ItemPrice;
    private String ItemImage;
    private String ItemDescription;

    public Products() {
    }

    public Products(String itemId, String itemName, String itemCategory, String itemStock, String itemUnit, String itemPrice, String itemImage, String itemDescription) {
        ItemId = itemId;
        ItemName = itemName;
        ItemCategory = itemCategory;
        ItemStock = itemStock;
        ItemUnit = itemUnit;
        ItemPrice = itemPrice;
        ItemImage = itemImage;
        ItemDescription = itemDescription;
    }

    public String getItemId() {
        return ItemId;
    }

    public void setItemId(String itemId) {
        ItemId = itemId;
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

    public String getItemStock() {
        return ItemStock;
    }

    public void setItemStock(String itemStock) {
        ItemStock = itemStock;
    }

    public String getItemUnit() {
        return ItemUnit;
    }

    public void setItemUnit(String itemUnit) {
        ItemUnit = itemUnit;
    }

    public String getItemPrice() {
        return ItemPrice;
    }

    public void setItemPrice(String itemPrice) {
        ItemPrice = itemPrice;
    }

    public String getItemImage() {
        return ItemImage;
    }

    public void setItemImage(String itemImage) {
        ItemImage = itemImage;
    }

    public String getItemDescription() {
        return ItemDescription;
    }

    public void setItemDescription(String itemDescription) {
        ItemDescription = itemDescription;
    }
}
