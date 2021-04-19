package com.example.clevertapper;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.SyncListener;

import org.json.JSONObject;

import java.util.HashMap;

import io.branch.referral.Branch;
import io.branch.referral.BranchError;

public class MainActivity extends AppCompatActivity implements SyncListener {

    private CleverTapAPI cleverTapDefaultInstance;
    private Branch.BranchReferralInitListener branchReferralInitListener =
            new Branch.BranchReferralInitListener() {
                @Override
                public void onInitFinished(@Nullable JSONObject referringParams, @Nullable BranchError error) {

                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        Button getCTIDbutton;
        Button login;

        final EditText name;
        final EditText email;
        final EditText phone;
        final EditText id;


        setContentView(R.layout.activity_main);

        getCTIDbutton = findViewById(R.id.getctidbutton);
        final TextView ctid = findViewById(R.id.ctidtext);

        login = findViewById(R.id.loginbutton);
        name = findViewById(R.id.uname);
        email = findViewById(R.id.emailtxt);
        phone = findViewById(R.id.phonetxt);
        id = findViewById(R.id.identity);

        //Clevertap
        CleverTapAPI.setDebugLevel(3);   //Set Log level to VERBOSE
        cleverTapDefaultInstance = CleverTapAPI.getDefaultInstance(getApplicationContext());

        cleverTapDefaultInstance.setSyncListener(this); // set listener for profileDidInitialize() callback

        getCTIDbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ctid.setText(cleverTapDefaultInstance.getCleverTapID());
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                HashMap<String, Object> profileUpdate = new HashMap<String, Object>();
                profileUpdate.put("Name", name.getText().toString());                  // String
                profileUpdate.put("Identity", id.getText().toString());                    // String or number
                profileUpdate.put("Email", email.getText().toString());               // Email address of the user
                profileUpdate.put("Phone", phone.getText().toString());                 // Phone (with the country code, starting with +)


                cleverTapDefaultInstance.onUserLogin(profileUpdate);

            }
        });

    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        this.setIntent(intent);
        Branch.getInstance().reInitSession(this, branchReferralInitListener);
    }

    @Override
    public void profileDataUpdated(final JSONObject updates) {

    }

    @Override
    public void profileDidInitialize(final String CleverTapID) {
        Branch branch = Branch.getInstance();
        Log.e("BRANCH SDK", "I am in");
        Log.e("ClevertapTest", "Clevertap id is " + CleverTapID);

        branch.setRequestMetadata("$clevertap_attribution_id",
                CleverTapID);


        // Branch init
        branch.initSession(branchReferralInitListener, MainActivity.this.getIntent().getData(), MainActivity.this);
    }
}



