package com.example.hadad.towme.DynamoDB;

import android.os.AsyncTask;
import android.util.Log;

import com.example.hadad.towme.Others.CommentsTable;
import com.example.hadad.towme.Others.Constants;
import com.example.hadad.towme.Tables.Comment;
import com.example.hadad.towme.Tables.Transaction;
import com.example.hadad.towme.Tables.User;

/**
 * Created by Omer on 20-Jan-17.
 */

public class DynamoDBManagerTask extends AsyncTask<MyQuery, Integer, MyQuery> {

    public interface DynamoDBManagerTaskResponse {
        public void DynamoDBManagerTaskResponse(MyQuery myQ);
    }

    public DynamoDBManagerTaskResponse mListener;

    public DynamoDBManagerTask(DynamoDBManagerTaskResponse listener) {
        mListener = listener;
    }

    public MyQuery doInBackground(MyQuery... query) {
        Long id;
        switch (query[0].getType()) {
            case GET_USER_BY_ID:
                id = ((User) query[0].getContent()).getId();
                MyQuery<User> answer = new MyQuery(Constants.DynamoDBManagerType.GET_USER_BY_ID, DynamoDBManager.getUserByID(id));
                if (answer.getContent() == null) {
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
                CommentsTable.addComment((Comment) query[0].getContent());
                DynamoDBManager.insertComment((Comment) query[0].getContent());
                return new MyQuery<Comment>(Constants.DynamoDBManagerType.ADD_COMMENTS_RES,null);
            case LIST_COMMENTS:
                return new MyQuery(Constants.DynamoDBManagerType.LIST_COMMENTS_RES,DynamoDBManager.getCommentListById(((Comment) query[0].getContent()).getTowId()));
            case ADD_TRANSACTION:
                DynamoDBManager.insertTransaction(((Transaction) query[0].getContent()).getId());
                break;
            case DELETE_USER_TABLE:
                DynamoDBManager.cleanUp();
                break;
            case UPDATE_USER:
                Log.d("DBTask","update user");
                DynamoDBManager.insertUsers((User) query[0].getContent());
//                DynamoDBManager.updateUser((User) query[0].getContent());
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
