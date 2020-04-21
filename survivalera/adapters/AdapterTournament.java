package com.sbadc.survivalera.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.sbadc.survivalera.R;
import com.sbadc.survivalera.TournamentDetailsActivity;
import com.sbadc.survivalera.models.ModelTournament;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.iwgang.countdownview.CountdownView;


public class AdapterTournament extends RecyclerView.Adapter<AdapterTournament.MyHolder>{

    private Context context;
    private List<ModelTournament> tournamentList;

    public AdapterTournament(Context context, List<ModelTournament> tournamentList) {
        this.context = context;
        this.tournamentList = tournamentList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_tournament,parent,false);

        return new MyHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int position) {
//        String uid = tournamentList.get(position).getUid();
//        String phone = tournamentList.get(position).getPhone();
//        String email = tournamentList.get(position).getEmail();
        final String tourID = tournamentList.get(position).getTourID();
        final String tourImage = tournamentList.get(position).getTourImage();
        String tourName = tournamentList.get(position).getTourName();
        String tourDate = tournamentList.get(position).getTourDate();
        String tourTime = tournamentList.get(position).getTourTime();
        String tourWinningPrize = tournamentList.get(position).getTourWinningPrize();
        String tourPerKillPrize = tournamentList.get(position).getTourPerKillPrize();
        String tourEntryFee = tournamentList.get(position).getTourEntryFee();
        String tourVersion = tournamentList.get(position).getTourVersion();
        String tourType = tournamentList.get(position).getTourType();
        String tourMap = tournamentList.get(position).getTourMap();
//        String tourCreatedTime = tournamentList.get(position).getTourCreatedTime();
        String tourVerified = tournamentList.get(position).getTourVerified();
//        String extraAdvice = tournamentList.get(position).getExtraAdvice();
        final String playerJoined = tournamentList.get(position).getPlayerJoined();
        final String totalPlayer = tournamentList.get(position).getTotalPlayer();
//        String perfectTime = tournamentList.get(position).getPerfectTime();
/*

        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(tourDate));
        final String formatedDate = android.text.format.DateFormat.format("dd/MM/yyy", calendar).toString();

        Calendar calendar2 = Calendar.getInstance(Locale.getDefault());
        calendar2.setTimeInMillis(Long.parseLong(tourDate));
        final String formatedTime = android.text.format.DateFormat.format("hh:mm aa", calendar2).toString();
*/

        holder.tourNameTV.setText(tourName);
        holder.tourDateTV.setText("Date: "+tourDate);
        holder.tourTimeTV.setText("Time: "+tourTime);
        holder.tourWinningPrizeTV.setText(tourWinningPrize + " Rs.");
        holder.tourPerKillPrizeTV.setText(tourPerKillPrize + " Rs.");
        holder.tourEntryTV.setText(tourEntryFee + " Rs.");
        holder.tourVersionTV.setText(tourVersion);
        holder.tourTypeTV.setText(tourType);
        holder.tourMapTV.setText(tourMap);
        holder.playersJoinedTV.setText("Players Joined : " + playerJoined + " / " + totalPlayer);

        if(tourImage.equals("noImage")){
            holder.tournamentCardIV.setVisibility(View.GONE);
        }else{
            try{
                Picasso.get().load(tourImage).placeholder(R.drawable.rupee_60px).into(holder.tournamentCardIV);
            }catch (Exception e){
                Toast.makeText(context, "Can't load the image.", Toast.LENGTH_SHORT).show();
            }
        }

        /*if(!(extraAdvice.length() == 0)){
            holder.extraAdviseTv.setVisibility(View.VISIBLE);
            holder.extraAdviseTv.setText(extraAdvice);
            holder.extraAdviseTv.setAlpha(1);
        }else{
            holder.extraAdviseTv.setAlpha(0);
            holder.extraAdviseTv.setVisibility(View.GONE);
        }*/

        if(tourVerified.equals("yes")){
            holder.joinTournamentButton.setEnabled(true);
            holder.joinTournamentButton.setText("Join");
        }else{
            holder.joinTournamentButton.setEnabled(false);
            holder.joinTournamentButton.setAlpha(0.5f);
            holder.joinTournamentButton.setText("Coming Soon");
        }

        if (Integer.parseInt(playerJoined) >= Integer.parseInt(totalPlayer)){
            holder.joinTournamentButton.setEnabled(false);
            holder.joinTournamentButton.setAlpha(0.7f);
            holder.joinTournamentButton.setText("Full");
        }

        /*HERE
        OTHER
        FUNCTIONALITY
        WILL
        COME
        FROM
        SURVIVAL
        ERA
        ADMIN
        APP
        PROJECT*/

        holder.joinTournamentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TournamentDetailsActivity.class);
                intent.putExtra("tourID",tourID);

                Pair[] pairs = new Pair[11];
                pairs[0] = new Pair<View, String >(holder.tournamentCardIV,"tourImage");
                pairs[1] = new Pair<View, String >(holder.tourNameTV,"tourName");
                pairs[2] = new Pair<View, String >(holder.tourDateTV,"tourDate");
                pairs[3] = new Pair<View, String >(holder.tourTimeTV,"tourTime");
                pairs[4] = new Pair<View, String >(holder.winningCV,"winningTran");
                pairs[5] = new Pair<View, String >(holder.perkillCv,"perkillTran");
                pairs[6] = new Pair<View, String >(holder.entryCV,"entryTran");
                pairs[7] = new Pair<View, String >(holder.mapCV,"mapTran");
                pairs[8] = new Pair<View, String >(holder.versionCV,"versionTran");
                pairs[9] = new Pair<View, String >(holder.typeCV,"typeTran");
                pairs[10] = new Pair<View, String >(holder.joinTournamentButton,"tourJoin");

                ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation((Activity)context,pairs);
                context.startActivity(intent,activityOptions.toBundle());
            }
        });

    }

    @Override
    public int getItemCount() {
        return tournamentList.size();
    }

    static class MyHolder extends RecyclerView.ViewHolder{

        ImageView tournamentCardIV;
        TextView tourNameTV,tourDateTV,tourTimeTV,
                tourWinningPrizeTV,tourPerKillPrizeTV,tourEntryTV,
                tourVersionTV,tourTypeTV,tourMapTV,playersJoinedTV,extraAdviseTv;

        Button joinTournamentButton;

        //for using shared animation this code should be used.....
        CardView winningCV,perkillCv,entryCV,mapCV,versionCV,typeCV;

        MyHolder(@NonNull View itemView){
            super(itemView);

            tournamentCardIV = itemView.findViewById(R.id.tournamentCardImage);

            tourNameTV = itemView.findViewById(R.id.tournamentCardGameName);
            tourDateTV = itemView.findViewById(R.id.tournamentCardDate);
            tourTimeTV = itemView.findViewById(R.id.tournamentCardTime);
            tourWinningPrizeTV = itemView.findViewById(R.id.tournamentCardWinningPrize);
            tourPerKillPrizeTV = itemView.findViewById(R.id.tournamentCardPerKillPrize);
            tourMapTV = itemView.findViewById(R.id.tournamentCardMap);
            tourVersionTV = itemView.findViewById(R.id.tournamentCardVersion);
            tourTypeTV = itemView.findViewById(R.id.tournamentCardMatchType);
            tourEntryTV = itemView.findViewById(R.id.tournamentCardEntryPrize);
            playersJoinedTV = itemView.findViewById(R.id.playerJoinedTv);
//            extraAdviseTv = itemView.findViewById(R.id.extraAdviseTv);

            joinTournamentButton = itemView.findViewById(R.id.joinMatchBtn);

            winningCV = itemView.findViewById(R.id.winningPrizeCV);
            perkillCv = itemView.findViewById(R.id.perKillCV);
            entryCV = itemView.findViewById(R.id.entryCV);
            mapCV = itemView.findViewById(R.id.mapCV);
            versionCV = itemView.findViewById(R.id.versionCV);
            typeCV = itemView.findViewById(R.id.typeCV);
        }
    }

}
