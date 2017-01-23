package com.example.hadad.towme.Tables;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;
import com.example.hadad.towme.Others.Constants;

/**
 * Created by Omer on 16-Jan-17.
 */


@DynamoDBTable(tableName = Constants.User_TABLE_NAME )
public  class User {
    private Long Id;
    private String firstName;
    private String lastName;
    private String Mail;
    private Long Telephone;
    private int carWeight;
    private String carType;
    private float x;
    private float y;

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }


//    private ArrayList<Tow> TowsAvailable;
    //   private ArrayList<Tow> HistoryTows;
    //  private bitmap Picture;


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
    public Long getTelephone() {
        return Telephone;
    }

    public void setTelephone(Long Telephone) {
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

//    Tow getLastToe() {
//        Tow tow = new Tow();
//        return tow;
//    }
//
//    ArrayList<Tow> getPreviousToes() {
//        //query to get prevous tows
//        return HistoryTows;
//    }

//    void TowHelped(Tow tow) {
//    }//Add Tow to the history
//
//    ArrayList<Tow> getClosestTows(User user, int rad) {
//        //query to get closest tow's
//        return TowsAvailable;
//    }
//
//    ArrayList<Tow> getCheapestTows(User user) {
//        //query to get cheapest tow
//        ArrayList<Tow> CheapestTows = new ArrayList<Tow>();
//        return CheapestTows;
//    }

}

