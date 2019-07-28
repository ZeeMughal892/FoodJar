package com.zeeshan.foodjar.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.zeeshan.foodjar.entities.Order;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteAssetHelper {

    private static final String DB_NAME = "FoodJarDB.db";
    private static final int DB_VER = 1;

    public Database(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    public void addToCart(Order order) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT INTO CartItems(ItemID,ItemName,ItemCategory,ItemPrice,ItemQuantity, ItemQuantityPerPack,ItemUnit,ItemImage,UserID,ItemDescription) VALUES('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s')",
                order.getItemID(),
                order.getItemName(),
                order.getItemCategory(),
                order.getItemPrice(),
                order.getItemQuantity(),
                order.getItemQuantityPerPack(),
                order.getItemUnit(),
                order.getItemImage(),
                order.getUserID(),
                order.getItemDescription());
        db.execSQL(query);
    }

    public void cleanCart() {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM CartItems");
        db.execSQL(query);
    }

    public List<Order> getCarts(String userID) {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"ItemID", "ItemName", "ItemCategory", "ItemPrice", "ItemQuantity", "ItemQuantityPerPack", "ItemUnit", "ItemImage", "UserID", "ItemDescription"};
        String where = "UserID =?";
        String[] selection_Args = {userID};
        String sqlTable = "CartItems";

        qb.setTables(sqlTable);
        Cursor c = qb.query(db, sqlSelect, where, selection_Args, null, null, null);

        final List<Order> orderList = new ArrayList<>();
        if (c.getCount() >= 1 && c.moveToFirst()) {
            do {
                orderList.add(new Order(c.getString(c.getColumnIndex("ItemID")),
                        c.getString(c.getColumnIndex("ItemName")),
                        c.getString(c.getColumnIndex("ItemCategory")),
                        c.getString(c.getColumnIndex("ItemPrice")),
                        c.getString(c.getColumnIndex("ItemQuantity")),
                        c.getString(c.getColumnIndex("ItemQuantityPerPack")),
                        c.getString(c.getColumnIndex("ItemUnit")),
                        c.getString(c.getColumnIndex("ItemImage")),
                        c.getString(c.getColumnIndex("UserID")),
                        c.getString(c.getColumnIndex("ItemDescription"))
                ));
            } while (c.moveToNext());
        }
        return orderList;
    }

    public void removeFromCart(String itemID, String uid) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM CartItems WHERE UserID='%s' and ItemID='%s' ", uid, itemID);
        db.execSQL(query);
    }
}
