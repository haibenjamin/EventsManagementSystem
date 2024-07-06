package com.example.client.Model;

public class SugVendor {
    private String _id,email,businessName,businessType;
    private int leadCount,priceForService;

    public SugVendor(String email, String businessName, String businessType) {
        this.email = email;
        this.businessName = businessName;
        this.businessType = businessType;
    }

    public String get_id() {
        return _id;
    }

    public int getPriceForService() {
        return priceForService;
    }

    public void setPriceForService(int priceForService) {
        this.priceForService = priceForService;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public int getLeadCount() {
        return leadCount;
    }

    public void setLeadCount(int leadCount) {
        this.leadCount = leadCount;
    }

    @Override
    public String toString() {
        return "SugVendor{" +
                "_id='" + _id + '\'' +
                ", email='" + email + '\'' +
                ", businessName='" + businessName + '\'' +
                ", businessType='" + businessType + '\'' +
                ", leadCount=" + leadCount +
                '}';
    }

}
