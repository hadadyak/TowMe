package com.example.hadad.towme.Tables;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;
import com.example.hadad.towme.Others.Constants;

/**
 * Created by Omer on 16-Jan-17.
 */

@DynamoDBTable(tableName = Constants.Comment_TABLE)
public class Comment {
    private int Id;
    private int TowId;
    private String Author;
    private String Comment;
    private int Date;

    @DynamoDBHashKey(attributeName = "Id") //primary key
    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }
    @DynamoDBAttribute(attributeName ="TowId")
    public int getTowId() {
        return TowId;
    }

    public void setTowId(int towId) {
        TowId = towId;
    }
    @DynamoDBAttribute(attributeName ="Author")
    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        Author = author;
    }
    @DynamoDBAttribute(attributeName ="Comment")
    public String getComment() {
        return Comment;
    }
    public void setComment(String comment) {
        Comment = comment;
    }
    @DynamoDBAttribute(attributeName ="Date")
    public int getDate() {
        return Date;
    }

    public void setDate(int date) {
        Date = date;
    }


}
