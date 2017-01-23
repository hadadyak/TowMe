package com.example.hadad.towme.Tables;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;
import com.example.hadad.towme.Others.Constants;

import java.util.ArrayList;

/**
 * Created by Omer on 16-Jan-17.
 */


@DynamoDBTable(tableName = Constants.TOW_TABLE_NAME )
public  class Tow  {
    private int Id;
    private float Rank;
    private double PricePerKM;
    private double PricePerWeight;
    private int Customers;
    private int startWorkingTime;
    private int EndWorkingTime;
    private String firstName;
    private String lastName;
    private String Mail;
    private float x;
    private float y;
    String PicUrl;
    @DynamoDBAttribute(attributeName ="PicUrl")
    public String getPicUrl() {
        return PicUrl;
    }

    public void setPicUrl(String picUrl) {
        PicUrl = picUrl;
    }

    @DynamoDBAttribute(attributeName ="x")
    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }
    @DynamoDBAttribute(attributeName ="y")
    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }



//    private int Telephone;
    //bitmap Picture;
    //  ArrayList<Coment> Comments;

    @DynamoDBHashKey(attributeName = "Id") //primary key
    public int getId() {
        return Id;
    }

    //1-5 like stars
    @DynamoDBAttribute(attributeName ="Rank")
    public float getRank() {
        return Rank;
    }



    @DynamoDBAttribute(attributeName ="Mail")
    public String getMail() {
        return Mail;
    }

    @DynamoDBAttribute(attributeName ="PricePerKM")
    public double getPricePerKM() {
        return PricePerKM;
    }

    @DynamoDBAttribute(attributeName ="PricePerWeight")
    public double getPricePerWeight() {
        return PricePerWeight;
    }

    @DynamoDBAttribute(attributeName ="Customers")
    public int getCustomers() {
        return Customers;
    }

    @DynamoDBAttribute(attributeName ="startWorkingTime")
    public int getStartWorkingTime() {
        return startWorkingTime;
    }

    @DynamoDBAttribute(attributeName ="EndWorkingTime")
    public int getEndWorkingTime() {
        return EndWorkingTime;
    }

    @DynamoDBAttribute(attributeName ="firstName")
    public String getFirstName() {
        return firstName;
    }

    @DynamoDBAttribute(attributeName ="lastName")
    public String getLastName() {
        return lastName;
    }

    public void setCustomers(int customers) {
        Customers = customers;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void setPricePerWeight(double pricePerWeight) {
        PricePerWeight = pricePerWeight;
    }
    public void setEndWorkingTime(int endWorkingTime) {
        EndWorkingTime = endWorkingTime;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public void setMail(String mail) {
        Mail = mail;
    }
    public void setRank(float rank) {
        Rank = rank;
    }
    public void setId(int id) {
        Id = id;
    }
    public void setStartWorkingTime(int startWorkingTime) {this.startWorkingTime = startWorkingTime;}
    public void setPricePerKM(double pricePerKM) {
        PricePerKM = pricePerKM;
    }


//    @DynamoDBAttribute(attributeName ="Telephone")
//    public int getTelephone() {
//        return Telephone;
//    }
//    public void setTelephone(int telephone) {
//        Telephone = telephone;
//    }

    //Return all the tows had used
    ArrayList<Tow> getPreviousToes(){
        ArrayList<Tow> PreviousToes=new ArrayList<Tow>();
        //query to get previous tow
        return PreviousToes;
    }
    void TowHelped(Tow tow){
        //Add Tow to the history
    }
}
