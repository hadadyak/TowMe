package com.example.hadad.towme.DynamoDB;

import android.os.AsyncTask;
import android.util.Log;
import com.example.hadad.towme.Tables.*;
import com.example.hadad.towme.Others.*;


import java.util.ArrayList;

/**
 * Created by Omer on 16-Jan-17.
 */


public class DynamoDBManagerTaskTow extends
        AsyncTask<Constants.DynamoDBManagerType, Void, DynamoDBManagerTaskResult> {
    public DynamoDBManagerTaskResult doInBackground(
            Constants.DynamoDBManagerType... types) {
        ArrayList<Tow> items = new ArrayList<Tow>();
        DynamoDBManagerTaskResult result = new DynamoDBManagerTaskResult();
        result.setTaskType(types[0]); //define the state
        if(types[0] == Constants.DynamoDBManagerType.LIST_TOW) {
            items = DynamoDBManager.getTowList();
            Log.d("asynctaskToew","after getTowList");
            for (Tow tow : items) {
                System.out.println(" " + tow.getFirstName() + " " + tow.getId());
            }
        }
        return result;

    }
}