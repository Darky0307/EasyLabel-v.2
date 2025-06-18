package com.easylabel.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressData {

    @JsonProperty("name2")
    private String firstName;

    @JsonProperty("name3")
    private String lastName;

    @JsonProperty("address1")
    private String street1;

    @JsonProperty("address2")
    private String street2;

    @JsonProperty("postalCode")
    private String postalCode;

    @JsonProperty("town")
    private String city;

    @JsonProperty("countryId")
    private int countryId;

    // wird mit initCountryMap() befüllt
    private String countryName;

    // --- Getter / Setter ---

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getStreet1() { return street1; }
    public void setStreet1(String street1) { this.street1 = street1; }

    public String getStreet2() { return street2; }
    public void setStreet2(String street2) { this.street2 = street2; }

    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public int getCountryId() { return countryId; }
    public void setCountryId(int countryId) { this.countryId = countryId; }

    public String getCountryName() { return countryName; }
    public void setCountryName(String countryName) { this.countryName = countryName; }
}
