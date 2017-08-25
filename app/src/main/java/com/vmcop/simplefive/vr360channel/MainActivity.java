package com.vmcop.simplefive.vr360channel;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.vmcop.simplefive.model.BeanPost;
import com.vmcop.simplefive.tabfragment.TabFragment;
import com.vmcop.simplefive.util.AppRater;
import com.vmcop.simplefive.util.Util;

import java.util.ArrayList;
import java.util.List;

import static com.vmcop.simplefive.vr360channel.R.id.picture;

public class MainActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    public static InterstitialAd mInterstitialAd;
    public static RewardedVideoAd adRewardedVideo;
    public static SharedPreferences prefs;
    public static SharedPreferences.Editor editor;
    private static ArrayList<BeanPost> currentBeanPostArrayList;
    private static int currentPosition;
    private static View currentView;
    private static String currentJSONFile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tab);
        MobileAds.initialize(this, Util.STR_APPLICATION_ID);

        prefs = this.getSharedPreferences("apprater", 0);
        editor = prefs.edit();
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(Util.STR_INTERSTITIAL_ID);
        requestNewInterstitial();
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {}
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
            }
        });
        adRewardedVideo = MobileAds.getRewardedVideoAdInstance(this);
        loadRewardedVideoAd();
        adRewardedVideo.setRewardedVideoAdListener(new RewardedVideoAdListener(){
            @Override
            public void onRewarded(RewardItem reward) {
                currentBeanPostArrayList.get(currentPosition).setIs_default_show(true);
                editor.putBoolean(currentJSONFile + "is_default_show" + currentPosition, true);
                editor.commit();

                ImageView tempImageView = (ImageView) currentView.findViewById(picture);

//                new DownloadImageTask(tempImageView)
//                        .execute(currentBeanPostArrayList.get(currentPosition).getImage_name());

                Glide.with(getApplicationContext())
                        .load(currentBeanPostArrayList.get(currentPosition).getImage_name())
//                        .skipMemoryCache(true)
//                        .diskCacheStrategy(DiskCacheStrategy.NONE)
//                        .signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                        .into(tempImageView);

//                Picasso.with(getApplicationContext())
//                        .load(currentBeanPostArrayList.get(currentPosition).getImage_name())
//                        //.memoryPolicy(MemoryPolicy.NO_CACHE)
//                        .into(tempImageView);

                TextView tempTextView = (TextView) currentView.findViewById(R.id.text);
                tempTextView.setText(currentBeanPostArrayList.get(currentPosition).getTitle());
            }
            @Override
            public void onRewardedVideoAdClosed() {
                loadRewardedVideoAd();
            }
            @Override
            public void onRewardedVideoAdLeftApplication() {}
            @Override
            public void onRewardedVideoAdFailedToLoad(int errorCode) {}
            @Override
            public void onRewardedVideoAdLoaded() {}
            @Override
            public void onRewardedVideoAdOpened() {}
            @Override
            public void onRewardedVideoStarted() {}
        });

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                long time_show_ad = prefs.getLong("time_show_ad", 0);
                Long calTime = time_show_ad + (Util.MINUTE_SHOW_AD * 60 * 1000);
                if(System.currentTimeMillis() >= calTime && mInterstitialAd.isLoaded()){
                    mInterstitialAd.show();
                    time_show_ad = System.currentTimeMillis();
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putLong("time_show_ad", time_show_ad);
                    editor.commit();
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                showDialog(this, "", "Do you want to quit app ?");
                AppRater.app_launched(this);
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showDialog(Context context, String inTitle, String inMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(inTitle);
        builder.setMessage(inMessage);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {}
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        TabFragment tab1 = new TabFragment();
        tab1.setJSONFile(Util.ACTION_FRAGMENT);
        tab1.setTabName("Action");
        tab1.setHaveInterstitialAd(false);

        TabFragment tab2 = new TabFragment();
        tab2.setJSONFile(Util.GIRL_FRAGMENT);
        tab2.setTabName("Teen Girl");

        adapter.addFragment(tab1, tab1.getTabName());
        adapter.addFragment(tab2, tab2.getTabName());

        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }
        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public static void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(Util.DEVICE_ID)
                .build();
        mInterstitialAd.loadAd(adRequest);
    }
    public static void loadRewardedVideoAd() {
        adRewardedVideo.loadAd(Util.STR_REWARD_VIDEO_CODE, new AdRequest.Builder().addTestDevice(Util.DEVICE_ID).build());
    }
    public static ArrayList<BeanPost> getCurrentBeanPostArrayList() {
        return currentBeanPostArrayList;
    }

    public static void setCurrentBeanPostArrayList(ArrayList<BeanPost> currentBeanPostArrayList) {
        MainActivity.currentBeanPostArrayList = currentBeanPostArrayList;
    }

    public static int getCurrentPosition() {
        return currentPosition;
    }

    public static void setCurrentPosition(int currentPosition) {
        MainActivity.currentPosition = currentPosition;
    }

    public static View getCurrentView() {
        return currentView;
    }

    public static void setCurrentView(View currentView) {
        MainActivity.currentView = currentView;
    }

    public static String getCurrentJSONFile() {
        return currentJSONFile;
    }

    public static void setCurrentJSONFile(String currentJSONFile) {
        MainActivity.currentJSONFile = currentJSONFile;
    }
}


// TAB
//toolbar = (Toolbar) findViewById(R.id.toolbar);
//setSupportActionBar(toolbar);
//getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//getSupportActionBar().setDisplayShowTitleEnabled(false);
//gridView = (GridView) findViewById(R.id.gridview);

// QUANG CAO START
        /*
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(STR_INTERSTITIAL_ID);
        requestNewInterstitial();

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
            }
            @Override
            public void onAdClosed() {

            }
            @Override
            public void onAdOpened() {}
            @Override
            public void onAdFailedToLoad(int errorCode) {}

        });
        */
// QUANG CAO END