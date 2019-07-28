package com.zeeshan.foodjar.entities;

import java.util.List;

public class OrderRequest {

    private String orderID;
    private String userID;
    private List<Order> orderList;
    private String totalAmount;
    private String totalVAT;
    private String orderStatus;
    private String assignTo;
    private String itemCount;

    public OrderRequest(){
    }

    public OrderRequest(String orderID, String userID, List<Order> orderList,String totalVAT, String totalAmount,  String orderStatus, String assignTo, String itemCount) {
        this.orderID = orderID;
        this.userID = userID;
        this.orderList = orderList;
        this.totalVAT = totalVAT;
        this.totalAmount = totalAmount;
        this.orderStatus = orderStatus;
        this.assignTo = assignTo;
        this.itemCount = itemCount;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getTotalVAT() {
        return totalVAT;
    }

    public void setTotalVAT(String totalVAT) {
        this.totalVAT = totalVAT;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getAssignTo() {
        return assignTo;
    }

    public void setAssignTo(String assignTo) {
        this.assignTo = assignTo;
    }

    public String getItemCount() {
        return itemCount;
    }

    public void setItemCount(String itemCount) {
        this.itemCount = itemCount;
    }
}
