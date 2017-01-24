package com.example.hadad.towme.DynamoDB;

import android.os.AsyncTask;

import com.example.hadad.towme.Others.Constants;
import com.example.hadad.towme.Tables.Comment;
import com.example.hadad.towme.Tables.Transaction;
import com.example.hadad.towme.Tables.User;

/**
 * Created by Omer on 20-Jan-17.
 */

public class DynamoDBManagerTask extends AsyncTask<MyQuery, Integer, MyQuery> {

    public interface DynamoDBManagerTaskResponse{
        public void DynamoDBManagerTaskResponse(MyQuery myQ);
    }

    public DynamoDBManagerTaskResponse mListener;
    public DynamoDBManagerTask(DynamoDBManagerTaskResponse listener){
        mListener = listener;
    }
    public MyQuery doInBackground(MyQuery... query) {
        Long id;
        switch (query[0].getType()) {
            case GET_USER_BY_ID:
                id = ((User) query[0].getContent()).getId();
                MyQuery<User> answer = new MyQuery(Constants.DynamoDBManagerType.GET_USER_BY_ID,DynamoDBManager.getUserByID(id));
                if(answer.getContent() == null){
                    query[0].setType(Constants.DynamoDBManagerType.USER_NON_EXIST);
                    return query[0];
                }
                return answer;
            case LIST_TOW:
                query[0].setContent(DynamoDBManager.getTowList());
                break; //fix that bee able to toch the list
            case INSERT_USER:
                DynamoDBManager.insertUsers((User) query[0].getContent());
                break;
            case ADD_COMMENTS:
                String comment = ((Comment) query[0].getContent()).getComment();
                int Id = ((Comment) query[0].getContent()).getId();
                DynamoDBManager.insertComment(comment, Id);
                break;
            case LIST_COMMENTS:
//                query[0].setComments(DynamoDBManager.getCommentList());
                break;
            case ADD_TRANSACTION:
                DynamoDBManager.insertTransaction(((Transaction) query[0].getContent()).getId());
                break;
            case DELETE_USER_TABLE:
                DynamoDBManager.cleanUp();
                break;
        }
        return query[0];
    }


    @Override
    protected void onPostExecute(MyQuery myQuery) {
        super.onPostExecute(myQuery);
        mListener.DynamoDBManagerTaskResponse(myQuery);
    }


}
