package com.sbadc.survivalera.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.sbadc.survivalera.R;
import com.sbadc.survivalera.models.ScreenItem;

import java.util.List;

public class IntroViewPagerAdapter extends PagerAdapter {

    Context context;
    List<ScreenItem> screenItemList;

    public IntroViewPagerAdapter(Context context, List<ScreenItem> screenItemList) {
        this.context = context;
        this.screenItemList = screenItemList;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View layoutScreen = inflater.inflate(R.layout.slider_screen,null);

        ImageView imgSlider = layoutScreen.findViewById(R.id.img_intro);
        TextView tittle = layoutScreen.findViewById(R.id.tv_tittle);
        TextView description = layoutScreen.findViewById(R.id.tv_desc);

        tittle.setText(screenItemList.get(position).getTittle());
        description.setText(screenItemList.get(position).getDescription());
        imgSlider.setImageResource(screenItemList.get(position).getScreenimg());

        container.addView(layoutScreen);

        return layoutScreen;

    }

    @Override
    public int getCount(){
        return screenItemList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o){
        return view == o;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object){
        container.removeView((View) object);
    }

}
