package com.sbadc.survivalera;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.material.tabs.TabLayout;
import com.sbadc.survivalera.adapters.IntroViewPagerAdapter;
import com.sbadc.survivalera.models.ScreenItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.iwgang.countdownview.CountdownView;

public class TryAnythingActivity extends AppCompatActivity {

    private ViewPager viewPager;
    IntroViewPagerAdapter introViewPagerAdapter;
    TabLayout tabLayout;
    Button nextBtn, getStartedBtn;
    LinearLayout linearLayoutNext, linearLayoutGetStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //fullscreen request
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (restorePreData()){
            Intent intent = new Intent(getApplicationContext(), MainContentActivity.class);
            startActivity(intent);
            finish();
        }
        setContentView(R.layout.activity_try_anything);

        nextBtn = findViewById(R.id.btn_next);
        getStartedBtn = findViewById(R.id.btn_get_started);

        linearLayoutNext = findViewById(R.id.linear_layout_next);
        linearLayoutGetStarted = findViewById(R.id.linear_layout_get_started);
        tabLayout = findViewById(R.id.tab_indicator);

        final List<ScreenItem> screenItemList = new ArrayList<>();
        screenItemList.add(new ScreenItem("Welcome To Survival Era","We wlcome you to survival era tournament application",R.drawable.seneon));
        screenItemList.add(new ScreenItem("Play In Tournaments","Win paytm money right after match ends to your paytm wallet.",R.drawable.seneon));
        screenItemList.add(new ScreenItem("Welcome To Survival Era","Watch the live streams online and also play live",R.drawable.seneon));

        //setting up view pager
        viewPager = findViewById(R.id.screen_viewpagger);
        introViewPagerAdapter = new IntroViewPagerAdapter(this,screenItemList);
        viewPager.setAdapter(introViewPagerAdapter);

        //setting up indicator
        tabLayout.setupWithViewPager(viewPager);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(viewPager.getCurrentItem()+1,true);
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition()==screenItemList.size()-1){
                    loadLastScreen();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        getStartedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainContentActivity.class);
                startActivity(intent);
                savePrefsData();
                finish();
            }
        });
    }

    private boolean restorePreData(){
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        Boolean isIntroActivityOpenedBefore = preferences.getBoolean("isIntroOpened",false);
        return isIntroActivityOpenedBefore;
    }

    private void savePrefsData(){
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isIntroOpened",true);
        editor.apply();
    }

    private void loadLastScreen(){
        linearLayoutNext.setVisibility(View.INVISIBLE);
        linearLayoutGetStarted.setVisibility(View.VISIBLE);
    }
}
