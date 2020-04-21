package com.sbadc.survivalera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import cn.iwgang.countdownview.CountdownView;

public class TournamentDetailsActivity extends AppCompatActivity {

    String tourID;

    //Views Are Here...
    TextView winningTv,perKillTv,entryTv,typeTv,versionTv,mapTv;
    TextView tourNameTv,tourDateTv,tourTimeTv;
    //TextView extraAdviceTv;
    TextView totalPlayersTv,playersJoinedTv;
    ImageView tourImageIv;
    CardView goneCVfortourImageIv;

    TextView getHelpTV;

    Button joinMatchBtn;
    FloatingActionButton joinMatchFAB;

//    int totalAmount;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference usersReference;
    String uid;

    String accountbalance;
    String accountCoinsbalance;
    int intAccountBalance;
    int intAccountCoinsBalance;

    boolean joined_tour_or_not = false;

    int tournamentAmount;
    int playerJoinedQuantity;

    //TO CONTROL THE NUMBERS OF PARTICIPANTS JOINED IN ANY MATCH...
    DatabaseReference numOfPlayerJoinedReference;

    private String TOUR_ID;
    private String playerInGameName;

    //for using shared animation this code should be used.....
    CardView winningCV,perkillCv,entryCV,mapCV,versionCV,typeCV;

    CountdownView countdownView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournament_details);

        Intent intent = getIntent();
        tourID = intent.getStringExtra("tourID");

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Tournament Details");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        usersReference = firebaseDatabase.getReference("Users");

        uid = firebaseUser.getUid();

        numOfPlayerJoinedReference = firebaseDatabase.getReference("Participants");

        //Views Initialisation.......
        winningTv = findViewById(R.id.winningPrizeTv);
        perKillTv = findViewById(R.id.perKillPrizeTv);
        entryTv = findViewById(R.id.entryPrizeTv);
        typeTv = findViewById(R.id.matchTypeTv);
        versionTv = findViewById(R.id.matchVersionTv);
        mapTv = findViewById(R.id.matchMapTv);

        tourNameTv = findViewById(R.id.tournamentNameTv);
        tourDateTv = findViewById(R.id.tournamentDateTv);
        tourTimeTv = findViewById(R.id.tournamentTimeTv);

        getHelpTV = findViewById(R.id.getHelpTV);

        tourImageIv = findViewById(R.id.tournamentImageIv);

        //extraAdviceTv = findViewById(R.id.extraAdviseTv);

        totalPlayersTv = findViewById(R.id.totalPlayersTv);
        playersJoinedTv = findViewById(R.id.playerJoinedTv);

        goneCVfortourImageIv = findViewById(R.id.goneCVforIv);

        joinMatchBtn = findViewById(R.id.joinMatchBtn);
        joinMatchFAB = findViewById(R.id.joinMatchBtnFAB);

        winningCV = findViewById(R.id.winningPrizeCV);
        perkillCv = findViewById(R.id.perKillCV);
        entryCV = findViewById(R.id.entryCV);
        mapCV = findViewById(R.id.mapCV);
        versionCV = findViewById(R.id.versionCV);
        typeCV = findViewById(R.id.typeCV);

        countdownView = (CountdownView) findViewById(R.id.CDV);

        //Using Functions....
        loadTournamentDetails();

        numOfPlayerJoinedReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(TOUR_ID).hasChild(uid)){
                    joinMatchBtn.setEnabled(false);
                    joinMatchBtn.setAlpha(0.5f);
                    joinMatchBtn.setText("Joined");

                    joinMatchFAB.setEnabled(false);
                    joinMatchFAB.setAlpha(0.5f);

                    joined_tour_or_not = true;
                }else{
                    joinMatchBtn.setEnabled(true);
                    joinMatchBtn.setAlpha(1f);
                    joinMatchBtn.setText("Join");

                    joinMatchFAB.setEnabled(true);
                    joinMatchFAB.setAlpha(1f);

                    joined_tour_or_not = false;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Query usersQuery = usersReference.orderByChild("email").equalTo(firebaseUser.getEmail());
        usersQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    //account balance in user's account....
                    accountbalance = "" + ds.child("accountBalance").getValue();
                    intAccountBalance = Integer.parseInt(accountbalance);

                    //survival era currency of user's account....
                    accountCoinsbalance = "" + ds.child("accountCoinBalance").getValue();
                    intAccountCoinsBalance = Integer.parseInt(accountCoinsbalance);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        getHelpTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (joined_tour_or_not){
                    showAlertDialogOfGetHelp("joined");
                }else{
                    showAlertDialogOfGetHelp("not_joined");

                }
            }
        });

        joinMatchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* here or statement with including intAccountCoinsBalance condition will checked also*/
                startTransactionAndAddingPlayer();
            }
        });

        joinMatchFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTransactionAndAddingPlayer();
            }
        });
    }

    private void startTransactionAndAddingPlayer(){
        if (intAccountBalance >= Integer.parseInt(entryTv.getText().toString())) {

            AlertDialog.Builder builder1 = new AlertDialog.Builder(TournamentDetailsActivity.this);
            builder1.setTitle("Enter your UserName in Game");
            builder1.setMessage("Please enter the correct name by which you play in game.");

            LinearLayout linearLayout = new LinearLayout(TournamentDetailsActivity.this);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.setPadding(10,10,10,10);

            final EditText editText = new EditText(TournamentDetailsActivity.this);
            editText.setHint("Enter UserName here");

            linearLayout.addView(editText);

            builder1.setView(linearLayout);

            builder1.setPositiveButton("Join",  new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which){
                    playerInGameName = editText.getText().toString().trim();

                    if (playerInGameName.contains("'")){
                        Toast.makeText(TournamentDetailsActivity.this, "Username shouldn't contain apostrophe s('s)", Toast.LENGTH_SHORT).show();
                    }else if (TextUtils.isEmpty(playerInGameName)){
                        Toast.makeText(TournamentDetailsActivity.this, "Invalid Username", Toast.LENGTH_SHORT).show();
                    }else{
                        //UPDATING THE AMOUNT OF RUPEE IN ACCOUNT OF USER....
                        String finalAmount = String.valueOf(Integer.parseInt(accountbalance) - tournamentAmount);
                        String finalPlayerJoinedQuantity = String.valueOf(playerJoinedQuantity + 1);

                        HashMap<String ,Object> updatedAmount = new HashMap<>();
                        updatedAmount.put("accountBalance",finalAmount);
                        updatedAmount.put("playerJoined",finalPlayerJoinedQuantity);
                        usersReference.child(firebaseUser.getUid()).updateChildren(updatedAmount)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        HashMap<String , Object> participantList = new HashMap<>();
                                        participantList.put("PlayerName",playerInGameName);

                                        numOfPlayerJoinedReference.child(TOUR_ID).child(firebaseUser.getUid()).updateChildren(participantList)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        //updating number of joined players...
                                                        int finalPlayerJoined = playerJoinedQuantity + 1;
                                                        HashMap<String,Object> finalPlayerJoinedIncrement = new HashMap<>();
                                                        finalPlayerJoinedIncrement.put("playerJoined",String.valueOf(finalPlayerJoined));

                                                        DatabaseReference tournamentDetailsRef = FirebaseDatabase.getInstance().getReference("Tournaments");
                                                        tournamentDetailsRef.child(TOUR_ID).updateChildren(finalPlayerJoinedIncrement)
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        Toast.makeText(TournamentDetailsActivity.this, "Your Username Added.", Toast.LENGTH_SHORT).show();
                                                                        Toast.makeText(TournamentDetailsActivity.this, "You will be notified before match starts", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                })
                                                                .addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        Toast.makeText(TournamentDetailsActivity.this, "An Error Occurred", Toast.LENGTH_SHORT).show();
                                                                        Toast.makeText(TournamentDetailsActivity.this, "Please contact us at the previous contact page.", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });

                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(TournamentDetailsActivity.this, "An Error Occurred", Toast.LENGTH_SHORT).show();
                                                        Toast.makeText(TournamentDetailsActivity.this, "Please contact us at the previous contact page.", Toast.LENGTH_SHORT).show();
                                                    }
                                                });

//                                                            Toast.makeText(TournamentDetailsActivity.this, "Transaction Successful", Toast.LENGTH_SHORT).show();
//                                                            Toast.makeText(TournamentDetailsActivity.this, "Amount Added To Account", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(TournamentDetailsActivity.this, "Failed to add amount please contact at contact tab.", Toast.LENGTH_SHORT).show();
                                    }
                                });


                        //Toast.makeText(getApplicationContext(), "Payment Transaction response " + inResponse.toString(), Toast.LENGTH_LONG).show();
                    }
                }
            });

            builder1.create().show();

        }else{
            final AlertDialog.Builder builder1 = new AlertDialog.Builder(TournamentDetailsActivity.this);
            builder1.setTitle("Enter your UserName in Game");
            builder1.setMessage("Please enter the correct name by which you play in game.");

            LinearLayout linearLayout = new LinearLayout(TournamentDetailsActivity.this);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.setPadding(10,10,10,10);

            final EditText editText = new EditText(TournamentDetailsActivity.this);
            editText.setHint("Enter UserName here");

            linearLayout.addView(editText);

            builder1.setView(linearLayout);

            builder1.setPositiveButton("Join", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    playerInGameName = editText.getText().toString().trim();
                    if (playerInGameName.contains("'")){
                        Toast.makeText(TournamentDetailsActivity.this, "Username shouldn't contain apostrophe s('s)", Toast.LENGTH_SHORT).show();
                    }else if (TextUtils.isEmpty(playerInGameName)){
                        Toast.makeText(TournamentDetailsActivity.this, "Invalid Username", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(TournamentDetailsActivity.this, "Wait a second, Processing...", Toast.LENGTH_SHORT).show();
                        Toast.makeText(TournamentDetailsActivity.this, "Insufficient amount in your account, Please add some.", Toast.LENGTH_SHORT).show();

                        if (ContextCompat.checkSelfPermission(TournamentDetailsActivity.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(TournamentDetailsActivity.this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS}, 101);
                        }

                        final String M_ID = "esRXFM67170330264023";
                        final String customer_id = FirebaseAuth.getInstance().getUid();
                        final String order_id = UUID.randomUUID().toString().substring(0, 28);
                        final String url = "https://survivalera.000webhostapp.com/paytm/generateChecksum.php";
                        final String callback_url = "https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp";

                        RequestQueue requestQueue = Volley.newRequestQueue(TournamentDetailsActivity.this);
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (jsonObject.has("CHECKSUMHASH")) {
                                        String CHECKSUMHASH = jsonObject.getString("CHECKSUMHASH");

                                        PaytmPGService paytmPGService = PaytmPGService.getStagingService();
                                        HashMap<String, String> paramMap = new HashMap<>();
                                        paramMap.put("MID", M_ID);
                                        paramMap.put("ORDER_ID", order_id);
                                        paramMap.put("CUST_ID", customer_id);
                                        paramMap.put("CHANNEL_ID", "WAP");
                                        paramMap.put("TXN_AMOUNT", entryTv.getText().toString());//.substring(0, entryTv.getText().length()));
                                        paramMap.put("WEBSITE", "WEBSTAGING");
                                        paramMap.put("INDUSTRY_TYPE_ID", "Retail");
                                        paramMap.put("CALLBACK_URL", callback_url);
                                        paramMap.put("CHECKSUMHASH", CHECKSUMHASH);

                                        PaytmOrder order = new PaytmOrder(paramMap);

                                        paytmPGService.initialize(order, null);
                                        paytmPGService.startPaymentTransaction(TournamentDetailsActivity.this, true, true, new PaytmPaymentTransactionCallback() {
                                            @Override
                                            public void onTransactionResponse(Bundle inResponse) {

                                                HashMap<String , Object> participantList = new HashMap<>();
                                                participantList.put("PlayerName",playerInGameName);

                                                numOfPlayerJoinedReference.child(TOUR_ID).child(firebaseUser.getUid()).updateChildren(participantList)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                //updating number of joined players...
                                                                int finalPlayerJoined = playerJoinedQuantity + 1;
                                                                HashMap<String,Object> finalPlayerJoinedIncrement = new HashMap<>();
                                                                finalPlayerJoinedIncrement.put("playerJoined",String.valueOf(finalPlayerJoined));

                                                                DatabaseReference tournamentDetailsRef = FirebaseDatabase.getInstance().getReference("Tournaments");
                                                                tournamentDetailsRef.child(TOUR_ID).updateChildren(finalPlayerJoinedIncrement)
                                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void aVoid) {
                                                                                Toast.makeText(TournamentDetailsActivity.this, "Your Username Added.", Toast.LENGTH_SHORT).show();
                                                                                Toast.makeText(TournamentDetailsActivity.this, "You will be notified before match starts", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        })
                                                                        .addOnFailureListener(new OnFailureListener() {
                                                                            @Override
                                                                            public void onFailure(@NonNull Exception e) {
                                                                                Toast.makeText(TournamentDetailsActivity.this, "An Error Occurred", Toast.LENGTH_SHORT).show();
                                                                                Toast.makeText(TournamentDetailsActivity.this, "Please contact us at the previous contact page.", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        });

                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(TournamentDetailsActivity.this, "An Error Occurred", Toast.LENGTH_SHORT).show();
                                                                Toast.makeText(TournamentDetailsActivity.this, "Please contact us at the previous contact page.", Toast.LENGTH_SHORT).show();
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
                                                Toast.makeText(getApplicationContext(), "Authentication failed: Server error" + inErrorMessage, Toast.LENGTH_LONG).show();
                                            }

                                            @Override
                                            public void someUIErrorOccurred(String inErrorMessage) {
                                                Toast.makeText(getApplicationContext(), "UI Error " + inErrorMessage, Toast.LENGTH_LONG).show();
                                            }

                                            @Override
                                            public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {
                                                Toast.makeText(getApplicationContext(), "Unable to load webpage " + inErrorMessage, Toast.LENGTH_LONG).show();
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
                                Toast.makeText(TournamentDetailsActivity.this, "something when wrong!", Toast.LENGTH_SHORT).show();
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> paramMap = new HashMap<>();
                                paramMap.put("MID", M_ID);
                                paramMap.put("ORDER_ID", order_id);
                                paramMap.put("CUST_ID", customer_id);
                                paramMap.put("CHANNEL_ID", "WAP");
                                paramMap.put("TXN_AMOUNT", entryTv.getText().toString().substring(0, entryTv.getText().length()));
                                paramMap.put("WEBSITE", "WEBSTAGING");
                                paramMap.put("INDUSTRY_TYPE_ID", "Retail");
                                paramMap.put("CALLBACK_URL", callback_url);
                                return paramMap;
                            }
                        };

                        requestQueue.add(stringRequest);
                    }
                }
            });

/*                    builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });*/

            builder1.create().show();


        }
    }

    private void showAlertDialogOfGetHelp(String joined) {

        String title,message;

        if (joined.equals("joined")){
            title = "Joined";
            message = "You have already joined this tournament. You will be notified sometime before the match starts, So please stay tuned for our notification. Thanks";
        }else{
            title = "Join Tournament";
            message = "1. Press the Join button.\n\n" +
                    "2. If you have sufficient amount in your account then pay it through paytm gateway else you will be redirected to the money adding tab.\n\n" +
                    "3. Then add amount to Your account and participate in matches.\n\n ENJOY!";
        }

        final AlertDialog.Builder builder1 = new AlertDialog.Builder(TournamentDetailsActivity.this);
        builder1.setTitle(title);
        builder1.setMessage(message);

        builder1.setPositiveButton("Got It!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder1.setCancelable(false);

        builder1.create().show();
    }

    private void loadTournamentDetails() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tournaments");

        Query postDetailQuery = reference.orderByChild("tourID").equalTo(tourID);
        postDetailQuery.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds:dataSnapshot.getChildren()){
                    //GET......
                    //VALUES......
                    String tourId = "" + ds.child("tourID").getValue();
                    String tourImage = "" + ds.child("tourImage").getValue();
                    String tourName = "" + ds.child("tourName").getValue();
                    String tourDate = "" + ds.child("tourDate").getValue();
                    String tourTime = "" + ds.child("tourTime").getValue();
                    String tourWinningPrize = "" + ds.child("tourWinningPrize").getValue();
                    String tourPerKillPrize = "" + ds.child("tourPerKillPrize").getValue();
                    String tourEntryFee = "" + ds.child("tourEntryFee").getValue();
                    String tourVersion = "" + ds.child("tourVersion").getValue();
                    String tourType = "" + ds.child("tourType").getValue();
                    String tourMap = "" + ds.child("tourMap").getValue();
//                    String tourCreatedTime = "" + ds.child("tourCreatedTime").getValue();
                    //String extraAdvice = "" + ds.child("extraAdvice").getValue();  //NOT USED IN THIS ACTIVITY RIGHT NOW....
                    String playerJoined = "" + ds.child("playerJoined").getValue();
                    String totalPlayer = "" + ds.child("totalPlayer").getValue();
                    String perfectTime = "" + ds.child("perfectTime").getValue();

                    //PUT......
                    //VALUES.....

                    TOUR_ID = tourId;

                    tournamentAmount = Integer.parseInt(tourEntryFee);
                    playerJoinedQuantity = Integer.parseInt(playerJoined);

                    winningTv.setText(tourWinningPrize + " Rs.");
                    perKillTv.setText(tourPerKillPrize + " Rs.");
                    entryTv.setText(tourEntryFee + " Rs.");
                    typeTv.setText(tourType);
                    versionTv.setText(tourVersion);
                    mapTv.setText(tourMap);

                    tourNameTv.setText(tourName);
                    tourDateTv.setText("Date: "+tourDate);
                    tourTimeTv.setText("Time: "+tourTime);

                    totalPlayersTv.setText("Total Players: " + totalPlayer);
                    playersJoinedTv.setText("Players Joined: " + playerJoined);

                    if(tourImage.equals("noImage")){
                        goneCVfortourImageIv.setVisibility(View.GONE);
                        tourImageIv.setVisibility(View.GONE);
                    }else{
                        try{
                            goneCVfortourImageIv.setVisibility(View.VISIBLE);
                            tourImageIv.setVisibility(View.VISIBLE);
                            Picasso.get().load(tourImage).placeholder(R.drawable.play_game).into(tourImageIv);
                        }catch (Exception e){
                            Toast.makeText(TournamentDetailsActivity.this, "Can't load the image.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    if (Integer.parseInt(playerJoined) == Integer.parseInt(totalPlayer)){
                        joinMatchBtn.setAlpha(0.5f);
                        joinMatchBtn.setEnabled(false);
                        joinMatchBtn.setText("FULL");
                    }
                    //totalAmount = Integer.parseInt(entryTv.getText().toString().substring(0,entryTv.getText().length()));

                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
//                    String countDate = "01-05-2020 00:00:00";
                    Date now = new Date();

                    try {
                        Date date = sdf.parse(perfectTime);
                        long currentDate = now.getTime();
                        long newYearDate = date.getTime();
                        long countDownoNewYear = newYearDate - currentDate;

                        countdownView.start(countDownoNewYear);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        /*TOUR_ID = null;
        uid = null;
        playerInGameName = null;*/
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        loadingDialog.dismiss();
    }
}
