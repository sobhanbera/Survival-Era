package com.sbadc.survivalera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private TextView haveAnAccount;

    Animation animation1,animation2;

    //For Login Purpose
    private TextView otpSentText;
    private EditText mFullNameEt, mUserNameEt, mPhoneEt, mEmailEt, mPasswordEt, mConfirmPasswordEt,mOTPET;
    private Button mregisterBtn,mResendBtn;
    private ProgressDialog progressDialog;

    private int count = 60;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);


    //Creating a instance to firebase auth...
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        animation1 = AnimationUtils.loadAnimation(this,R.anim.animlefttoright);
        animation2 = AnimationUtils.loadAnimation(this,R.anim.animrighttoleft);

        mFullNameEt = findViewById(R.id.fullnameEt);
        mUserNameEt = findViewById(R.id.ingameusernameEt);
        mPhoneEt = findViewById(R.id.phonenoEt);
        mEmailEt = findViewById(R.id.emailEt);
        mPasswordEt = findViewById(R.id.passwordEt);
        mConfirmPasswordEt = findViewById(R.id.confirmPasswordEt);
        mOTPET = findViewById(R.id.OTPET);
        otpSentText = findViewById(R.id.otpSentToPhoneNumber);
        mregisterBtn = findViewById(R.id.registerBtn);
        mResendBtn = findViewById(R.id.resendBtn);
        haveAnAccount = findViewById(R.id.haveAnAccountAlready);

        mFullNameEt.startAnimation(animation1);
        mUserNameEt.startAnimation(animation2);
        mPhoneEt.startAnimation(animation1);
        mEmailEt.startAnimation(animation2);
        mPasswordEt.startAnimation(animation1);
        mConfirmPasswordEt.startAnimation(animation2);
        mOTPET.startAnimation(animation1);
        mResendBtn.startAnimation(animation1);
        mregisterBtn.startAnimation(animation2);
        haveAnAccount.startAnimation(animation2);

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Creating New Account....");

        //Handling the resend button and then register button inside it.....
        mResendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //creating a new variable for all user_details.
                String fullname = mFullNameEt.getText().toString();
                String username = mUserNameEt.getText().toString();
                String phoneno = mPhoneEt.getText().toString();
                String email = mEmailEt.getText().toString();
                String password = mPasswordEt.getText().toString();
                String confirmPassword = mConfirmPasswordEt.getText().toString();
                //checking whether all field are correctly filled or not...

                if(!VALID_EMAIL_ADDRESS_REGEX.matcher(email).find()){/*!Patterns.EMAIL_ADDRESS.matcher(email).matches()){*/
                    mEmailEt.setError("Invalid Email Id");
                    mEmailEt.setFocusable(true);
                } else if(password.length() < 8){
                    mPasswordEt.setError("Password must contain at least 8 characters.");
                    mPasswordEt.setFocusable(true);
                }else if(!confirmPassword.equals(password)){
                    mConfirmPasswordEt.setError("Password didn't match");
                    mConfirmPasswordEt.setFocusable(true);
                } else if(phoneno.length() < 10 || phoneno.length() > 10){
                    mPhoneEt.setError("Input correct phone number.");
                    mPhoneEt.setFocusable(true);
                } else if(fullname.length() < 8){
                    mFullNameEt.setError("Full name must contain 8 letter");
                    mFullNameEt.setFocusable(true);
                } else if(username.equals("")){
                    Toast.makeText(RegisterActivity.this, "All fields are compulsory to be filled", Toast.LENGTH_SHORT).show();
                } else if(username.contains("'")){
                    Toast.makeText(RegisterActivity.this, "Username shouldn't contain apostrophe s('s)", Toast.LENGTH_SHORT).show();
                } else{
                    disableEditTexts();
                    registerNewUser(fullname,username,phoneno,email,password);
                }
            }
        });

        haveAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    private void disableEditTexts() {
        mregisterBtn.setEnabled(true);
        mregisterBtn.setAlpha(1);
        mOTPET.setEnabled(true);

        mFullNameEt.setEnabled(false);
        mFullNameEt.setAlpha(0.5f);

        mUserNameEt.setEnabled(false);
        mUserNameEt.setAlpha(0.5f);

        mPhoneEt.setEnabled(false);
        mPhoneEt.setAlpha(0.5f);

        mEmailEt.setEnabled(false);
        mEmailEt.setAlpha(0.5f);

        mPasswordEt.setEnabled(false);
        mPasswordEt.setAlpha(0.5f);

        mConfirmPasswordEt.setEnabled(false);
        mConfirmPasswordEt.setAlpha(0.5f);
    }

    private void registerNewUser(final String fullname, final String username, final String phoneno, final String email, final String password) {
//        progressDialog.show();

        firebaseAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                if(task.isSuccessful()){
                    if(task.getResult().getSignInMethods().isEmpty()){

                        sendOTP();
                        otpSentText.setText("Verification code has been sent to +91"+mPhoneEt.getText().toString());
                        final Timer timer = new Timer();
                        timer.scheduleAtFixedRate(new TimerTask() {
                            @Override
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(count == 0){
                                            mResendBtn.setText("RESEND");
                                            mResendBtn.setEnabled(true);
                                            mResendBtn.setAlpha(1);
                                        }else{
                                            mResendBtn.setText("Resend in "+count);
                                            mResendBtn.setEnabled(false);
                                            mResendBtn.setAlpha(0.5f);
                                            count--;
                                        }
                                    }
                                });
                            }
                        },0,1000);
                        mResendBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                resendOTP();
                                mResendBtn.setEnabled(false);
                                mResendBtn.setAlpha(0.5f);
                                count = 60;
                            }
                        });

                        mregisterBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(mOTPET.getText() == null ||mOTPET.getText().toString().isEmpty()){
                                    return;
                                }
                                mOTPET.setError(null);
                                String code = mOTPET.getText().toString();
                                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId,code);
                                signInWithPhoneAuthCredential(credential,fullname,username,phoneno,email,password);
                                progressDialog.show();
                                timer.cancel();
                            }
                        });

                    }else{
                        mEmailEt.setError("Email already exists!");
                        mEmailEt.setFocusable(true);
                        progressDialog.dismiss();
                    }
                }else{
                    String error = task.getException().getMessage();
                    Toast.makeText(RegisterActivity.this, error, Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }
        });

    }

    private void sendOTP(){

        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                //Log.d(TAG, "onVerificationCompleted:" + credential);

//                        signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
//                        Log.w(TAG, "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    mOTPET.setError(e.getMessage());
                    mOTPET.setFocusable(true);
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    mOTPET.setError(e.getMessage());
                    mOTPET.setFocusable(true);
                }
                progressDialog.dismiss();

                // Show a message and update the UI
                // ...
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
//                        Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;

                // ...
            }
        };

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91"+mPhoneEt.getText().toString(),
                60,
                TimeUnit.SECONDS,
                this,
                mCallback);
    }

    private void resendOTP(){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91"+mPhoneEt.getText().toString(),
                60,
                TimeUnit.SECONDS,
                this,
                mCallback,mResendToken);
    }

    private void signInWithPhoneAuthCredential(final PhoneAuthCredential credential,final String fullname, final String username, final String phoneno, final String email, final String password) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            AuthCredential authCredential = EmailAuthProvider.getCredential(email,password);
                            user.linkWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){

                                        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

                                        String Email = currentUser.getEmail();
                                        String Uid = currentUser.getUid();
                                        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
                                        String registeredDateAndTime = DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString();
                                        int zeroValue = 0;
                                        int initialAccountValue = 5;
                                        int initialAccountCoinsValue = 0;
                                        String zeroStringValue = String.valueOf(zeroValue);
                                        String initialAccountStringValue = String.valueOf(initialAccountValue);
                                        String initialAccountCoinStringValue = String.valueOf(initialAccountCoinsValue);


                                        //HashMap for user details.....
                                        HashMap<Object,String> hashMap = new HashMap<>();
                                        hashMap.put("email",Email);
                                        hashMap.put("uid",Uid);
                                        hashMap.put("fullname",fullname);
                                        hashMap.put("username",username);
                                        hashMap.put("phoneNumber",phoneno);
                                        hashMap.put("joinedOn",registeredDateAndTime);
                                        hashMap.put("matchWon",zeroStringValue);
                                        hashMap.put("matchPlayed",zeroStringValue);
                                        hashMap.put("totalKills",zeroStringValue);
                                        hashMap.put("accountBalance",initialAccountStringValue);
                                        hashMap.put("accountCoinBalance",initialAccountCoinStringValue);
                                        hashMap.put("donatedAmount","0");

                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        DatabaseReference reference = database.getReference("Users");
                                        reference.child(Uid).setValue(hashMap);

                                        //To close the timer...
//                                        timer.cancel();

                                        progressDialog.dismiss();
                                        startActivity(new Intent(RegisterActivity.this, MainContentActivity.class));
                                        finish();
                                    }else{
                                        String error = task.getException().getMessage();
                                        Toast.makeText(RegisterActivity.this, error, Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }
                                }
                            });
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
//                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                mOTPET.setError("Invalid OTP");
                                mOTPET.setFocusable(true);
                            }
                            progressDialog.dismiss();
                        }
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onDestroy() {
//        timer.cancel();
        super.onDestroy();
    }
}
