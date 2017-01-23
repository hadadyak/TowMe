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
    private String Table;
    private int TableType;
    private T Content;

    public MyQuery(Constants.DynamoDBManagerType type){
        this.type = type;
    }
    public Constants.DynamoDBManagerType getType() {
        return type;
    }

    public void setType(Constants.DynamoDBManagerType type) {
        this.type = type;
    }

    public String getTable() {
        return Table;
    }

    public void setTable(String table) {
        Table = table;
    }

    public int getTableType() {
        return TableType;
    }

    public void setTableType(int tableType) {
        TableType = tableType;
    }

    public  T getContent() {
        return Content;
    }

    public void setContent(T content) {
        Content = content;
    }



}
