package com.example.hadad.towme.Activities;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentContainer;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.Editable;
import android.text.Layout;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.hadad.towme.DynamoDB.DynamoDBManagerTask;
import com.example.hadad.towme.DynamoDB.MyQuery;
import com.example.hadad.towme.Others.CommentsTable;
import com.example.hadad.towme.Others.Constants;
import com.example.hadad.towme.Others.CropCircleTransformation;
import com.example.hadad.towme.Others.InterceptViewPager;
import com.example.hadad.towme.Others.OnSwipeTouchListener;
import com.example.hadad.towme.Others.TowList;
import com.example.hadad.towme.Others.UserProfile;
import com.example.hadad.towme.R;
import com.example.hadad.towme.Tables.Comment;
import com.example.hadad.towme.Tables.Tow;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.ProfilePictureView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TowProfileActivity extends AppCompatActivity implements CommentListFragment.OnCommentListFragmentInteractionListener ,DynamoDBManagerTask.DynamoDBManagerTaskResponse{

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    public InterceptViewPager mViewPager;

    private FloatingActionButton fab;
    private DynamoDBManagerTask newComment;
    private boolean toSend=false;
    private EditText msgEditor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tow_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (InterceptViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(getIntent().getIntExtra("position",0));

        msgEditor = (EditText) findViewById(R.id.writeMessageEditText);
        final CoordinatorLayout cordLayout = (CoordinatorLayout)findViewById(R.id.cordLayout);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.bringToFront();
        newComment = new DynamoDBManagerTask(this);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!toSend) {
                        final int xOffset = cordLayout.getMeasuredWidth() - 2*fab.getWidth();

                        TranslateAnimation animTr = new TranslateAnimation(0, -xOffset, 0, 0);
                        Animation animRot = new RotateAnimation(0, 90, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                        Animation animSc = new ScaleAnimation(0f, 1f, 1f, 1f, Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0.5f);

                        AnimationSet animSet = new AnimationSet(true);
                        animSet.setDuration(200);
                        animSet.addAnimation(animRot);
                        animSet.addAnimation(animTr);
                        animSc.setDuration(200);
                        animSc.setFillAfter(true);
                        animTr.setAnimationListener(new TranslateAnimation.AnimationListener() {

                            @Override
                            public void onAnimationStart(Animation animation) {
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                fab.setRotation(-90);
                                CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
                                params.gravity = Gravity.LEFT |Gravity.BOTTOM;
                                fab.setLayoutParams(params);

                                OnSwipeTouchListener swipe = new OnSwipeTouchListener(getApplication()) {
                                    public void onSwipeBottom() {
                                        swipeLeft();
                                    }
                                };
                                fab.setOnTouchListener(swipe);
                            }
                        });
                        fab.startAnimation(animSet);
                        msgEditor.setVisibility(View.VISIBLE);
                        msgEditor.startAnimation(animSc);
                        toSend=!toSend;


                    }else{
                        String CommentMsg =  msgEditor.getText().toString().substring(3);
                        if(!CommentMsg.equals("")){
//                            MyQuery<Comment> comm = new MyQuery<Comment>(Constants.DynamoDBManagerType.ADD_COMMENTS,new Comment(tow.getId(), UserProfile.getId(),CommentMsg));
//                            newComment.execute(comm);
                        }
                        msgEditor.setText("   ");
                    }
                }
            });

            Selection.setSelection(msgEditor.getText(), msgEditor.getText().length());

            msgEditor.setText("   ");
            Selection.setSelection(msgEditor.getText(), msgEditor.getText().length());
            msgEditor.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(!s.toString().contains("   ")){
                        msgEditor.setText("   ");
                        Selection.setSelection(msgEditor.getText(), msgEditor.getText().length());

                    }

                }
            });
    }


    @Override
    public void DynamoDBManagerTaskResponse(MyQuery myQ) {
        if(myQ.getType() == Constants.DynamoDBManagerType.ADD_COMMENTS_RES){
            Fragment commFrag = getSupportFragmentManager().findFragmentById(R.id.frag_list_comment);
            ((CommentListFragment)commFrag).notifyList();
            newComment = new DynamoDBManagerTask(this);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tow_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListFragmentInteraction(Comment item) {

    }


    private void swipeLeft(){

        final CoordinatorLayout cordLayout = (CoordinatorLayout)findViewById(R.id.cordLayout);
        final int xOffset = cordLayout.getMeasuredWidth() - 2*fab.getWidth();

        TranslateAnimation animTr = new TranslateAnimation(0, -xOffset, 0, 0);
        Animation animRot = new RotateAnimation(0, -90, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        Animation animSc = new ScaleAnimation(1f, 0f, 1f, 1f, Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0.5f);

        AnimationSet animSet = new AnimationSet(true);
        animSet.addAnimation(animRot);
        animSet.addAnimation(animTr);

        animSet.setDuration(200);
        animSc.setDuration(200);

        animSc.setFillAfter(true);
        animTr.setAnimationListener(new TranslateAnimation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                fab.setRotation(90);
                CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
                params.gravity = Gravity.RIGHT |Gravity.BOTTOM;
                fab.setLayoutParams(params);
                fab.setOnTouchListener(null);
            }
        });
        fab.startAnimation(animSet);

        msgEditor.setVisibility(View.VISIBLE);
        msgEditor.startAnimation(animSc);
        toSend=!toSend;

    }
    @Override
    public Context getContextListener(){
        return getApplicationContext();
    }

    public static class PlaceholderFragment extends Fragment  {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_tow_profile, container, false);
            int index=getArguments().getInt(ARG_SECTION_NUMBER);
            final Tow tow = TowList.getTow(index);
            try{
                Picasso.with(getContext()).load("https://graph.facebook.com/"+tow.getId()+"/picture?type=normal").transform(new CropCircleTransformation())
                        .resize(120, 120)
                        .into((ImageView)rootView.findViewById(R.id.profile_image_tow));
            }catch (Exception e) {
                Picasso.with(getContext()).load("https://vignette3.wikia.nocookie.net/worldofcarsdrivein/images/e/ee/Mater.png/revision/latest?cb=20111006091530").into((ImageView) rootView.findViewById(R.id.profile_image_tow));
            }
            TextView name = (TextView)rootView.findViewById(R.id.set_name_text);
            name.setText(tow.getLastName()+" "+tow.getFirstName());
            TextView telephon = (TextView)rootView.findViewById(R.id.SET_telephone_number);
            telephon.setText(tow.getPricePerKM()+"");  // change to telephone
            Fragment commentFrag = CommentListFragment.newInstance(1,tow.getId());
            getChildFragmentManager().beginTransaction().add(R.id.frag_list_comment,commentFrag).commit();


            return rootView;
        }

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return TowList.size();
        }

    }
}
