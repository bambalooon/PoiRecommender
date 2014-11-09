package pl.edu.agh.eis.poirecommender.openstreetmap.model.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Krzysztof Balon on 2014-11-10.
 */
public class Tags {
    @SerializedName("name")
    private final String name;

    @SerializedName("layer") //FIXME: int?
    private final String layer;
    @SerializedName("opening_hours")
    private final String openingHours;
    @SerializedName("phone")
    private final String phone;
    @SerializedName("website")
    private final String website;
    @SerializedName("operator")
    private final String operator;

    @SerializedName("amenity")
    private final String amenity;
    @SerializedName("tourism")
    private final String tourism;
    @SerializedName("shop")
    private final String shop;


    @SerializedName("addr:city")
    private final String city;
    @SerializedName("addr:country")
    private final String country;
    @SerializedName("addr:street")
    private final String street;
    @SerializedName("addr:housenumber")
    private final String houseNumber;
    @SerializedName("addr:housename")
    private final String houseName;
    @SerializedName("addr:postcode")
    private final String postCode;

    public Tags(String name, String layer, String openingHours, String phone, String website, String operator, String amenity, String tourism, String shop, String city, String country, String street, String houseNumber, String houseName, String postCode) {
        this.name = name;
        this.layer = layer;
        this.openingHours = openingHours;
        this.phone = phone;
        this.website = website;
        this.operator = operator;
        this.amenity = amenity;
        this.tourism = tourism;
        this.shop = shop;
        this.city = city;
        this.country = country;
        this.street = street;
        this.houseNumber = houseNumber;
        this.houseName = houseName;
        this.postCode = postCode;
    }
}
