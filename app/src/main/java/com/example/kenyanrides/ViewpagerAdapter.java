package com.example.kenyanrides;

import android.content.Context;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ViewpagerAdapter extends PagerAdapter {

    private ArrayList<String> images;
    private LayoutInflater inflater;
    private Context context;

    public ViewpagerAdapter(ArrayList<String> images, Context context){

        this.images = images;
        this.context = context;

        inflater = LayoutInflater.from(context);

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View imageLayout = inflater.inflate(R.layout.view_pager_item, container, false);

        assert imageLayout != null;
        final ImageView imageView = (ImageView) imageLayout
                .findViewById(R.id.image);


        Glide.with(context).load(images.get(position)).into(imageView);

        container.addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(@Nullable Parcelable state, @Nullable ClassLoader loader) {

    }

    @Nullable
    @Override
    public Parcelable saveState() {
        return null;
    }
}
