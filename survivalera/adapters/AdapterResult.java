package com.sbadc.survivalera.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sbadc.survivalera.R;
import com.sbadc.survivalera.ResultLeaderboardActivity;
import com.sbadc.survivalera.models.ModelResult;

import java.util.List;

public class AdapterResult extends RecyclerView.Adapter<AdapterResult.MyHolder> {


    Context context;
    List<ModelResult> resultList;

    public AdapterResult(Context context, List<ModelResult> resultList) {
        this.context = context;
        this.resultList = resultList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_result,parent,false);

        return new MyHolder(view);
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int position) {
        String uid = resultList.get(position).getUid();
        String phone = resultList.get(position).getPhone();
        String email = resultList.get(position).getEmail();
        String tourID = resultList.get(position).getTourID();
        String tourImage = resultList.get(position).getTourImage();
        String tourName = resultList.get(position).getTourName();
        String tourDate = resultList.get(position).getTourDate();
        String tourTime = resultList.get(position).getTourTime();
        String tourWinningPrize = resultList.get(position).getTourWinningPrize();
        String tourPerKillPrize = resultList.get(position).getTourPerKillPrize();
        String tourEntryFee = resultList.get(position).getTourEntryFee();
        String tourVersion = resultList.get(position).getTourVersion();
        String tourType = resultList.get(position).getTourType();
        String tourMap = resultList.get(position).getTourMap();
        final String tourVideoLink = resultList.get(position).getTourVideoLink();
        final String tourCreatedTime = resultList.get(position).getTourCreatedTime();
        String tourResultVerified = resultList.get(position).getResultVerified();


        holder.tourNameTV.setText(tourName);
        holder.tourDateTV.setText(tourDate);
        holder.tourTimeTV.setText(tourTime);
        holder.tourWinningPrizeTV.setText(tourWinningPrize);
        holder.tourPerKillPrizeTV.setText(tourPerKillPrize);
        holder.tourEntryTV.setText(tourEntryFee);
        holder.tourVersionTV.setText(tourVersion);
        holder.tourTypeTV.setText(tourType);
        holder.tourMapTV.setText(tourMap);

        if(tourResultVerified.equals("yes")){
            holder.watchMatchBtn.setEnabled(true);
            holder.watchMatchBtn.setVisibility(View.VISIBLE);
            holder.watchMatchBtn.setAlpha(1);
            holder.watchMatchBtn.setText("Watch");

            holder.leaderBoardBtn.setEnabled(true);
            holder.leaderBoardBtn.setVisibility(View.VISIBLE);
            holder.leaderBoardBtn.setAlpha(1);
            holder.leaderBoardBtn.setText("LeaderBoard");
        }else{
            holder.watchMatchBtn.setEnabled(false);
            holder.watchMatchBtn.setVisibility(View.GONE);
            holder.watchMatchBtn.setAlpha(0);
            holder.watchMatchBtn.setText("");

            holder.leaderBoardBtn.setEnabled(true);
            holder.leaderBoardBtn.setVisibility(View.VISIBLE);
            holder.leaderBoardBtn.setAlpha(0.5f);
            holder.leaderBoardBtn.setText("Coming Soon");
        }

        holder.watchMatchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tourVideoLink.isEmpty()) {
                    holder.webView.loadUrl(tourVideoLink);
                }else{
                    Toast.makeText(context, "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.leaderBoardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ResultLeaderboardActivity.class);
                context.startActivity(intent);
                intent.putExtra("resultId",tourCreatedTime);
            }
        });
    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{

        TextView tourNameTV,tourDateTV,tourTimeTV,
                tourWinningPrizeTV,tourPerKillPrizeTV,tourEntryTV,
                tourVersionTV,tourTypeTV,tourMapTV;

        Button watchMatchBtn,leaderBoardBtn;
        WebView webView;

        public MyHolder(@NonNull View itemView){
            super(itemView);

            tourNameTV = itemView.findViewById(R.id.tournamentCardGameName);
            tourDateTV = itemView.findViewById(R.id.tournamentCardDate);
            tourTimeTV = itemView.findViewById(R.id.tournamentCardTime);
            tourWinningPrizeTV = itemView.findViewById(R.id.tournamentCardWinningPrize);
            tourPerKillPrizeTV = itemView.findViewById(R.id.tournamentCardPerKillPrize);
            tourMapTV = itemView.findViewById(R.id.tournamentCardMap);
            tourVersionTV = itemView.findViewById(R.id.tournamentCardVersion);
            tourTypeTV = itemView.findViewById(R.id.tournamentCardMatchType);
            tourEntryTV = itemView.findViewById(R.id.tournamentCardEntryPrize);

            watchMatchBtn = itemView.findViewById(R.id.watchCompletedMatchFromResultBtn);
            leaderBoardBtn = itemView.findViewById(R.id.resultLeaderBoardBtn);

            webView = itemView.findViewById(R.id.webViewMatchComplete);
        }
    }
}
