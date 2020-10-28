package com.gcodedevelopers.kenyanrides;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.viewpager.widget.PagerAdapter;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

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
        final ImageView imageView = imageLayout
                .findViewById(R.id.imgDisplay);

        ImageView btnClose = imageLayout.findViewById(R.id.btnClose);

        ProgressBar progressBar = imageLayout.findViewById(R.id.progress_bar);

        progressBar.setVisibility(View.VISIBLE);

        //Glide.with(_activity).load(_imagePaths.get(position)).into(imageView);
        Picasso.get().load(_imagePaths.get(position)).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                //check if bitmap is potrait
                if(bitmap.getHeight()>bitmap.getWidth()){

                    progressBar.setVisibility(View.INVISIBLE);
                    //load to imageview and set scale type to center crop
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    imageView.setImageBitmap(bitmap);

                }else {

                    progressBar.setVisibility(View.INVISIBLE);
                    //load to imageview and set scale type to fitxy
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    imageView.setImageBitmap(bitmap);

                }

            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });

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

