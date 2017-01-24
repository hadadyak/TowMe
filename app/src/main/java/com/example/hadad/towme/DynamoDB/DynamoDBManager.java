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
    public static ArrayList<Comment> getCommentList() {
        AmazonDynamoDBClient ddb = SplashActivity.clientManager.ddb();
        DynamoDBMapper mapper2 = new DynamoDBMapper(ddb);
        DynamoDBScanExpression scanExpression2 = new DynamoDBScanExpression();
        try {
            PaginatedScanList<Comment> result = mapper2.scan(Comment.class, scanExpression2);
            ArrayList<Comment> resultList = new ArrayList<Comment>();
            for (Comment up : result) {
                resultList.add(up);
            }
            return resultList;
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

    /*
 * Inserts ten users with userNo from 1 to 10 and random names.
 */
    public static void insertComment(String commentStr, int id) {
        AmazonDynamoDBClient ddb = SplashActivity.clientManager
                .ddb();
        DynamoDBMapper mapper = new DynamoDBMapper(ddb);

        try {

            Comment comment = new Comment();
            if (id == 0)
                id = 10;
            comment.setId(id);
            if (commentStr == null)
                commentStr = "exapmle comment";
            comment.setComment(commentStr);

            Log.d(TAG, "Inserting comment");
            mapper.save(comment);
            Log.d(TAG, "comment inserted");

        } catch (AmazonServiceException ex) {
            Log.e(TAG, "Error inserting comment");
            SplashActivity.clientManager.wipeCredentialsOnAuthError(ex);
        }
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
