package com.example.hadad.towme.Others;

/**
 * Created by Omer on 16-Jan-17.
 */

public class Constants {
    public static final String IDENTITY_POOL_ID = "us-east-1:8d2b4765-7913-4d47-9fe2-50b1e69f829b";
    // Note that spaces are not allowed in the table name
    public static final String User_TABLE_NAME = "Users";
    public static final String TOW_TABLE_NAME = "Tow";
    public static final String Comment_TABLE = "Comments";
    public static final String TRANSACTION_TABLE_NAME = "Transactions";
    public static final String BUCKET_NAME = "s31234324";
    public static final String OBJECT_KEY="4my-TowMe";

    public enum MainActivityState{
        DIVIDED,
        MAP,
        LIST
    }

    public enum DynamoDBManagerType {
        ADD_TRANSACTION,
        GET_USER_BY_ID,
        USER_NON_EXIST,
        UPDATE_USER,
        INSERT_USER,
        LIST_TOW,
        DELETE_USER_TABLE,
        LIST_COMMENTS,
        LIST_COMMENTS_RES,
        ADD_COMMENTS,
        ADD_COMMENTS_RES,
        GET_TOW_BY_ID,
        UPDATE_TOW,
        GET_ACTIVE_TOWS,
        TOW_NON_EXIST

    }
}
