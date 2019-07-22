package com.zeeshan.foodjaradmin.entities;

public class DeliveryBoy {
    private String boyID;
    private String boyName;
    private String boyPassword;
    private String boyPhone;

    public DeliveryBoy() {
    }

    public DeliveryBoy(String boyID, String boyName, String boyPassword, String boyPhone) {
        this.boyID = boyID;
        this.boyName = boyName;
        this.boyPassword = boyPassword;
        this.boyPhone = boyPhone;
    }

    public String getBoyPassword() {
        return boyPassword;
    }

    public void setBoyPassword(String boyPassword) {
        this.boyPassword = boyPassword;
    }

    public String getBoyID() {
        return boyID;
    }

    public void setBoyID(String boyID) {
        this.boyID = boyID;
    }

    public String getBoyName() {
        return boyName;
    }

    public void setBoyName(String boyName) {
        this.boyName = boyName;
    }

    public String getBoyPhone() {
        return boyPhone;
    }

    public void setBoyPhone(String boyPhone) {
        this.boyPhone = boyPhone;
    }
}
