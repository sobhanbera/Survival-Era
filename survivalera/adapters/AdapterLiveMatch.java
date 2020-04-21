package com.sbadc.survivalera.adapters;

import android.content.Context;
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
import com.sbadc.survivalera.models.ModelLiveMatch;

import java.util.List;

public class AdapterLiveMatch extends RecyclerView.Adapter<AdapterLiveMatch.MyHolder> {

    Context context;
    List<ModelLiveMatch> liveMatchList;

    public AdapterLiveMatch(Context context, List<ModelLiveMatch> liveMatchList) {
        this.context = context;
        this.liveMatchList = liveMatchList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_live_match,parent,false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int position) {
        String uid = liveMatchList.get(position).getUid();
        String phone = liveMatchList.get(position).getPhone();
        String email = liveMatchList.get(position).getEmail();
        String tourID = liveMatchList.get(position).getTourID();
        String tourImage = liveMatchList.get(position).getTourImage();
        String tourName = liveMatchList.get(position).getTourName();
        String tourDate = liveMatchList.get(position).getTourDate();
        String tourTime = liveMatchList.get(position).getTourTime();
        String tourWinningPrize = liveMatchList.get(position).getTourWinningPrize();
        String tourPerKillPrize = liveMatchList.get(position).getTourPerKillPrize();
        String tourEntryFee = liveMatchList.get(position).getTourEntryFee();
        String tourVersion = liveMatchList.get(position).getTourVersion();
        String tourType = liveMatchList.get(position).getTourType();
        String tourMap = liveMatchList.get(position).getTourMap();
        String tourCreatedTime = liveMatchList.get(position).getTourCreatedTime();
        final String livevideoLink = liveMatchList.get(position).getTourVideoLink();
        final String tourLiveMatchVerified = liveMatchList.get(position).getIsVerified();


        /*Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(tourDate));
        String formatedDate = DateFormat.format("dd/MM/yyyy",calendar).toString();

        Calendar calendar2 = Calendar.getInstance(Locale.getDefault());
        calendar2.setTimeInMillis(Long.parseLong(tourTime));
        String formatedTime = DateFormat.format("hh:mm aa",calendar2).toString();*/

        holder.tourNameTV.setText(tourName);
        holder.tourDateTV.setText(tourDate);
        holder.tourTimeTV.setText(tourTime);
        holder.tourWinningPrizeTV.setText(tourWinningPrize);
        holder.tourPerKillPrizeTV.setText(tourPerKillPrize);
        holder.tourEntryTV.setText(tourEntryFee);
        holder.tourVersionTV.setText(tourVersion);
        holder.tourTypeTV.setText(tourType);
        holder.tourMapTV.setText(tourMap);

        holder.watchLiveMatchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!livevideoLink.isEmpty()) {
                    holder.webView.loadUrl(livevideoLink);
                }else{
                    Toast.makeText(context, "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if(tourLiveMatchVerified.equals("yes")){
            holder.watchLiveMatchBtn.setEnabled(true);
            holder.watchLiveMatchBtn.setVisibility(View.VISIBLE);
            holder.watchLiveMatchBtn.setAlpha(1);
            holder.watchLiveMatchBtn.setText("Watch Live");
        }else{
            holder.watchLiveMatchBtn.setEnabled(false);
            holder.watchLiveMatchBtn.setVisibility(View.VISIBLE);
            holder.watchLiveMatchBtn.setAlpha(0.5f);
            holder.watchLiveMatchBtn.setText("Live Stream Soon");
        }

    }


    @Override
    public int getItemCount() {
        return liveMatchList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{

        TextView tourNameTV,tourDateTV,tourTimeTV,
                tourWinningPrizeTV,tourPerKillPrizeTV,tourEntryTV,
                tourVersionTV,tourTypeTV,tourMapTV;

        Button watchLiveMatchBtn;
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

            watchLiveMatchBtn = itemView.findViewById(R.id.watchLiveMatchBtn);

            webView = itemView.findViewById(R.id.webViewInLiveMatchRow);

        }
    }

}
