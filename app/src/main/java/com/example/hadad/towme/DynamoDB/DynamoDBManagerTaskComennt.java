package com.example.hadad.towme.DynamoDB;

import android.os.AsyncTask;

import com.example.hadad.towme.DynamoDB.DynamoDBManagerTaskResult;
import com.example.hadad.towme.Others.Constants.DynamoDBManagerType;
/**
 * Created by Omer on 16-Jan-17.
 */

public class DynamoDBManagerTaskComennt extends
        AsyncTask<DynamoDBManagerType, Void, DynamoDBManagerTaskResult> {

    protected DynamoDBManagerTaskResult doInBackground(DynamoDBManagerType... types) {
        DynamoDBManagerTaskResult result = new DynamoDBManagerTaskResult();
        result.setTaskType(types[0]);
        int id=4; // here insert from main activity the id for the query
        if (types[0] == DynamoDBManagerType.LIST_COMMENTS)
            System.out.println(" "+ DynamoDBManager.getComment(id).getComment());
        else if(types[0] == DynamoDBManagerType.ADD_COMMENTS){
            DynamoDBManager.insertComment("omer comment",9);
        }
        return result;
    }

}