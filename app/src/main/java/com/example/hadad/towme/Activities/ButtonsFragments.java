package com.example.hadad.towme.Activities;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.hadad.towme.R;

/**
 * A fragment with a Google +1 button.
 * Activities that contain this fragment must implement the
 * {@link ButtonsFragments.OnButtonFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ButtonsFragments#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ButtonsFragments extends Fragment {

    private OnButtonFragmentInteractionListener mListener;
    enum SortBy{
        PRICE,
        DISTANCE,
        RANK
    }
    public ButtonsFragments() {
        // Required empty public constructor
    }

    public static ButtonsFragments newInstance(String param1, String param2) {
        ButtonsFragments fragment = new ButtonsFragments();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_buttons_fragments, container, false);

        Button price = (Button) view.findViewById(R.id.price_bt);
        price.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("Click pressed","Price");
                mListener.onButtonInteractionClick(SortBy.PRICE);
            }
        });

        Button distance = (Button) view.findViewById(R.id.dist_bt);
        distance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onButtonInteractionClick(SortBy.DISTANCE);
            }
        });

        Button rank = (Button) view.findViewById(R.id.rank_bt);
        rank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onButtonInteractionClick(SortBy.RANK);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Refresh the state of the +1 button each time the activity receives focus.
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnButtonFragmentInteractionListener) {
            mListener = (OnButtonFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnButtonFragmentInteractionListener {
        // TODO: Update argument type and name
        void onButtonInteractionClick(SortBy sort);
    }

}
