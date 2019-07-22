package com.zeeshan.foodjaradmin.entities;

public class User{
        private String userID;
        private String userName;
        private String password;
        private String role;
        private String shopName;
        private String phoneNumber;
        private String address;
        private String referredBy;

        public User() {
        }

        public User(String userID, String userName, String password, String role, String shopName, String phoneNumber, String address, String referredBy) {
            this.userID = userID;
            this.userName = userName;
            this.password = password;
            this.role = role;
            this.shopName = shopName;
            this.phoneNumber = phoneNumber;
            this.address = address;
            this.referredBy = referredBy;
        }

        public String getUserID() {
            return userID;
        }

        public void setUserID(String userID) {
            this.userID = userID;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getReferredBy() {
            return referredBy;
        }

        public void setReferredBy(String referredBy) {
            this.referredBy = referredBy;
        }
}
