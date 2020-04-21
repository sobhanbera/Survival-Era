package com.sbadc.survivalera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Random;
import java.util.regex.Pattern;

public class zProfileActivity extends AppCompatActivity {

    EditText fullnameEt,inGameUserNameEt,emailEt,phoneNumberEt;
    TextView accountBalanceTV,coinsAmountTv,wantToUpdatePassword;
    TextView fullnameTV,emailTV,phoneTV;
    TextView totalMatchesPlayedTv,chickenDinnersTv,totalKillsTv;
    Button updateProfileBtn;

    //Human Verification TextViews and EditText
    TextView HVTV01,HVTV02,HVTV03,HVTV04,HVTV05,HVTV06;
    EditText humanVerificationEt;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference usersReference;

    ProgressDialog progressDialog;

    String humanVerificationString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_z_profile);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        usersReference = firebaseDatabase.getReference("Users");

        fullnameEt = findViewById(R.id.fullnameEt);
        inGameUserNameEt = findViewById(R.id.ingameusernameEt);
        emailEt = findViewById(R.id.emailEt);
        phoneNumberEt = findViewById(R.id.phonenoEt);

        HVTV01 = findViewById(R.id.HVTV01);
        HVTV02 = findViewById(R.id.HVTV02);
        HVTV03 = findViewById(R.id.HVTV03);
        HVTV04 = findViewById(R.id.HVTV04);
        HVTV05 = findViewById(R.id.HVTV05);
        HVTV06 = findViewById(R.id.HVTV06);
        humanVerificationEt = findViewById(R.id.humanVerificationEt);

        updateProfileBtn = findViewById(R.id.updateProfileBtn);

        accountBalanceTV = findViewById(R.id.accountBalanceTV);
        coinsAmountTv = findViewById(R.id.accountBalanceCoinsTv);
        wantToUpdatePassword = findViewById(R.id.wantToChangePassword);

        fullnameTV = findViewById(R.id.fullNameTV);
        emailTV = findViewById(R.id.emailTV);
        phoneTV = findViewById(R.id.phoneTV);

        totalMatchesPlayedTv = findViewById(R.id.totalMatchPlayedTv);
        chickenDinnersTv = findViewById(R.id.chickenDinnerTv);
        totalKillsTv = findViewById(R.id.totalKillsTv);

        Random random = new Random();
        String outputHVText01 = String.valueOf(random.nextInt((9 - 1) + 1) + 1);
        String outputHVText02 = String.valueOf(random.nextInt((9 - 1) + 1) + 1);
        String outputHVText03 = String.valueOf(random.nextInt((9 - 1) + 1) + 1);
        String outputHVText04 = String.valueOf(random.nextInt((9 - 1) + 1) + 1);
        String outputHVText05 = String.valueOf(random.nextInt((9 - 1) + 1) + 1);
        String outputHVText06 = String.valueOf(random.nextInt((9 - 1) + 1) + 1);

        HVTV01.setText(outputHVText01);
        HVTV02.setText(outputHVText02);
        HVTV03.setText(outputHVText03);
        HVTV04.setText(outputHVText04);
        HVTV05.setText(outputHVText05);
        HVTV06.setText(outputHVText06);

        humanVerificationString = outputHVText01 + outputHVText02 + outputHVText03 + outputHVText04 + outputHVText05 + outputHVText06;

        //To get details user details in this activity.....
        Query usersQuery = usersReference.orderByChild("email").equalTo(firebaseUser.getEmail());
        usersQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    //get data
                    String fullname = "" + ds.child("fullname").getValue();
                    String username = "" + ds.child("username").getValue();
                    String email = "" + ds.child("email").getValue();
                    String phone_number = "" + ds.child("phoneNumber").getValue();

                    String accountbalance = "" + ds.child("accountBalance").getValue();
                    String accountCoinsbalance = "" + ds.child("accountCoinBalance").getValue();

                    String totalMatchPlayed = "" + ds.child("matchPlayed").getValue();
                    String matchWon = "" + ds.child("matchWon").getValue();
                    String totalKills = "" + ds.child("totalKills").getValue();

                    fullnameTV.setText(fullname);
                    emailTV.setText(email);
                    phoneTV.setText(phone_number);

                    fullnameEt.setText(fullname);
                    inGameUserNameEt.setText(username);
                    emailEt.setText(email);
                    phoneNumberEt.setText(phone_number);

                    accountBalanceTV.setText(accountbalance + "  Rs. ");
                    coinsAmountTv.setText(accountCoinsbalance);

                    totalMatchesPlayedTv.setText(totalMatchPlayed);
                    chickenDinnersTv.setText(matchWon);
                    totalKillsTv.setText(totalKills);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        updateProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String updatedname = fullnameEt.getText().toString().trim();
                final String updatedingameusername = inGameUserNameEt.getText().toString().trim();
                final String updatedphone = phoneNumberEt.getText().toString().trim();

                if(TextUtils.isEmpty(updatedname) && TextUtils.isEmpty(updatedingameusername)&& TextUtils.isEmpty(updatedphone)){
                    Toast.makeText(zProfileActivity.this, "All fields are required.", Toast.LENGTH_SHORT).show();
                }else if(updatedname.length() < 8){
                    fullnameEt.setError("Full name must contain 8 letter");
                    fullnameEt.setFocusable(true);
                }else if(updatedingameusername.contains("'")){
                    Toast.makeText(zProfileActivity.this, "Username shouldn't contain apostrophe s('s)", Toast.LENGTH_SHORT).show();
                }else if(updatedphone.length() != 10){
                    phoneNumberEt.setError("Input correct phone number.");
                    phoneNumberEt.setFocusable(true);
                }else if(!humanVerificationEt.getText().toString().equals(humanVerificationString) || humanVerificationEt.getText().toString().isEmpty()){
                    Toast.makeText(zProfileActivity.this, "input code is invalid", Toast.LENGTH_SHORT).show();
                    humanVerificationEt.setText("");
                    Random random = new Random();
                    String outputHVText01 = String.valueOf(random.nextInt((9 - 1) + 1) + 1);
                    String outputHVText02 = String.valueOf(random.nextInt((9 - 1) + 1) + 1);
                    String outputHVText03 = String.valueOf(random.nextInt((9 - 1) + 1) + 1);
                    String outputHVText04 = String.valueOf(random.nextInt((9 - 1) + 1) + 1);
                    String outputHVText05 = String.valueOf(random.nextInt((9 - 1) + 1) + 1);
                    String outputHVText06 = String.valueOf(random.nextInt((9 - 1) + 1) + 1);

                    HVTV01.setText(outputHVText01);
                    HVTV02.setText(outputHVText02);
                    HVTV03.setText(outputHVText03);
                    HVTV04.setText(outputHVText04);
                    HVTV05.setText(outputHVText05);
                    HVTV06.setText(outputHVText06);

                    humanVerificationString = outputHVText01 + outputHVText02 + outputHVText03 + outputHVText04 + outputHVText05 + outputHVText06;
                }else{
                    showProfileUpdateDialog(updatedname,updatedingameusername,updatedphone);
                    humanVerificationEt.setText("");
                    Random random = new Random();
                    String outputHVText01 = String.valueOf(random.nextInt((9 - 1) + 1) + 1);
                    String outputHVText02 = String.valueOf(random.nextInt((9 - 1) + 1) + 1);
                    String outputHVText03 = String.valueOf(random.nextInt((9 - 1) + 1) + 1);
                    String outputHVText04 = String.valueOf(random.nextInt((9 - 1) + 1) + 1);
                    String outputHVText05 = String.valueOf(random.nextInt((9 - 1) + 1) + 1);
                    String outputHVText06 = String.valueOf(random.nextInt((9 - 1) + 1) + 1);

                    HVTV01.setText(outputHVText01);
                    HVTV02.setText(outputHVText02);
                    HVTV03.setText(outputHVText03);
                    HVTV04.setText(outputHVText04);
                    HVTV05.setText(outputHVText05);
                    HVTV06.setText(outputHVText06);

                    humanVerificationString = outputHVText01 + outputHVText02 + outputHVText03 + outputHVText04 + outputHVText05 + outputHVText06;
                }
            }
        });

        wantToUpdatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPasswordResetDialog();
            }
        });

        progressDialog = new ProgressDialog(this);

    }

    private void showPasswordResetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Recover Password");

        LinearLayout linearLayout = new LinearLayout(this);

        final EditText emailEt = new EditText(this);
        emailEt.setHint("Email");
        emailEt.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        emailEt.setMinEms(16);

        linearLayout.addView(emailEt);
        linearLayout.setPadding(10,10,10,10);

        builder.setView(linearLayout);

        builder.setPositiveButton("Recover", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int which){
                String email = emailEt.getText().toString().trim();
                beginRecovery(email);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int which){
                dialogInterface.dismiss();
            }
        });

        builder.create().show();
    }

    private void beginRecovery(String email) {
        progressDialog.setMessage("Sending Email...");
        progressDialog.show();
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    Toast.makeText(zProfileActivity.this, "Email sent successfully.", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(zProfileActivity.this, "Failed to send email.", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(zProfileActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showProfileUpdateDialog(final String name, final String username, final String phone) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(zProfileActivity.this);
        builder.setTitle("Update Profile");
        builder.setMessage("Are you sure you want to change profile?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final AlertDialog.Builder builder1 = new AlertDialog.Builder(zProfileActivity.this);
                builder1.setTitle("Last Step");

                LinearLayout linearLayout = new LinearLayout(zProfileActivity.this);
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.setPadding(10,10,10,10);

                final EditText editText = new EditText(zProfileActivity.this);
                editText.setHint("Enter Your Email");

                linearLayout.addView(editText);

                builder1.setView(linearLayout);

                builder1.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String infoToUpdateProfile = editText.getText().toString().trim();
                        if (infoToUpdateProfile.equals(firebaseUser.getEmail())){
                            updateProfile(name,username,phone);
                        }else{
                            Toast.makeText(zProfileActivity.this, "Email is incorrect.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder1.create().show();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }

    private void updateProfile(String name, String ingameusername, String phone) {

        if(name.length() < 8){
            fullnameEt.setError("Full name must contain 8 letter");
            fullnameEt.setFocusable(true);
        }else if(ingameusername.contains("'")){
            Toast.makeText(zProfileActivity.this, "Username shouldn't contain apostrophe s('s)", Toast.LENGTH_SHORT).show();
        }else if(phone.length() != 10){
            phoneNumberEt.setError("Input correct phone number.");
            phoneNumberEt.setFocusable(true);
        }else if(name.isEmpty()){
            Toast.makeText(zProfileActivity.this, "Please Fill full name", Toast.LENGTH_SHORT).show();
        }else if(ingameusername.isEmpty()){
            Toast.makeText(zProfileActivity.this, "Please Fill username", Toast.LENGTH_SHORT).show();
        }else if(phone.isEmpty()){
            Toast.makeText(zProfileActivity.this, "Please Fill mobile number", Toast.LENGTH_SHORT).show();
        }else{
            HashMap<String , Object> updatedValues = new HashMap<>();
            updatedValues.put("fullname",name);
            updatedValues.put("username",ingameusername);
            updatedValues.put("phoneNumber",phone);

            usersReference.child(firebaseUser.getUid()).updateChildren(updatedValues)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(zProfileActivity.this, "Profile Updated Successfully.", Toast.LENGTH_SHORT).show();
                            humanVerificationEt.setText("");
                            Random random = new Random();
                            String outputHVText01 = String.valueOf(random.nextInt((9 - 1) + 1) + 1);
                            String outputHVText02 = String.valueOf(random.nextInt((9 - 1) + 1) + 1);
                            String outputHVText03 = String.valueOf(random.nextInt((9 - 1) + 1) + 1);
                            String outputHVText04 = String.valueOf(random.nextInt((9 - 1) + 1) + 1);
                            String outputHVText05 = String.valueOf(random.nextInt((9 - 1) + 1) + 1);
                            String outputHVText06 = String.valueOf(random.nextInt((9 - 1) + 1) + 1);

                            HVTV01.setText(outputHVText01);
                            HVTV02.setText(outputHVText02);
                            HVTV03.setText(outputHVText03);
                            HVTV04.setText(outputHVText04);
                            HVTV05.setText(outputHVText05);
                            HVTV06.setText(outputHVText06);

                            humanVerificationString = outputHVText01 + outputHVText02 + outputHVText03 + outputHVText04 + outputHVText05 + outputHVText06;
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(zProfileActivity.this, "Servers Are Busy Please Try Again Later.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
