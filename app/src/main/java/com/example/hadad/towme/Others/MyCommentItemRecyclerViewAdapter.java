package com.example.hadad.towme.Others;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hadad.towme.Activities.CommentListFragment.OnCommentListFragmentInteractionListener;
import com.example.hadad.towme.R;
import com.example.hadad.towme.Tables.Comment;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * specified {@link OnCommentListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyCommentItemRecyclerViewAdapter extends RecyclerView.Adapter<MyCommentItemRecyclerViewAdapter.ViewHolder> {

    private final List<Comment> mValues;
    private final OnCommentListFragmentInteractionListener mListener;

    public MyCommentItemRecyclerViewAdapter(List<Comment> items, OnCommentListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        Picasso.with(mListener.getContextListener()).load("https://graph.facebook.com/"+mValues.get(position).getTowId()+"/picture?type=normal").transform(new CropCircleTransformation())
                .resize(40, 40)
                .into(holder.mImageAuthor);
        holder.mCommentView.setText(mValues.get(position).getComment());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mImageAuthor;
        public final TextView mCommentView;
        public Comment mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImageAuthor = (ImageView) view.findViewById(R.id.imageAuthor);
            mCommentView = (TextView) view.findViewById(R.id.comment);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mCommentView.getText() + "'";
        }
    }
}
