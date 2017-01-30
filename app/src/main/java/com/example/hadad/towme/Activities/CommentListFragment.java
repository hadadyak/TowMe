package com.example.hadad.towme.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hadad.towme.DynamoDB.DynamoDBManagerTask;
import com.example.hadad.towme.DynamoDB.MyQuery;
import com.example.hadad.towme.Others.CommentsTable;
import com.example.hadad.towme.Others.Constants;
import com.example.hadad.towme.Others.MyTowItemRecyclerViewAdapter;
import com.example.hadad.towme.R;
import com.example.hadad.towme.Others.MyCommentItemRecyclerViewAdapter;
import com.example.hadad.towme.Tables.Comment;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnCommentListFragmentInteractionListener}
 * interface.
 */
public class CommentListFragment extends Fragment implements DynamoDBManagerTask.DynamoDBManagerTaskResponse {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String TOW_ID = "tow-id";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private Long mTowId;
    private OnCommentListFragmentInteractionListener mListener;
    private MyCommentItemRecyclerViewAdapter recAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CommentListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static CommentListFragment newInstance(int columnCount, Long tid) {
        CommentListFragment fragment = new CommentListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        args.putLong(TOW_ID,tid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            mTowId = getArguments().getLong(TOW_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recAdapter = new MyCommentItemRecyclerViewAdapter(CommentsTable.getComments(mTowId), mListener);
            recyclerView.setAdapter(recAdapter);
        }
        DynamoDBManagerTask getCommentTask = new DynamoDBManagerTask(this);
        MyQuery<Comment> getCommentQ = new MyQuery<>(Constants.DynamoDBManagerType.LIST_COMMENTS,new Comment(mTowId,null,null));
        getCommentTask.execute(getCommentQ);
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCommentListFragmentInteractionListener) {
            mListener = (OnCommentListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void DynamoDBManagerTaskResponse(MyQuery myQ) {
        if(myQ.getType() == Constants.DynamoDBManagerType.LIST_COMMENTS_RES) {
            CommentsTable.hardCopy(mTowId,(ArrayList<Comment>)(myQ.getContent()));
            recAdapter.notifyDataSetChanged();
        }
    }

    public void notifyList(){
        recAdapter.notifyDataSetChanged();
    }

    public interface OnCommentListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Comment item);
        Context getContextListener();
    }
}
