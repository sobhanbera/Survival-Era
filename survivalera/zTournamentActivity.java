package com.sbadc.survivalera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sbadc.survivalera.adapters.AdapterTournament;
import com.sbadc.survivalera.extraclasses.LoadingDialog;
import com.sbadc.survivalera.models.ModelTournament;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class zTournamentActivity extends AppCompatActivity {


    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference detailReference;

    RecyclerView recyclerView;
    List<ModelTournament> tournamentList;
    AdapterTournament adapterTournament;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_z_tournament);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Tournaments");
        /*Resources resources = getResources();
        Drawable textColor = resources.getDrawable(R.drawable.up_arrow_show);
        actionBar.setBackgroundDrawable(textColor);*/
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        detailReference = firebaseDatabase.getReference("App_Details");

        progressDialog = new ProgressDialog(zTournamentActivity.this);
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

        recyclerView = findViewById(R.id.recyclerViewOfList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);

        recyclerView.setLayoutManager(layoutManager);

        tournamentList = new ArrayList<>();

        loadTournaments();

        /*Query booleanQuery = detailReference.orderByChild("Booleans");
        booleanQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    String TOUR_AVAILABLE_OR_NOT = "" + ds.child("TourAvail").getValue();
                    if (TOUR_AVAILABLE_OR_NOT.equals("yes")){
                        recyclerView.setVisibility(View.VISIBLE);
                        linearLayout.setVisibility(View.GONE);


                        *//*recyclerView.setVisibility(View.GONE);
                        linearLayout.setVisibility(View.VISIBLE);*//*
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

        checkUserStatus();

    }

    private void checkUserStatus(){
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null){
            String uid = user.getUid();
            //Stay in this activity only
        }else{
            startActivity(new Intent(zTournamentActivity.this,MainActivity.class));
            finish();
        }
    }

    private void loadTournaments() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Tournaments");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tournamentList.clear();
                for (DataSnapshot ds:dataSnapshot.getChildren()){
                    ModelTournament modelTournament = ds.getValue(ModelTournament.class);

                    tournamentList.add(modelTournament);

                    adapterTournament = new AdapterTournament(zTournamentActivity.this,tournamentList);

                    recyclerView.setAdapter(adapterTournament);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(zTournamentActivity.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}

