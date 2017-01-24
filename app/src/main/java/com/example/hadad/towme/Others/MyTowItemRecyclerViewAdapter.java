package com.example.hadad.towme.Others;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hadad.towme.R;
import com.example.hadad.towme.Tables.Tow;
import com.example.hadad.towme.Activities.TowListFragment.OnListFragmentInteractionListener;
import java.util.List;

/**
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyTowItemRecyclerViewAdapter extends RecyclerView.Adapter<MyTowItemRecyclerViewAdapter.ViewHolder> {

    private final List<Tow> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyTowItemRecyclerViewAdapter(List<Tow> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_towitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.mIndex.setText("" + position);
        holder.mFirstName.setText(mValues.get(position).getFirstName());
        holder.mPrice.setText(mValues.get(position).getPricePerKM()+"");

        double x1= mValues.get(position).getX()-UserProfile.getUser().getX();
        double y1= mValues.get(position).getY()-UserProfile.getUser().getY();
        double dist = Math.sqrt(x1*x1+y1*y1);
        holder.mDistance.setText(String.format("%.2f",dist));

//        switch(mValues.get(position).getRank()){
//            case 0:
//
//        }
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(position);
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
        public final TextView mIndex;
        public final TextView mFirstName;
        public final TextView mPrice;
        public final TextView mDistance;
        public final ImageView mRank;
        public Tow mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIndex = (TextView) view.findViewById(R.id.id);
            mFirstName = (TextView) view.findViewById(R.id.first_name);
            mPrice = (TextView) view.findViewById(R.id.price);
            mDistance = (TextView)view.findViewById(R.id.distance);
            mRank = (ImageView)view.findViewById(R.id.rank);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + mFirstName.getText() + "'";
        }
    }
}
