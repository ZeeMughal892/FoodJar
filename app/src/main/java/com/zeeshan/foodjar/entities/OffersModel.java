package com.zeeshan.foodjar.entities;

public class OffersModel {
    String offerId;
    String offer;
    String lastUpdatedBy;

    public OffersModel() {
    }

    public OffersModel(String offerId, String offer, String lastUpdatedBy) {
        this.offerId = offerId;
        this.offer = offer;
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public String getOffer() {
        return offer;
    }

    public void setOffer(String offer) {
        this.offer = offer;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

}
