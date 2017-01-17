package com.example.hadad.towme.DynamoDB;

import android.os.AsyncTask;

import com.example.hadad.towme.Others.Constants;

/**
 * Created by Omer on 16-Jan-17.
 */

public class DynamoDBManagerTaskTransaction extends
        AsyncTask<Constants.DynamoDBManagerType, Void, DynamoDBManagerTaskResult> {
    protected DynamoDBManagerTaskResult doInBackground(Constants.DynamoDBManagerType... types) {
        DynamoDBManagerTaskResult result = new DynamoDBManagerTaskResult();
        result.setTaskType(types[0]);
        int id=4; // here insert from main activity the id for the query
        if(types[0] == Constants.DynamoDBManagerType.ADD_TRANSACTION){
            DynamoDBManager.insertTransaction(9);
        }
        return result;
    }
}