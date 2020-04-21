package com.sbadc.survivalera;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sbadc.survivalera.adapters.SliderAdapter;

public class MainActivity extends AppCompatActivity {

    ViewPager viewPager;
    private LinearLayout dotLinearLayout;
    private SliderAdapter sliderAdapter;
    private TextView[] mDots;
    private Button nextBtn,backBtn;
    private int currentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //For SLider In First Menu Page
        viewPager = findViewById(R.id.viewPager);
        dotLinearLayout = findViewById(R.id.dotLinearLayout);
        nextBtn = findViewById(R.id.nextBtn);
        backBtn = findViewById(R.id.previousBtn);
        sliderAdapter = new SliderAdapter(this);
        viewPager.setAdapter(sliderAdapter);
        addDotsIndicators(0);
        viewPager.addOnPageChangeListener(viewListner);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(currentPage + 1);
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(currentPage - 1);
            }
        });
    }

    public void addDotsIndicators(int position){
        mDots = new TextView[4];
        dotLinearLayout.removeAllViews();

        for (int i = 0;i<mDots.length;i++){
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8259"));
            mDots[i].setTextSize(25);
            mDots[i].setTextColor(getResources().getColor(R.color.colorGradientForGoldenEnd));
            dotLinearLayout.addView(mDots[i]);
        }

        if (mDots.length > 0){
            mDots[position].setTextColor(getResources().getColor(R.color.colorBlack));
        }
    }

    ViewPager.OnPageChangeListener viewListner = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDotsIndicators(position);
            currentPage = position;

            if (position == 0){
                nextBtn.setEnabled(true);
                backBtn.setEnabled(false);
                backBtn.setVisibility(View.INVISIBLE);
                nextBtn.setText("NEXT");
                backBtn.setText("");
            }else if(position == mDots.length - 1){
                nextBtn.setEnabled(true);
                backBtn.setEnabled(true);
                backBtn.setVisibility(View.VISIBLE);
                nextBtn.setText("FINISH");
                backBtn.setText("PREVIOUS");
                nextBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(MainActivity.this,RegisterActivity.class));
                    }
                });
            }else {
                nextBtn.setEnabled(true);
                backBtn.setEnabled(true);
                backBtn.setVisibility(View.VISIBLE);

                nextBtn.setText("NEXT");
                backBtn.setText("PREVIOUS");
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}
