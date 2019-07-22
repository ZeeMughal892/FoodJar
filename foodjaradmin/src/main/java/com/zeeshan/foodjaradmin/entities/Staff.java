package com.zeeshan.foodjaradmin.entities;

public class Staff {
    private String staffID;
    private String staffName;
    private String staffPassword;
    private String staffPhoneNo;
    private String role;

    public Staff(){}

    public Staff(String staffID, String staffName, String staffPassword, String staffPhoneNo, String role) {
        this.staffID = staffID;
        this.staffName = staffName;
        this.staffPassword = staffPassword;
        this.staffPhoneNo = staffPhoneNo;
        this.role = role;
    }

    public String getStaffID() {
        return staffID;
    }

    public void setStaffID(String staffID) {
        this.staffID = staffID;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getStaffPassword() {
        return staffPassword;
    }

    public void setStaffPassword(String staffPassword) {
        this.staffPassword = staffPassword;
    }

    public String getStaffPhoneNo() {
        return staffPhoneNo;
    }

    public void setStaffPhoneNo(String staffPhoneNo) {
        this.staffPhoneNo = staffPhoneNo;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
