package com.example.hadad.towme.DynamoDB;

import com.example.hadad.towme.Others.Constants;

/**
 * Created by Omer on 16-Jan-17.
 */


public class DynamoDBManagerTaskResult {
    private Constants.DynamoDBManagerType taskType;
    private String tableStatus;
    private String firstName;
    private String LastName;
    private String Mail;
    private Long Telephone;

    public void setFirstName(String firstName) {this.firstName = firstName;}
    public void setTelephone(Long telephone) {Telephone = telephone;}
    public void setLastName(String lastName) {LastName = lastName;}
    public void setMail(String mail) {Mail = mail;}
    public void setTaskType(Constants.DynamoDBManagerType taskType) {this.taskType = taskType;}
    public void setTableStatus(String tableStatus) {
        this.tableStatus = tableStatus;
    }


    public Long getTelephone() {return Telephone;}
    public String getTableStatus() {
        return tableStatus;
    }
    public String getMail() {return Mail;}
    public String getFirstName(){return firstName;}
    public String getLastName(){return LastName;}
    public Constants.DynamoDBManagerType getTaskType() {
        return taskType;
    }



}