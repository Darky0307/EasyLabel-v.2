package com.easylabel.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Datenmodell für eine Bestellung (Order) aus PlentyMarkets,
 * inklusive des Länder-Namens zur UI-Auswahl.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderData {

    @JsonProperty("invoiceNumber")
    private String invoiceNumber;

    @JsonProperty("recipientName")
    private String recipientName;

    @JsonProperty("street")
    private String street;

    @JsonProperty("postalCode")
    private String postalCode;

    @JsonProperty("city")
    private String city;

    @JsonProperty("country")
    private String country;

    // Neu: Für den gemappten Länder-Namen aus PlentyApiClient
    private String countryName;

    @JsonProperty("invoiceNumber")
    public String getInvoiceNumber() { return invoiceNumber; }
    public void setInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; }

    @JsonProperty("recipientName")
    public String getRecipientName() { return recipientName; }
    public void setRecipientName(String recipientName) { this.recipientName = recipientName; }

    @JsonProperty("street")
    public String getStreet() { return street; }
    public void setStreet(String street) { this.street = street; }

    @JsonProperty("postalCode")
    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }

    @JsonProperty("city")
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getCountryName() { return countryName; }
    public void setCountryName(String countryName) { this.countryName = countryName; }
}

