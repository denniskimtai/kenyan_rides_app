package com.gcodedevelopers.kenyanrides;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.os.Handler;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class FullscreenViewpagerActivity extends AppCompatActivity {

    private static ViewPager mPager;
    private static int currentPage;
    private static int NUM_PAGES = 0;
    private static String[] IMAGES ;

    private int clicked_position;

    private ArrayList<String> ImagesArray = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_viewpager);

        //get images from intent
        String image1 = getIntent().getStringExtra("Image1");
        String image2 = getIntent().getStringExtra("Image2");
        String image3 = getIntent().getStringExtra("Image3");
        String image4 = getIntent().getStringExtra("Image4");
        String image5 = getIntent().getStringExtra("Image5");

        currentPage = getIntent().getIntExtra("clicked_position", 0);

        IMAGES = new String[]{image1, image2, image3, image4, image5};

        init();

    }

    private void init(){



        for(int i=0;i<IMAGES.length;i++)
            ImagesArray.add(IMAGES[i]);

        mPager =  findViewById(R.id.fullscreen_view_pager);


        mPager.setAdapter(new FullscreenViewpagerAdapter(FullscreenViewpagerActivity.this,ImagesArray));

        final float density = getResources().getDisplayMetrics().density;


        NUM_PAGES =IMAGES.length;

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 10000, 10000);



    }
}