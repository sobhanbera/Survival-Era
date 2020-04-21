package com.sbadc.survivalera.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.AnimatorRes;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.sbadc.survivalera.R;

public class SliderAdapter extends PagerAdapter {

    private Context context;

    public SliderAdapter(Context context){
        this.context = context;
    }

    private int[] sliderImages = {R.drawable.register_image,
            R.drawable.price_image,
            R.drawable.live_image_image,
            R.drawable.refer_and_earn_image};

    private String[] sliderHeadings = {"Register", "Play Tournaments","Watch Live Match","Refer And Earn"};

    private String[] sliderDescriptions = {"Register now and get extra benefits of a new account easily",
            "Take part in every matches to win mammoth prices",
            "Watch live tournament of every matches online on youtube,facebook,instagram,twitch and in many more platforms",
            "You Can Also Earn By Referring The App To Others You Can Get Upto 10 Rs. By Referring"};


    @Override
    public int getCount() {
        return sliderHeadings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (ConstraintLayout)object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert layoutInflater != null;
        View view = layoutInflater.inflate(R.layout.slider,container,false);


        Animation animation01 = AnimationUtils.loadAnimation(context, R.anim.animslow);
        Animation animation02 = AnimationUtils.loadAnimation(context,R.anim.animmedium);
        Animation animation03 = AnimationUtils.loadAnimation(context,R.anim.animfast);
        ImageView sliderImage = view.findViewById(R.id.imageView2);
        TextView sliderHeading = view.findViewById(R.id.textView2);
        TextView sliderDescription = view.findViewById(R.id.textView8);
        ConstraintLayout constraintLayout = view.findViewById(R.id.layoutBack);

        sliderImage.startAnimation(animation01);
        sliderHeading.startAnimation(animation02);
        sliderDescription.startAnimation(animation03);

        sliderImage.setImageResource(sliderImages[position]);
        sliderHeading.setText(sliderHeadings[position]);
        sliderDescription.setText(sliderDescriptions[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout)object);
    }
}
