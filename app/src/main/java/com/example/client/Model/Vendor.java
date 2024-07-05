package com.example.client.Model;

public class Vendor {
    private CustomVendor custom;

    public CustomVendor getCustom() {
        return custom;
    }

    public void setCustom(CustomVendor custom) {
        this.custom = custom;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getPriceForService() {
        return priceForService;
    }

    public void setPriceForService(int priceForService) {
        this.priceForService = priceForService;
    }

    private String status;
    private int priceForService;

}
