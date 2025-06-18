package com.easylabel.model;

import java.util.List;

public class ShipmentRequest {
    private String recipientName;
    private String street;
    private String postalCode;
    private String city;
    private String country;
    private double weight;
    private String service;
    private List<CustomsItem> customsItems;

    public String getRecipientName() { return recipientName; }
    public void setRecipientName(String recipientName) { this.recipientName = recipientName; }

    public String getStreet() { return street; }
    public void setStreet(String street) { this.street = street; }

    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }

    public String getService() { return service; }
    public void setService(String service) { this.service = service; }

    public List<CustomsItem> getCustomsItems() { return customsItems; }
    public void setCustomsItems(List<CustomsItem> customsItems) { this.customsItems = customsItems; }
}