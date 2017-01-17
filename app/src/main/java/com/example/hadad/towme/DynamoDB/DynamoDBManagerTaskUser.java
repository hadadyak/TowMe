package com.example.hadad.towme.DynamoDB;

import android.os.AsyncTask;
import android.util.Log;

import com.example.hadad.towme.Tables.*;
import com.example.hadad.towme.Others.*;


/**
 * Created by Omer on 16-Jan-17.
 */


public class DynamoDBManagerTaskUser extends
        AsyncTask<Constants.DynamoDBManagerType, Void, DynamoDBManagerTaskResult> {

    public DynamoDBManagerTaskResult doInBackground(
            Constants.DynamoDBManagerType... types) {
        String tableStatus = DynamoDBManager.getTestTableStatus();
        User newuser=new User();
        DynamoDBManagerTaskResult result = new DynamoDBManagerTaskResult();
        result.setTaskType(types[0]);
        result.setFirstName(newuser.getFirstName());
        result.setLastName(newuser.getLastName());
        result.setMail(newuser.getMail());
        result.setTelephone(newuser.getTelephone());

        if (types[0] == Constants.DynamoDBManagerType.INSERT_USER) {
            if (tableStatus.equalsIgnoreCase("ACTIVE")) {
                DynamoDBManager.insertUsers(result.getFirstName(), result.getLastName(), result.getMail(), result.getTelephone());
            }
        }
        else if(types[0] ==Constants.DynamoDBManagerType.GET_USER_BY_NAME){
            DynamoDBManager.getUserByName(null);

        }
        else
            Log.d("DynamoDBManagerTask","failure at ");


        return result;
    }

    protected void onPostExecute(DynamoDBManagerTaskResult result) {
//         if (!result.getTableStatus().equalsIgnoreCase("ACTIVE")) {
//            Log.d("DynamoDBManager", "The test table status is not active.");
//        } else if (result.getTableStatus().equalsIgnoreCase("ACTIVE")
//                && result.getTaskType() == MainActivity.DynamoDBManagerType.INSERT_USER) {
//            Log.d("DynamoDBManager", "Users inserted successfully!");
//        }

    }
}