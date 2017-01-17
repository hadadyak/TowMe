package com.example.hadad.towme.Tables;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;
import com.example.hadad.towme.Others.Constants;

/**
 * Created by Omer on 16-Jan-17.
 */


@DynamoDBTable(tableName = Constants.TRANSACTION_TABLE_NAME )
public class Transaction {
    private int Id;
    private int User_id;
    private int Tow_id;
    private int Date;
    private int Price;
    private int Location;
    private int Rank;
    @DynamoDBHashKey(attributeName = "Id") //primary key
    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }
    @DynamoDBAttribute(attributeName ="User_id")
    public int getUser_id() {
        return User_id;
    }

    public void setUser_id(int user_id) {
        User_id = user_id;
    }
    @DynamoDBAttribute(attributeName ="Tow_id")
    public int getTow_id() {
        return Tow_id;
    }

    public void setTow_id(int tow_id) {
        Tow_id = tow_id;
    }
    @DynamoDBAttribute(attributeName ="Date")
    public int getDate() {
        return Date;
    }

    public void setDate(int date) {
        Date = date;
    }
    @DynamoDBAttribute(attributeName ="Price")
    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        Price = price;
    }
    @DynamoDBAttribute(attributeName ="Location")
    public int getLocation() {
        return Location;
    }

    public void setLocation(int location) {
        Location = location;
    }
    @DynamoDBAttribute(attributeName ="Rank")
    public int getRank() {
        return Rank;
    }

    public void setRank(int rank) {
        Rank = rank;
    }


}