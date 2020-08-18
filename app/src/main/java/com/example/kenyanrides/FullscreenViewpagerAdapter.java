package com.example.kenyanrides;

import android.app.Activity;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class FullscreenViewpagerAdapter extends PagerAdapter {

    private Activity _activity;
    private ArrayList<String> _imagePaths;
    LayoutInflater fullScreenInflater;

    // constructor
    public FullscreenViewpagerAdapter(Activity activity, ArrayList<String> imagePaths) {
        this._activity = activity;
        this._imagePaths = imagePaths;

        fullScreenInflater = LayoutInflater.from(_activity);
    }


    @Override
    public int getCount() {
        return this._imagePaths.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View imageLayout = fullScreenInflater.inflate(R.layout.layout_fullscreen_image, container, false);

        assert imageLayout != null;
        final ImageView imageView = (ImageView) imageLayout
                .findViewById(R.id.imgDisplay);

        ImageView btnClose = imageLayout.findViewById(R.id.btnClose);


        Glide.with(_activity).load(_imagePaths.get(position)).into(imageView);

        container.addView(imageLayout, 0);

        // close button click event
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _activity.finish();
            }
        });


        return imageLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        (container).removeView((RelativeLayout) object);

    }
}

