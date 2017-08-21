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

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.vmcop.simplefive.tabfragment.ActionFragment;
import com.vmcop.simplefive.tabfragment.GirlFragment;
import com.vmcop.simplefive.util.AppRater;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    // private Handler handler = new Handler();
    // ADMOB
    private static final String  INTERSTITIALAD_ID = "ca-app-pub-8354689046611467/2212695376";
    private static final int MINUTE_SHOW_AD = 2;//Min number of minutes
    private long time_show_ad;
    private SharedPreferences prefs;
    InterstitialAd mInterstitialAd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tab);

        prefs = MainActivity.this.getSharedPreferences("apprater", 0);
        time_show_ad = prefs.getLong("time_show_ad", 0);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(INTERSTITIALAD_ID);
        requestNewInterstitial();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Long calTime = time_show_ad + (MINUTE_SHOW_AD * 60 * 1000);
                if(mInterstitialAd.isLoaded()){
                    if(System.currentTimeMillis() >= calTime){
                        mInterstitialAd.show();
                        time_show_ad = System.currentTimeMillis();
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putLong("time_show_ad", time_show_ad);
                        editor.commit();
                    }
                } else {
                    requestNewInterstitial();
                }

//                if(mAd.isLoaded()){
//                    mAd.show();
//                }
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
                showDialog(MainActivity.this, "", "Do you want to quit app ?");
                AppRater.app_launched(MainActivity.this);
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
        adapter.addFragment(new ActionFragment(), "Action");
        adapter.addFragment(new GirlFragment(), "Teen Girl");
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

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("91BAF0D14311747AD628F5A5F9629E31")
                .build();
        mInterstitialAd.loadAd(adRequest);
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
        mInterstitialAd.setAdUnitId(INTERSTITIALAD_ID);
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