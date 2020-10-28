package com.gcodedevelopers.kenyanrides;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

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

        ProgressBar progressBar = imageLayout.findViewById(R.id.progress_bar);

        progressBar.setVisibility(View.VISIBLE);


        //Glide.with(context).load(images.get(position)).into(imageView);
        Picasso.get().load(images.get(position)).into(new Target() {
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

        imageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), FullscreenViewpagerActivity.class);

                intent.putExtra("Image1", images.get(0));
                intent.putExtra("Image2", images.get(1));
                intent.putExtra("Image3", images.get(2));
                intent.putExtra("Image4", images.get(3));
                intent.putExtra("Image5", images.get(4));
                intent.putExtra("clicked_position", position);

                view.getContext().startActivity(intent);
            }
        });

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
