package com.example.hadad.towme.DynamoDB;

import android.util.Log;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.example.hadad.towme.Tables.*;
import com.example.hadad.towme.Others.*;
import com.example.hadad.towme.Activities.*;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBScanExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedScanList;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.DeleteTableRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeTableRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeTableResult;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.google.android.gms.plus.People;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static java.util.Comparator.comparing;

/**
 * Created by Omer on 16-Jan-17.
 */


public class DynamoDBManager {
    private static final String TAG = "DynamoDBManager";
    private static int idCounter = 1;

    public static void createTable() {
        AmazonDynamoDBClient ddb = SplashActivity.clientManager.ddb();
        KeySchemaElement kse = new KeySchemaElement().withAttributeName(
                "Id").withKeyType(KeyType.HASH);
        AttributeDefinition ad = new AttributeDefinition().withAttributeName(
                "Id").withAttributeType(ScalarAttributeType.N);
        ProvisionedThroughput pt = new ProvisionedThroughput()
                .withReadCapacityUnits(10l).withWriteCapacityUnits(5l);

        CreateTableRequest request = new CreateTableRequest()
                .withTableName(Constants.User_TABLE_NAME)
                .withKeySchema(kse).withAttributeDefinitions(ad)
                .withProvisionedThroughput(pt);

        try {
            Log.d(TAG, "Sending Create table request");
            ddb.createTable(request);
            Log.d(TAG, "Create request response successfully recieved");
        } catch (AmazonServiceException ex) {
            Log.e(TAG, "Error sending create table request", ex);
            SplashActivity.clientManager
                    .wipeCredentialsOnAuthError(ex);
        }
    }

    /*
       * Retrieves the table description and returns the table status Telephoneas a string.
       */
    public static String getTestTableStatus() {

        try {
            AmazonDynamoDBClient ddb = SplashActivity.clientManager.ddb();

            DescribeTableRequest request = new DescribeTableRequest()
                    .withTableName(Constants.User_TABLE_NAME);
            DescribeTableResult result = ddb.describeTable(request);

            String status = result.getTable().getTableStatus();
            return status == null ? "" : status;

        } catch (ResourceNotFoundException e) {
        } catch (AmazonServiceException ex) {
            SplashActivity.clientManager
                    .wipeCredentialsOnAuthError(ex);
        }

        return "";
    }

    /*
        * Inserts ten users with userNo from 1 to 10 and random names.
        */
    public static void insertUsers(User user) {
        AmazonDynamoDBClient ddb = SplashActivity.clientManager.ddb();
        DynamoDBMapper mapper = new DynamoDBMapper(ddb);
        try {

            //------> detailes that should come from the facebook login activity
            Log.d(TAG, "Inserting user");
            mapper.save(user);
            Log.d(TAG, "User was inserted");
        } catch (AmazonServiceException ex) {
            Log.e(TAG, "Error inserting users");
            SplashActivity.clientManager.wipeCredentialsOnAuthError(ex);
        }
    }


    public static ArrayList<Tow> OrderByRank(){
        ArrayList<Tow> tows = getTowList();
               /* Sorting of arraylist using Collections.sort*/
        Collections.sort(tows, new RankeComparator());
        return tows;
    }
    public static ArrayList<Tow> SortByRank(ArrayList<Tow> tows){
        Collections.sort(tows, new RankeComparator());
        return tows;
    }
    public static ArrayList<Tow> SortByPrice(ArrayList<Tow> tows){
        Collections.sort(tows, new PriceComparator());
        return tows;
    }


    /*
       * Scans the table and returns the list of tow.
       */
    public static ArrayList<Tow> getTowList() {
        AmazonDynamoDBClient ddb = SplashActivity.clientManager.ddb();
        DynamoDBMapper mapper2 = new DynamoDBMapper(ddb);
        DynamoDBScanExpression scanExpression2 = new DynamoDBScanExpression();
        try {
            PaginatedScanList<Tow> result = mapper2.scan(Tow.class, scanExpression2);
            ArrayList<Tow> resultList = new ArrayList<Tow>();
            for (Tow up : result) {
                resultList.add(up);
            }
            return resultList;
        } catch (AmazonServiceException ex) {
            SplashActivity.clientManager.wipeCredentialsOnAuthError(ex);
        }
        return null;
    }

    public static ArrayList<Comment> getCommentListById(Long id) {
        AmazonDynamoDBClient ddb = SplashActivity.clientManager.ddb();
        DynamoDBMapper mapper2 = new DynamoDBMapper(ddb);
//        String active="yes";
        Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        eav.put(":val1", new AttributeValue().withN(id+""));
        DynamoDBScanExpression scanExpression2 = new DynamoDBScanExpression()
                .withFilterExpression("TowId = :val1 ")
                .withExpressionAttributeValues(eav);
        try {
            PaginatedScanList<Comment> result2 = mapper2.scan(Comment.class, scanExpression2);

            ArrayList<Comment> resultList2 = new ArrayList<Comment>();

            resultList2.addAll(result2);
            return resultList2;


        } catch (AmazonServiceException ex) {
            SplashActivity.clientManager.wipeCredentialsOnAuthError(ex);
        }

        return null;
    }
    /*
    * Retrieves all of the attribute/value pairs for the specified user.
    */
    public static User getUser(int id) {

        AmazonDynamoDBClient ddb = SplashActivity.clientManager.ddb();
        DynamoDBMapper mapper = new DynamoDBMapper(ddb);

        try {
            User user = mapper.load(User.class, id);

            return user;

        } catch (AmazonServiceException ex) {
            SplashActivity.clientManager.wipeCredentialsOnAuthError(ex);
        }

        return null;
    }

    public static void insertTransaction(int id) {
        AmazonDynamoDBClient ddb = SplashActivity.clientManager
                .ddb();
        DynamoDBMapper mapper = new DynamoDBMapper(ddb);

        try {
            Transaction transaction = new Transaction();
            if (id == 0)
                id = 10; //default if there is no id
            transaction.setId(id);
            transaction.setPrice(100); //just default value
            Log.d(TAG, "Inserting transaction");
            mapper.save(transaction);
            Log.d(TAG, "transaction inserted");

        } catch (AmazonServiceException ex) {
            Log.e(TAG, "Error inserting transaction");
            SplashActivity.clientManager.wipeCredentialsOnAuthError(ex);
        }
    }

    public static User getUserByID(long id) {
        AmazonDynamoDBClient ddb = SplashActivity.clientManager.ddb();
        DynamoDBMapper mapper = new DynamoDBMapper(ddb);
        // DynamoDBMapperConfig config=new DynamoDBMapperConfig();
        User user = new User();
        user.setId(id);
        User us = mapper.load(user);
        return us;
    }

    /*
* Retrieves all of the attribute/value pairs for the specified user.
*/
    public static Comment getComment(int id) {

        AmazonDynamoDBClient ddb = SplashActivity.clientManager.ddb();
        DynamoDBMapper mapper = new DynamoDBMapper(ddb);

        try {
            Comment comment = mapper.load(Comment.class, id);

            return comment;

        } catch (AmazonServiceException ex) {
            SplashActivity.clientManager.wipeCredentialsOnAuthError(ex);
        }

        return null;
    }

    public static void insertTow(Tow tow) {
        AmazonDynamoDBClient ddb = SplashActivity.clientManager.ddb();
        DynamoDBMapper mapper = new DynamoDBMapper(ddb);
        try {

            //------> detailes that should come from the facebook login activity
            Log.d(TAG, "Inserting tow");
            mapper.save(tow);
            Log.d(TAG, "tow was inserted");
        } catch (AmazonServiceException ex) {
            Log.e(TAG, "Error inserting tow");
            SplashActivity.clientManager.wipeCredentialsOnAuthError(ex);
        }
    }

    /*
 * Inserts ten users with userNo from 1 to 10 and random names.
 */
    public static void insertComment(Comment comm) {
        AmazonDynamoDBClient ddb = SplashActivity.clientManager
                .ddb();
        DynamoDBMapper mapper = new DynamoDBMapper(ddb);

        try {

            mapper.save(comm);

        } catch (AmazonServiceException ex) {
            Log.e(TAG, "Error inserting comment");
            SplashActivity.clientManager.wipeCredentialsOnAuthError(ex);
        }
    }

    //Scan with user name of patient -> find ID8888888888888888
    public static ArrayList<Tow> geActiveTows() {
        AmazonDynamoDBClient ddb = SplashActivity.clientManager.ddb();
        DynamoDBMapper mapper2 = new DynamoDBMapper(ddb);
        String active="yes";
        //------search and save only with same userFacebook ----------
        Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        eav.put(":val1", new AttributeValue().withS(active));
        DynamoDBScanExpression scanExpression2 = new DynamoDBScanExpression()
                .withFilterExpression("Active = :val1 ")
                .withExpressionAttributeValues(eav);
        try {
            PaginatedScanList<Tow> result2 = mapper2.scan(
                    Tow.class, scanExpression2);

            ArrayList<Tow> resultList2 = new ArrayList<Tow>();
            resultList2.addAll(result2);
//            for (Tow up : result2) {
//
//                resultList2.add(up);
//            }
            return resultList2;

        } catch (AmazonServiceException ex) {
            SplashActivity.clientManager.wipeCredentialsOnAuthError(ex);
        }

        return null;
    }

    public static Tow getTowByID(long id) {
        AmazonDynamoDBClient ddb = SplashActivity.clientManager.ddb();
        DynamoDBMapper mapper = new DynamoDBMapper(ddb);
        // DynamoDBMapperConfig config=new DynamoDBMapperConfig();
        Tow tow= new Tow(id);
        Tow us = mapper.load(tow);
        return us;
    }

    /*
    * Updates one attribute/value pair for the specified user.
    */
    public static void updateUser(User updateUser) {

        AmazonDynamoDBClient ddb = SplashActivity.clientManager.ddb();
        DynamoDBMapper mapper = new DynamoDBMapper(ddb);

        try {
            mapper.save(updateUser);

        } catch (AmazonServiceException ex) {
            SplashActivity.clientManager.wipeCredentialsOnAuthError(ex);
        }
    }

    /*
   * Deletes the specified user and all of its attribute/value pairs.
   */
    public static void deleteUser(User deleteUser) {

        AmazonDynamoDBClient ddb = SplashActivity.clientManager.ddb();
        DynamoDBMapper mapper = new DynamoDBMapper(ddb);

        try {
            mapper.delete(deleteUser);

        } catch (AmazonServiceException ex) {
            SplashActivity.clientManager.wipeCredentialsOnAuthError(ex);
        }
    }

    /*
  * Deletes the test table and all of its users and their attribute/value
  * pairs.
  */
    public static void cleanUp() {

        AmazonDynamoDBClient ddb = SplashActivity.clientManager
                .ddb();

        DeleteTableRequest request = new DeleteTableRequest().withTableName(Constants.User_TABLE_NAME);
        try {
            ddb.deleteTable(request);

        } catch (AmazonServiceException ex) {
            SplashActivity.clientManager.wipeCredentialsOnAuthError(ex);
        }
    }
}
