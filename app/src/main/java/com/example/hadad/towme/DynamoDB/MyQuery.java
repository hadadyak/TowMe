package com.example.hadad.towme.DynamoDB;

import com.example.hadad.towme.Others.Constants;
import com.example.hadad.towme.Tables.Comment;
import com.example.hadad.towme.Tables.Tow;

import java.util.ArrayList;

/**
 * Created by Omer on 20-Jan-17.
 */

public class MyQuery <T>{
    private Constants.DynamoDBManagerType type;
    private T Content;

    public MyQuery(Constants.DynamoDBManagerType type,T content){
        this.type = type;
        this.Content = content;
    }
    public Constants.DynamoDBManagerType getType() {
        return type;
    }

    public void setType(Constants.DynamoDBManagerType type) {
        this.type = type;
    }

    public  T getContent() {
        return Content;
    }

    public void setContent(T content) {
        Content = content;
    }



}
