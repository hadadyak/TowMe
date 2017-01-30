package com.example.hadad.towme.Tables;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;
import com.example.hadad.towme.Others.Constants;

/**
 * Created by Omer on 16-Jan-17.
 */


@DynamoDBTable(tableName = Constants.User_TABLE_NAME)
public class User {
    private Long Id;
    private String firstName;
    private String lastName;
    private String Mail;
    private String Telephone;
    private int carWeight;
    private String carType;
    private double Latitude;
    private double Longitude;
    private String PicUrl;

    public User() {
    }

    public User(Long id) {
        this.Id = id;
    }

    @DynamoDBAttribute(attributeName = "PicUrl")
    public String getPicUrl() {
        return this.PicUrl;
    }

    public void setPicUrl(String photo) {
        this.PicUrl = photo;
    }


    @DynamoDBAttribute(attributeName = "Latitude")
    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double Latitude) {
        this.Latitude = Latitude;
    }

    @DynamoDBAttribute(attributeName = "Longitude")
    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double Longitude) {
        this.Longitude = Longitude;
    }


    @DynamoDBHashKey(attributeName = "Id") //primary key
    public Long getId() {
        return Id;
    }

    public void setId(Long Id) {
        this.Id = Id;
    }

    @DynamoDBAttribute(attributeName = "carType")
    public String getcarType() {
        return carType;
    }

    public void setcarType(String carType) {
        this.carType = carType;
    }

    @DynamoDBAttribute(attributeName = "carWeight")
    public int getcarWeight() {
        return carWeight;
    }

    public void setcarWeight(int carWeight) {
        this.carWeight = carWeight;
    }

    @DynamoDBAttribute(attributeName = "Telephone")
    public String getTelephone() {
        return Telephone;
    }

    public void setTelephone(String Telephone) {
        this.Telephone = Telephone;
    }


    @DynamoDBAttribute(attributeName = "firstName")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @DynamoDBAttribute(attributeName = "lastName")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @DynamoDBAttribute(attributeName = "Mail")
    public String getMail() {
        return Mail;
    }

    public void setMail(String Mail) {
        this.Mail = Mail;
    }


}

