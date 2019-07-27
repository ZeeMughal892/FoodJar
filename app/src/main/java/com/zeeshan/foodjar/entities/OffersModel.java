package com.zeeshan.foodjar.entities;

public class OffersModel {
    String offerId;
    String offerTitle;
    String offerImage;
    String lastUpdatedBy;

    public OffersModel(){}

    public OffersModel(String offerId, String offerTitle, String offerImage, String lastUpdatedBy) {
        this.offerId = offerId;
        this.offerTitle = offerTitle;
        this.offerImage = offerImage;
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public String getOfferTitle() {
        return offerTitle;
    }

    public void setOfferTitle(String offerTitle) {
        this.offerTitle = offerTitle;
    }

    public String getOfferImage() {
        return offerImage;
    }

    public void setOfferImage(String offerImage) {
        this.offerImage = offerImage;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

}
