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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sbadc.survivalera.adapters.AdapterResult;
import com.sbadc.survivalera.models.ModelResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class zResultsActivity extends AppCompatActivity {


    FirebaseAuth firebaseAuth;

    RecyclerView recyclerView;
    List<ModelResult> resultList;
    AdapterResult adapterResult;

    ProgressDialog progressDialog;

    LinearLayout linearLayout;
    Button refreshBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_z_results);

        firebaseAuth = FirebaseAuth.getInstance();
        recyclerView = findViewById(R.id.recyclerViewOfList);

        linearLayout = findViewById(R.id.linearLayout);
        refreshBtn = findViewById(R.id.refreshBtn);

        progressDialog = new ProgressDialog(zResultsActivity.this);
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

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Results");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);

        recyclerView.setLayoutManager(layoutManager);

        resultList = new ArrayList<>();

        RefreshResultsActivity();
        loadResults();
        checkUserStatus();

        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RefreshResultsActivity();
            }
        });

    }

    private void RefreshResultsActivity() {
        loadResults();
        if (resultList.isEmpty()){
            recyclerView.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);
        }else {
            recyclerView.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.GONE);
        }
    }

    private void checkUserStatus(){
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null){
            String uid = user.getUid();
            //Stay in this activity only
        }else{
            startActivity(new Intent(zResultsActivity.this,MainActivity.class));
            finish();
        }
    }

    private void loadResults() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Results");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                resultList.clear();
                for (DataSnapshot ds:dataSnapshot.getChildren()){
                    ModelResult modelResult = ds.getValue(ModelResult.class);

                    resultList.add(modelResult);

                    adapterResult = new AdapterResult(zResultsActivity.this,resultList);

                    recyclerView.setAdapter(adapterResult);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(zResultsActivity.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        RefreshResultsActivity();
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
