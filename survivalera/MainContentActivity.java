package com.sbadc.survivalera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sbadc.survivalera.extraclasses.ConnectionDetector;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class MainContentActivity extends AppCompatActivity {

    //ViewFlipper viewFlipper;
    //For Auto Image Flipper Animation....
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    DatabaseReference detailReference;

    CardView tournamentCV,liveMatchCv,
            resultCV;

    LinearLayout earnLL, myWalletLL, addMoneyLL,
            youtubeLL,facebookLL,instagramLL,
            guidesLL,shareLL,aboutUsLL;
    ImageView profileIV,logoutIV;

    TextView coinsTV,moneyTV;
    ImageView coinIcon,rupeeIcon;
//    TextView extraAdviceTV;

//    WebView webView,webView1,webView2;

    String youtubeChannelLink,instagramLink,facebookLink;

    //Checking Internet Connection...
    ConnectionDetector detector;

    private long backPressedTime;
    private Toast backBtnToast;

    ProgressDialog progressDialog;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_content);

/*USED FOR ADDING ANY KIND OF DATABASE REFERENCE IN GOOGLE FIREBASE DATABASE SYSTEM OF THIS SURVIVAL ERA DOMAIN APP SYSTEM...*/
        /*HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("TOTAL","0");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Donations");
        reference.child("TotalDonationsMadeByUsersSB").updateChildren(hashMap);*/



        /*int images[] = {R.drawable.pubg, R.drawable.freefire, R.drawable.ps5};      //For Auto Image Flipper Animation....
        viewFlipper = findViewById(R.id.viewFlipper);                               //For Auto Image Flipper Animation....
        getSupportActionBar().setDisplayShowHomeEnabled(true);                      //For Auto Image Flipper Animation....
        //getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(false);
        for(int image:images){
            autoFlipperImageMethod(image);                                          //For Auto Image Flipper Animation....
        }*/

        progressDialog = new ProgressDialog(MainContentActivity.this);
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.custom_loading_dialog);
        Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
            }
        },3000);

//        extraAdviceTV = findViewById(R.id.extraAdviseTv);

        /*final Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        detector = new ConnectionDetector(MainContentActivity.this);
                        if(!detector.isConnectedOrNot()){
                            extraAdviceTV.setText("You Are Not Connected With Internet Please Check Your Intenet Connection");
                            Resources resources = getResources();
                            int textColor = resources.getColor(R.color.red);
                            int backColor = resources.getColor(R.color.colorWhite);
                            extraAdviceTV.setTextColor(textColor);
                            extraAdviceTV.setBackgroundColor(backColor);
                        }else{
                            extraAdviceTV.setText("Welcome To Survival Era Tournament App. " +
                                    "Participate, Play and Win Paytm Cash. " +
                                    "Follow us on Instagram,Facebook,Discord,Twitter and also Subscribe our YouTube Channel. " +
                                    "All the links are given below.");
                            Resources resources = getResources();
                            int textColor = resources.getColor(R.color.colorWhite);
                            int backColor = resources.getColor(R.color.colorDark02);
                            extraAdviceTV.setTextColor(textColor);
                            extraAdviceTV.setBackgroundColor(backColor);
                        }
                    }
                });
            }
        },0,3000);*/

        final Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        detector = new ConnectionDetector(MainContentActivity.this);
                        if(!detector.isConnectedOrNot()) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainContentActivity.this);
                            builder.setTitle("No Internet Connection");
                            builder.setMessage("Please Check Your Internet Connectivity. It seem not to be working.");
                            builder.setCancelable(false);


                            builder.setPositiveButton("GOT IT!", new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialogInterface, int which){
                                    dialogInterface.dismiss();
                                }
                            });

                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialogInterface, int which){
                                    dialogInterface.dismiss();
                                }
                            });

                            builder.create().show();
                        }/*else{
                            Toast.makeText(MainContentActivity.this, "Welcome To Survival Era.", Toast.LENGTH_SHORT).show();
                        }*/
                    }
                });
            }
        },0,5000);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");
        detailReference = firebaseDatabase.getReference("App_Details");

        tournamentCV = findViewById(R.id.openTouramentActivityCard);
        liveMatchCv = findViewById(R.id.openOnGoingMatchActivityCard);
        earnLL = findViewById(R.id.openReferAndEarnActivityCard);
        resultCV = findViewById(R.id.openResultsActivityCard);
        profileIV = findViewById(R.id.openMyProfileActivityTV);
        myWalletLL = findViewById(R.id.openMyWalletActivityCard);
        youtubeLL = findViewById(R.id.openYoutubeChannelActivityCard);
        instagramLL = findViewById(R.id.openInstagramActivityCard);
        facebookLL = findViewById(R.id.openFaceBookActivityCard);
        shareLL = findViewById(R.id.openShareThisAppActivityCard);
        guidesLL = findViewById(R.id.openGuidanceActivityCard);
        aboutUsLL = findViewById(R.id.openAboutUsActivityCard);
        logoutIV = findViewById(R.id.openLogoutActivityTV);
        addMoneyLL = findViewById(R.id.openAddAmountActivityCard);

        coinsTV = findViewById(R.id.amountOfCoins);
        moneyTV = findViewById(R.id.amountOfRupee);
        coinIcon = findViewById(R.id.coinIcon);
        rupeeIcon = findViewById(R.id.rupeeIcon);

        /*webView = findViewById(R.id.webView);
        webView1 = findViewById(R.id.webView00);
        webView2 = findViewById(R.id.webView01);*/

        checkUserStatus();

        final Query usersQuery = databaseReference.orderByChild("email").equalTo(firebaseUser.getEmail());
        usersQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String userCoins = "" + ds.child("accountCoinBalance").getValue();
                    String userMoney = "" + ds.child("accountBalance").getValue();

                    coinsTV.setText(userCoins);
                    moneyTV.setText(userMoney + " Rs.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ////FROM
        // HERE
        Query linksQuery = detailReference.orderByChild("Links");
        linksQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    youtubeChannelLink = "" + ds.child("YouTubeChannel").getValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        linksQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    instagramLink = "" + ds.child("InstagramID").getValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        linksQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    facebookLink = "" + ds.child("FacebookPage").getValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        ////TO
        // HERE

        //Handles to open tournament activity...
        tournamentCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detector = new ConnectionDetector(MainContentActivity.this);
                if(!detector.isConnectedOrNot()) {
                    showAlertDialogForNoInternet();
                }else{
                    startActivity(new Intent(MainContentActivity.this, zTournamentActivity.class));
                }
            }
        });

        //Handles to open live Match activity...
        liveMatchCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detector = new ConnectionDetector(MainContentActivity.this);
                if(!detector.isConnectedOrNot()) {
                    showAlertDialogForNoInternet();
                }else{
                    startActivity(new Intent(MainContentActivity.this,zLiveMatchActivity.class));
                }
            }
        });

        //Handles to open refer and earn activity...
        earnLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detector = new ConnectionDetector(MainContentActivity.this);
                if(!detector.isConnectedOrNot()) {
                    showAlertDialogForNoInternet();
                }else{
                    startActivity(new Intent(MainContentActivity.this,zReferAndEarnActivity.class));
                }
            }
        });

        //Handles to open result activity...
        resultCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detector = new ConnectionDetector(MainContentActivity.this);
                if(!detector.isConnectedOrNot()) {
                    showAlertDialogForNoInternet();
                }else{
                    startActivity(new Intent(MainContentActivity.this,zResultsActivity.class));
                }
            }
        });

        //Handles to open my profile activity...
        profileIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detector = new ConnectionDetector(MainContentActivity.this);
                if(!detector.isConnectedOrNot()) {
                    showAlertDialogForNoInternet();
                }else{
                    startActivity(new Intent(MainContentActivity.this,zProfileActivity.class));
                }
            }
        });

        //Handles to open my wallet activity...
        myWalletLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detector = new ConnectionDetector(MainContentActivity.this);
                if(!detector.isConnectedOrNot()) {
                    showAlertDialogForNoInternet();
                }else{
                    startActivity(new Intent(MainContentActivity.this,zWalletActivity.class));
                }
            }
        });

        //Handles to open add money to account activity...
        addMoneyLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detector = new ConnectionDetector(MainContentActivity.this);
                if(!detector.isConnectedOrNot()) {
                    showAlertDialogForNoInternet();
                }else{
                    startActivity(new Intent(MainContentActivity.this, zAddMoneyActivity.class));
                }
            }
        });

        //Handles to open youtube activity...
        youtubeLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                webView.loadUrl(youtubeChannelLink);
            }
        });

        //Handles to open instagram activity...
        instagramLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                webView1.loadUrl(instagramLink);
            }
        });

        //Handles to open facebook activity...
        facebookLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                webView2.loadUrl(facebookLink);
            }
        });

        //Handles to open share this activity activity...
        shareLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //Handles to open guidance activity...
        guidesLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detector = new ConnectionDetector(MainContentActivity.this);
                if(!detector.isConnectedOrNot()) {
                    showAlertDialogForNoInternet();
                }else{
                    startActivity(new Intent(MainContentActivity.this,TCandGuideActivity.class));
                }
            }
        });

        //Handles to open about us activity...
        aboutUsLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detector = new ConnectionDetector(MainContentActivity.this);
                if(!detector.isConnectedOrNot()) {
                    showAlertDialogForNoInternet();
                }else{
                    startActivity(new Intent(MainContentActivity.this,zAboutUsActivity.class));
                }
            }
        });

        //Handles to open main activity by logout function...
        logoutIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detector = new ConnectionDetector(MainContentActivity.this);
                if(!detector.isConnectedOrNot()) {
                    showAlertDialogForNoInternet();
                }else{
                    showLogoutConfirmationDialog();
                }
            }
        });

        coinsTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainContentActivity.this, zAddMoneyActivity.class));
            }
        });

        moneyTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainContentActivity.this, zAddMoneyActivity.class));
            }
        });

        coinIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainContentActivity.this, zAddMoneyActivity.class));
            }
        });

        rupeeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainContentActivity.this, TryAnythingActivity .class));
            }
        });
    }

    private void showAlertDialogForNoInternet() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainContentActivity.this);
        builder.setTitle("No Internet Connection");
        builder.setMessage("Please Check Your Internet Connectivity. It seem not to be working.");
        builder.setCancelable(false);


        builder.setPositiveButton("GOT IT!", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int which){
                dialogInterface.dismiss();
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

    private void checkUserStatus(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null){
//            String uid = firebaseUser.getUid();
            //Stay in this activity only
        }else{
            startActivity(new Intent(MainContentActivity.this,MainActivity.class));
            finish();
        }
    }

    private void showLogoutConfirmationDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainContentActivity.this);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to Logout?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                firebaseAuth.signOut();
                checkUserStatus();
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

    //METHODS FOR THE ACTIVITY ARE HERE
    @Override
    protected void onStart() {
        checkUserStatus();
        super.onStart();
    }

    @Override
    protected void onResume() {
        checkUserStatus();
        super.onResume();
    }

    @Override
    protected void onRestart() {
        checkUserStatus();
        super.onRestart();
    }

    @Override
    protected void onPause() {
        checkUserStatus();
        super.onPause();
    }

    @Override
    public void onBackPressed() {

        if (backPressedTime + 2000 > System.currentTimeMillis()){
            backBtnToast.cancel();
            super.onBackPressed();
            return;
        }else{
            backBtnToast = Toast.makeText(this, "Press Again To Exit", Toast.LENGTH_SHORT);
            backBtnToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }

    /*//For Auto Image Flipper Animation....
    private void autoFlipperImageMethod(int image) {
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(image);

        viewFlipper.addView(imageView);
        viewFlipper.setFlipInterval(3000);
        viewFlipper.setAutoStart(true);

        viewFlipper.setInAnimation(this,android.R.anim.slide_out_right);
        viewFlipper.setOutAnimation(this,android.R.anim.slide_in_left);
    }*/

    /*FOR TESTING PURPOSE ONLY ELSE IT SHOULD BE COMMENT OUT.
    private void refreshForTestingDuringDevelopment() {
        if (!verify.equals("0")){

            final AlertDialog.Builder builder1 = new AlertDialog.Builder(MainContentActivity.this);
            builder1.setTitle("Welcome To Survival Era");
            builder1.setMessage("Please Read the full imformation if you haven'ttour.\n\n" +
                    "This is the test version of survival era tournament application thanks for seeking interest in our testing app." +
                    " You are given a limited time to use this app." +
                    "You can do anything you want but in this limited time.This alertDialog will be not shown in the published version.\n\n" +
                    "And don't press the Worst idea button below or your account will be suspended\n Thanks");
            builder1.setCancelable(false);
            builder1.setPositiveButton("Okay!", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    startActivity(new Intent(MainContentActivity.this,zTournamentActivity.class));
                }
            });

            builder1.setNegativeButton("Worst Idea!", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    HashMap<String,Object> hashMap1 = new HashMap<>();
                    hashMap1.put("accountBalance","0");
                    databaseReference.child(firebaseUser.getUid()).updateChildren(hashMap1);
                    Toast.makeText(MainContentActivity.this, "Thanks For Your Advise.", Toast.LENGTH_SHORT).show();
                }
            });
            builder1.create().show();
        }else{
            final AlertDialog.Builder builder1 = new AlertDialog.Builder(MainContentActivity.this);
            builder1.setTitle("Sorry To Say!");
            builder1.setMessage("Account Suspended Your Time Is Complete To Try This Trial version\n\n" +
                    "Thanks for trying this application but this is only for limited period testing usage app. " +
                    "So we are sending you to the main Avtivity.Thanks");
            builder1.setCancelable(false);
            builder1.setPositiveButton("GOT IT!", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    firebaseAuth.signOut();
                    checkUserStatus();
                }
            });

            builder1.create().show();
        }
    }
*/
}
