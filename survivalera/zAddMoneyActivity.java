package com.sbadc.survivalera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.sbadc.survivalera.extraclasses.LoadingDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class zAddMoneyActivity extends AppCompatActivity {

    Button addMoneyBtn, buyPremiumBtn, showPremiumDetailsBtn;
    EditText moneyEnteredEt;

    CardView SECoinsCV01, SECoinsCV02, SECoinsCV03, SECoinsCV04, SECoinsCV05;

    TextView emailTV,amountOfRuppeTV, amountOfCoinTV;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference userReference;
    DatabaseReference totalDonationRef;

    String accountbalance,accountCoinsbalance;

    //FOR DONATION PURPOSE CODES...
//    EditText donationMoneyET;
//    Button manuallyEnterAmountBTN;
//    TextView donationAdviceTV;
    Button donateMoneyBTN;
    TextView textViewForDonationAmountShower;
    SeekBar seekBarForDonationAmountShower;
    String amountToDonate;
    String userDonatedAmount;
    String amountDonatedTillNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_z_add_money);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        userReference = firebaseDatabase.getReference("Users");
        totalDonationRef = firebaseDatabase.getReference("Donations");

        addMoneyBtn = findViewById(R.id.addMoneyBtn);
        buyPremiumBtn = findViewById(R.id.buyPremiumBtn);
        showPremiumDetailsBtn = findViewById(R.id.getDetailsOfPremiumBtn);

        moneyEnteredEt = findViewById(R.id.enteredAmountEt);

        amountOfRuppeTV = findViewById(R.id.amountOfRupee);
        amountOfCoinTV = findViewById(R.id.amountOfCoins);

        SECoinsCV01 = findViewById(R.id.buySECoins01);
        SECoinsCV02 = findViewById(R.id.buySECoins02);
        SECoinsCV03 = findViewById(R.id.buySECoins03);
        SECoinsCV04 = findViewById(R.id.buySECoins04);
        SECoinsCV05 = findViewById(R.id.buySECoins05);

        //FOR DONATION PURPOSE CODE...
//        donationMoneyET = findViewById(R.id.donateAmountET);
//        manuallyEnterAmountBTN = findViewById(R.id.manuallyEnterAmountBTN);
//        donationAdviceTV = findViewById(R.id.donationAmountAdviceTV);
        donateMoneyBTN = findViewById(R.id.donateAmountBTN);
        textViewForDonationAmountShower = findViewById(R.id.textViewForDonationAmount);
        seekBarForDonationAmountShower = findViewById(R.id.seekBarForDonationAmount);

        final Query usersQuery = userReference.orderByChild("email").equalTo(firebaseUser.getEmail());
        usersQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    //get data
                    String _accountbalance = "" + ds.child("accountBalance").getValue();
                    String _accountCoinsbalance = "" + ds.child("accountCoinBalance").getValue();
                    userDonatedAmount = "" + ds.child("donatedAmount").getValue();

                    amountOfRuppeTV.setText(_accountbalance);
                    amountOfCoinTV.setText(_accountCoinsbalance);

                    accountbalance = _accountbalance;
                    accountCoinsbalance = _accountCoinsbalance;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Query donatedTillNowQuery = totalDonationRef.orderByChild("TotalDonationsMadeByUsersSB");
        donatedTillNowQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds:dataSnapshot.getChildren()){
                    amountDonatedTillNow = "" + ds.child("TOTAL").getValue();
                    Toast.makeText(zAddMoneyActivity.this, String.valueOf(amountDonatedTillNow), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /*Query usersQuery = databaseReference.orderByChild("email").equalTo(firebaseUser.getEmail());
        usersQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String userCoins = "" + ds.child("accountCoinBalance").getValue();
                    String userMoney = "" + ds.child("accountBalance").getValue();

                    coinsTV.setText(userCoins);
                    moneyTV.setText(userMoney);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

        addMoneyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(moneyEnteredEt.getText().toString()) >= 5
                        && Integer.parseInt(moneyEnteredEt.getText().toString()) <= 5000) {

                    Toast.makeText(zAddMoneyActivity.this, "Wait a second, Processing...", Toast.LENGTH_SHORT).show();

                    if (ContextCompat.checkSelfPermission(zAddMoneyActivity.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(zAddMoneyActivity.this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS}, 101);
                    }

                    final String amountToAdd = moneyEnteredEt.getText().toString();

                    final String M_ID = "esRXFM67170330264023";
                    final String customer_id = FirebaseAuth.getInstance().getUid();
                    final String order_id = UUID.randomUUID().toString().substring(0, 28);
                    final String url = "https://survivalera.000webhostapp.com/paytm/generateChecksum.php";
                    final String callback_url = "https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp";

                    RequestQueue requestQueue = Volley.newRequestQueue(zAddMoneyActivity.this);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.has("CHECKSUMHASH")) {
                                    String CHECKSUMHASH = jsonObject.getString("CHECKSUMHASH");

                                    PaytmPGService paytmPGService = PaytmPGService.getStagingService();
                                    HashMap<String, String> paramMap = new HashMap<String, String>();
                                    paramMap.put("MID", M_ID);
                                    paramMap.put("ORDER_ID", order_id);
                                    paramMap.put("CUST_ID", customer_id);
                                    paramMap.put("CHANNEL_ID", "WAP");
                                    paramMap.put("TXN_AMOUNT", amountToAdd);//.substring(0, entryTv.getText().length()));
                                    paramMap.put("WEBSITE", "WEBSTAGING");
                                    paramMap.put("INDUSTRY_TYPE_ID", "Retail");
                                    paramMap.put("CALLBACK_URL", callback_url);
                                    paramMap.put("CHECKSUMHASH", CHECKSUMHASH);

                                    PaytmOrder order = new PaytmOrder(paramMap);

                                    paytmPGService.initialize(order, null);
                                    paytmPGService.startPaymentTransaction(zAddMoneyActivity.this, true, true, new PaytmPaymentTransactionCallback() {
                                        @Override
                                        public void onTransactionResponse(Bundle inResponse) {

                                            //UPDATING THE AMOUNT OF RUPEE IN ACCOUNT OF USER....
                                            String finalAmount = String.valueOf(Integer.parseInt(accountbalance) + Integer.parseInt(amountToAdd));

                                            HashMap<String ,Object> updatedAmount = new HashMap<>();
                                            updatedAmount.put("accountBalance",finalAmount);
                                            userReference.child(firebaseUser.getUid()).updateChildren(updatedAmount)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Toast.makeText(zAddMoneyActivity.this, "Transaction Successful", Toast.LENGTH_SHORT).show();
                                                            Toast.makeText(zAddMoneyActivity.this, "Amount Added To Account", Toast.LENGTH_SHORT).show();
                                                            moneyEnteredEt.setText("");
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(zAddMoneyActivity.this, "Failed to add amount please contact at contact tab.", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });

                                            //Toast.makeText(getApplicationContext(), "Payment Transaction response " + inResponse.toString(), Toast.LENGTH_LONG).show();
                                        }

                                        @Override
                                        public void networkNotAvailable() {
                                            Toast.makeText(getApplicationContext(), "Network connection error: Check your internet connectivity", Toast.LENGTH_LONG).show();
                                        }

                                        @Override
                                        public void clientAuthenticationFailed(String inErrorMessage) {
                                            Toast.makeText(getApplicationContext(), "Authentication failed: Server error" + inErrorMessage.toString(), Toast.LENGTH_LONG).show();
                                        }

                                        @Override
                                        public void someUIErrorOccurred(String inErrorMessage) {
                                            Toast.makeText(getApplicationContext(), "UI Error " + inErrorMessage, Toast.LENGTH_LONG).show();
                                        }

                                        @Override
                                        public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {
                                            Toast.makeText(getApplicationContext(), "Unable to load webpage " + inErrorMessage.toString(), Toast.LENGTH_LONG).show();
                                        }

                                        @Override
                                        public void onBackPressedCancelTransaction() {
                                            Toast.makeText(getApplicationContext(), "Transaction cancelled", Toast.LENGTH_LONG).show();
                                        }

                                        @Override
                                        public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                                            Toast.makeText(getApplicationContext(), "Transaction cancelled" + inResponse.toString(), Toast.LENGTH_LONG).show();
                                        }
                                    });

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //                        loadingDialog.dismiss();
                            Toast.makeText(zAddMoneyActivity.this, "something when wrong!", Toast.LENGTH_SHORT).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> paramMap = new HashMap<String, String>();
                            paramMap.put("MID", M_ID);
                            paramMap.put("ORDER_ID", order_id);
                            paramMap.put("CUST_ID", customer_id);
                            paramMap.put("CHANNEL_ID", "WAP");
                            paramMap.put("TXN_AMOUNT", amountToAdd);
                            paramMap.put("WEBSITE", "WEBSTAGING");
                            paramMap.put("INDUSTRY_TYPE_ID", "Retail");
                            paramMap.put("CALLBACK_URL", callback_url);
                            return paramMap;
                        }
                    };

                    requestQueue.add(stringRequest);

                }else{
                    Toast.makeText(zAddMoneyActivity.this, "Please enter legal integer between 50 & 5000", Toast.LENGTH_SHORT).show();
                }
            }
        });

        SECoinsCV01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customBuyingPPGS(10,9999);
            }
        });

        SECoinsCV02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customBuyingPPGS(12,10000);
            }
        });

        SECoinsCV03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customBuyingPPGS(19,19999);
            }
        });

        SECoinsCV04.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customBuyingPPGS(22,9999);
            }
        });

        SECoinsCV05.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customBuyingPPGS(28,29999);
            }
        });

        seekBarForDonationAmountShower.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress == 0 || progress == 1 || progress == 2 || progress == 3){
                    textViewForDonationAmountShower.setText("Donate 15 Rs.");
                    amountToDonate = String.valueOf(15);
                }else{
                    textViewForDonationAmountShower.setText("Donate " + String.valueOf(progress * 5) + " Rs.");
                    amountToDonate = String.valueOf(progress * 5);
                }

                /*if (progress == 100){
                    manuallyEnterAmountBTN.setVisibility(View.VISIBLE);
                    manuallyEnterAmountBTN.setEnabled(true);
                    manuallyEnterAmountBTN.setAlpha(1);
                }else{
                    manuallyEnterAmountBTN.setVisibility(View.GONE);
                    manuallyEnterAmountBTN.setEnabled(false);
                    manuallyEnterAmountBTN.setAlpha(0.5f);

                    donationMoneyET.setVisibility(View.GONE);
                    donationMoneyET.setEnabled(false);
                    donationMoneyET.setAlpha(0.5f);
                }*/

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        /*manuallyEnterAmountBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (seekBarForDonationAmountShower.getProgress() == 100) {
                    donationMoneyET.setVisibility(View.VISIBLE);
                    donationMoneyET.setEnabled(true);
                    donationMoneyET.setAlpha(1);
                }else{
                    donationMoneyET.setVisibility(View.GONE);
                    donationMoneyET.setEnabled(false);
                    donationMoneyET.setAlpha(0.5f);
                }

                donationMoneyET.setVisibility(View.VISIBLE);
                donationMoneyET.setEnabled(true);
                donationMoneyET.setAlpha(1);
            }
        });*/

        donateMoneyBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(zAddMoneyActivity.this, "Wait a second, Processing...", Toast.LENGTH_SHORT).show();

                if (ContextCompat.checkSelfPermission(zAddMoneyActivity.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(zAddMoneyActivity.this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS}, 101);
                }

                final String M_ID = "esRXFM67170330264023";
                final String customer_id = FirebaseAuth.getInstance().getUid();
                final String order_id = UUID.randomUUID().toString().substring(0, 28);
                final String url = "https://survivalera.000webhostapp.com/paytm/generateChecksum.php";
                final String callback_url = "https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp";

                RequestQueue requestQueue = Volley.newRequestQueue(zAddMoneyActivity.this);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.has("CHECKSUMHASH")) {
                                String CHECKSUMHASH = jsonObject.getString("CHECKSUMHASH");

                                PaytmPGService paytmPGService = PaytmPGService.getStagingService();
                                HashMap<String, String> paramMap = new HashMap<String, String>();
                                paramMap.put("MID", M_ID);
                                paramMap.put("ORDER_ID", order_id);
                                paramMap.put("CUST_ID", customer_id);
                                paramMap.put("CHANNEL_ID", "WAP");
                                paramMap.put("TXN_AMOUNT", amountToDonate);//.substring(0, entryTv.getText().length()));
                                paramMap.put("WEBSITE", "WEBSTAGING");
                                paramMap.put("INDUSTRY_TYPE_ID", "Retail");
                                paramMap.put("CALLBACK_URL", callback_url);
                                paramMap.put("CHECKSUMHASH", CHECKSUMHASH);

                                PaytmOrder order = new PaytmOrder(paramMap);

                                paytmPGService.initialize(order, null);
                                paytmPGService.startPaymentTransaction(zAddMoneyActivity.this, true, true, new PaytmPaymentTransactionCallback() {
                                    @Override
                                    public void onTransactionResponse(Bundle inResponse) {

                                        HashMap<String ,Object> hashMap = new HashMap<>();
                                        String string = userDonatedAmount + "+" + amountToDonate;
                                        hashMap.put("donatedAmount", string);

                                        userReference.child(firebaseUser.getUid()).updateChildren(hashMap)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(zAddMoneyActivity.this, "Thanks For Supporting Us.", Toast.LENGTH_SHORT).show();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(zAddMoneyActivity.this, "Transaction Successfull But Name not sent.", Toast.LENGTH_SHORT).show();
                                                    }
                                                });


                                        //set current user email donation
                                        HashMap<String, Object> totalDonationByUser = new HashMap<>();
                                        hashMap.put(firebaseUser.getEmail().toString(),amountToDonate);
                                        totalDonationRef.child("USERS_RECENT_EMAIL").updateChildren(totalDonationByUser);

                                        //set total donation till the present time
                                        String finalDonatedTillNow = String.valueOf(Integer.parseInt(amountDonatedTillNow) + Integer.parseInt(amountToDonate));
                                        HashMap<String, Object> totalDonation = new HashMap<>();
                                        hashMap.put("TOTAL_DONATION",finalDonatedTillNow);
                                        totalDonationRef.child("TotalDonationsMadeByUsersSB").updateChildren(totalDonation);
                                    }

                                    @Override
                                    public void networkNotAvailable() {
                                        Toast.makeText(getApplicationContext(), "Network connection error: Check your internet connectivity", Toast.LENGTH_LONG).show();
                                    }

                                    @Override
                                    public void clientAuthenticationFailed(String inErrorMessage) {
                                        Toast.makeText(getApplicationContext(), "Authentication failed: Server error" + inErrorMessage.toString(), Toast.LENGTH_LONG).show();
                                    }

                                    @Override
                                    public void someUIErrorOccurred(String inErrorMessage) {
                                        Toast.makeText(getApplicationContext(), "UI Error " + inErrorMessage, Toast.LENGTH_LONG).show();
                                    }

                                    @Override
                                    public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {
                                        Toast.makeText(getApplicationContext(), "Unable to load webpage " + inErrorMessage.toString(), Toast.LENGTH_LONG).show();
                                    }

                                    @Override
                                    public void onBackPressedCancelTransaction() {
                                        Toast.makeText(getApplicationContext(), "Transaction cancelled", Toast.LENGTH_LONG).show();
                                    }

                                    @Override
                                    public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                                        Toast.makeText(getApplicationContext(), "Transaction cancelled" + inResponse.toString(), Toast.LENGTH_LONG).show();
                                    }
                                });

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //                        loadingDialog.dismiss();
                        Toast.makeText(zAddMoneyActivity.this, "something when wrong!", Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> paramMap = new HashMap<String, String>();
                        paramMap.put("MID", M_ID);
                        paramMap.put("ORDER_ID", order_id);
                        paramMap.put("CUST_ID", customer_id);
                        paramMap.put("CHANNEL_ID", "WAP");
                        paramMap.put("TXN_AMOUNT", amountToDonate);
                        paramMap.put("WEBSITE", "WEBSTAGING");
                        paramMap.put("INDUSTRY_TYPE_ID", "Retail");
                        paramMap.put("CALLBACK_URL", callback_url);
                        return paramMap;
                    }
                };

                requestQueue.add(stringRequest);

            }
        });

    }

    public void customBuyingPPGS(final int paymentAmount, final int howManyCoinsToIncrease){

        Toast.makeText(zAddMoneyActivity.this, "Wait a second, Processing...", Toast.LENGTH_SHORT).show();

        if (ContextCompat.checkSelfPermission(zAddMoneyActivity.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(zAddMoneyActivity.this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS}, 101);
        }

        final String M_ID = "esRXFM67170330264023";
        final String customer_id = FirebaseAuth.getInstance().getUid();
        final String order_id = UUID.randomUUID().toString().substring(0, 28);
        final String url = "https://survivalera.000webhostapp.com/paytm/generateChecksum.php";
        final String callback_url = "https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp";

        RequestQueue requestQueue = Volley.newRequestQueue(zAddMoneyActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("CHECKSUMHASH")) {
                        String CHECKSUMHASH = jsonObject.getString("CHECKSUMHASH");

                        PaytmPGService paytmPGService = PaytmPGService.getStagingService();
                        HashMap<String, String> paramMap = new HashMap<String, String>();
                        paramMap.put("MID", M_ID);
                        paramMap.put("ORDER_ID", order_id);
                        paramMap.put("CUST_ID", customer_id);
                        paramMap.put("CHANNEL_ID", "WAP");
                        paramMap.put("TXN_AMOUNT", String.valueOf(paymentAmount));//.substring(0, entryTv.getText().length()));
                        paramMap.put("WEBSITE", "WEBSTAGING");
                        paramMap.put("INDUSTRY_TYPE_ID", "Retail");
                        paramMap.put("CALLBACK_URL", callback_url);
                        paramMap.put("CHECKSUMHASH", CHECKSUMHASH);

                        PaytmOrder order = new PaytmOrder(paramMap);

                        paytmPGService.initialize(order, null);
                        paytmPGService.startPaymentTransaction(zAddMoneyActivity.this, true, true, new PaytmPaymentTransactionCallback() {
                            @Override
                            public void onTransactionResponse(Bundle inResponse) {

                                //UPDATING THE AMOUNT OF RUPEE IN ACCOUNT OF USER....
                                String finalAmount = String.valueOf(Integer.parseInt(accountCoinsbalance) + howManyCoinsToIncrease);

                                HashMap<String ,Object> updatedAmount = new HashMap<>();
                                updatedAmount.put("accountCoinBalance",finalAmount);
                                userReference.child(firebaseUser.getUid()).updateChildren(updatedAmount)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(zAddMoneyActivity.this, "Transaction Successful", Toast.LENGTH_SHORT).show();
                                                Toast.makeText(zAddMoneyActivity.this, "Coins Added To Account", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(zAddMoneyActivity.this, "Failed to add amount please contact at contact tab.", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                //Toast.makeText(getApplicationContext(), "Payment Transaction response " + inResponse.toString(), Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void networkNotAvailable() {
                                Toast.makeText(getApplicationContext(), "Network connection error: Check your internet connectivity", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void clientAuthenticationFailed(String inErrorMessage) {
                                Toast.makeText(getApplicationContext(), "Authentication failed: Server error" + inErrorMessage.toString(), Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void someUIErrorOccurred(String inErrorMessage) {
                                Toast.makeText(getApplicationContext(), "UI Error " + inErrorMessage, Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {
                                Toast.makeText(getApplicationContext(), "Unable to load webpage " + inErrorMessage.toString(), Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onBackPressedCancelTransaction() {
                                Toast.makeText(getApplicationContext(), "Transaction cancelled", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                                Toast.makeText(getApplicationContext(), "Transaction cancelled" + inResponse.toString(), Toast.LENGTH_LONG).show();
                            }
                        });

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(zAddMoneyActivity.this, "something when wrong!", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> paramMap = new HashMap<String, String>();
                paramMap.put("MID", M_ID);
                paramMap.put("ORDER_ID", order_id);
                paramMap.put("CUST_ID", customer_id);
                paramMap.put("CHANNEL_ID", "WAP");
                paramMap.put("TXN_AMOUNT", String.valueOf(paymentAmount));
                paramMap.put("WEBSITE", "WEBSTAGING");
                paramMap.put("INDUSTRY_TYPE_ID", "Retail");
                paramMap.put("CALLBACK_URL", callback_url);
                return paramMap;
            }
        };

        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onStart() {
        seekBarForDonationAmountShower.setProgress(4);
        super.onStart();
    }
}
