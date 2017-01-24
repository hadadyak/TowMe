package com.example.hadad.towme.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.hadad.towme.DynamoDB.DynamoDBManagerTask;
import com.example.hadad.towme.DynamoDB.MyQuery;
import com.example.hadad.towme.Others.Constants;
import com.example.hadad.towme.R;
import com.example.hadad.towme.Tables.User;
import com.facebook.Profile;

public class PhoneNumberActivity extends AppCompatActivity implements DynamoDBManagerTask.DynamoDBManagerTaskResponse{

    final String phoneNumber[] = new String[1];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number);
        final Spinner spinner = (Spinner) findViewById(R.id.prefix_number_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.prefixNumbers, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        final EditText verf_edit = (EditText) findViewById(R.id.edit_verf_number);
        final Button verf_btn = (Button) findViewById(R.id.verf_btn);
        final EditText editPhoneNumber = (EditText)findViewById(R.id.editPhoneNumber);
        ((Button)findViewById(R.id.send_verf_btn)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int random = (int)(Math.random()*89999+10000);
                SmsManager smsManager = SmsManager.getDefault();
                String prefix = spinner.toString();
                phoneNumber[0] = prefix + (editPhoneNumber).getText().toString();
                phoneNumber[0] = "0522674573";
                //smsManager.sendTextMessage(phoneNumber, null, "TowMe code "+ random+ " If you dont know what is this message just ignore it.", null, null);
                verf_edit.setVisibility(View.VISIBLE);
                verf_btn.setVisibility(View.VISIBLE);
            }
        });
        final DynamoDBManagerTask getUser = new DynamoDBManagerTask(this);

        verf_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //if success

                MyQuery<User> getQuery = new MyQuery<User>( Constants.DynamoDBManagerType.GET_USER_BY_ID,new User(Long.parseLong(Profile.getCurrentProfile().getId())));
                getUser.execute(getQuery);
                verf_btn.setOnClickListener(null);
                Intent intent = new Intent(PhoneNumberActivity.this,EditProfileActivity.class);
                startActivity(intent);

            }
        });
    }

    @Override
    public void DynamoDBManagerTaskResponse(MyQuery myQ) {
        if(myQ.getType() == Constants.DynamoDBManagerType.GET_USER_BY_ID){
            myQ.setType(Constants.DynamoDBManagerType.INSERT_USER);
            ((MyQuery<User>)myQ).getContent().setTelephone(phoneNumber[0]);
            DynamoDBManagerTask inserUser = new DynamoDBManagerTask(this);
            inserUser.execute(myQ);
        }
    }
}
