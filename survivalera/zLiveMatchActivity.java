package com.sbadc.survivalera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sbadc.survivalera.adapters.AdapterLiveMatch;
import com.sbadc.survivalera.extraclasses.LoadingDialog;
import com.sbadc.survivalera.models.ModelLiveMatch;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class zLiveMatchActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;

    RecyclerView recyclerView;
    List<ModelLiveMatch> liveMatchList;
    AdapterLiveMatch adapterLiveMatch;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_z_live_match);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Live Streams");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();
        recyclerView = findViewById(R.id.recyclerViewOfList);

        progressDialog = new ProgressDialog(zLiveMatchActivity.this);
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

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);

        recyclerView.setLayoutManager(layoutManager);

        liveMatchList = new ArrayList<>();

        loadLiveMatch();

        checkUserStatus();

    }

    private void checkUserStatus(){
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null){
            String uid = user.getUid();
            //Stay in this activity only
        }else{
            startActivity(new Intent(zLiveMatchActivity.this,MainActivity.class));
            finish();
        }
    }

    private void loadLiveMatch() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Live_Matches");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                liveMatchList.clear();
                for (DataSnapshot ds:dataSnapshot.getChildren()){
                    ModelLiveMatch modelLiveMatch = ds.getValue(ModelLiveMatch.class);

                    liveMatchList.add(modelLiveMatch);

                    adapterLiveMatch = new AdapterLiveMatch(zLiveMatchActivity.this,liveMatchList);

                    recyclerView.setAdapter(adapterLiveMatch);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(zLiveMatchActivity.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
