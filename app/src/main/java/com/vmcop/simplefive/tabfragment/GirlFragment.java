package com.vmcop.simplefive.tabfragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.vmcop.simplefive.model.BeanPost;
import com.vmcop.simplefive.util.ImageAdapter;
import com.vmcop.simplefive.util.Util;
import com.vmcop.simplefive.vr360channel.R;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static java.security.AccessController.getContext;


public class GirlFragment extends Fragment{
    // Google ca-app-pub-3940256099942544/5224354917
    private static final String STR_REWARD_VIDEO_CODE = "ca-app-pub-8354689046611467/4063110891";
    private ArrayList<BeanPost> beanPostArrayList;
    private int currPosition;
    private View currView;
    private GridView gridView;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private RewardedVideoAd mAd;

    public GirlFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = this.getActivity().getSharedPreferences("apprater", 0);
        editor = prefs.edit();
        MobileAds.initialize(this.getActivity(), "ca-app-pub-8354689046611467~8985809679");
        mAd = MobileAds.getRewardedVideoAdInstance(this.getActivity());
        loadRewardedVideoAd();
        mAd.setRewardedVideoAdListener(new RewardedVideoAdListener(){
            // Required to reward the user.
            @Override
            public void onRewarded(RewardItem reward) {
                beanPostArrayList.get(currPosition).setIs_default_show(true);
                editor.putBoolean(Util.GIRL_FRAGMENT + "is_default_show" + currPosition, true);
                editor.commit();
                ImageView tempImageView = (ImageView) currView.findViewById(R.id.picture);
                Glide.with(getContext())
                        .load(beanPostArrayList.get(currPosition).getImage_name())
                        .signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                        .into(tempImageView);

                TextView tempTextView = (TextView) currView.findViewById(R.id.text);
                tempTextView.setText(beanPostArrayList.get(currPosition).getTitle());
            }

            @Override
            public void onRewardedVideoAdLeftApplication() {
                //Toast.makeText(getActivity(), "onRewardedVideoAdLeftApplication",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onRewardedVideoAdClosed() {
                //Toast.makeText(getActivity(), "onRewardedVideoAdClosed", Toast.LENGTH_SHORT).show();
                loadRewardedVideoAd();
            }
            @Override
            public void onRewardedVideoAdFailedToLoad(int errorCode) {
                //Toast.makeText(getActivity(), "onRewardedVideoAdFailedToLoad", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onRewardedVideoAdLoaded() {
                //Toast.makeText(getActivity(), "onRewardedVideoAdLoaded", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onRewardedVideoAdOpened() {
                //Toast.makeText(getActivity(), "onRewardedVideoAdOpened", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onRewardedVideoStarted() {
                //Toast.makeText(getActivity(), "onRewardedVideoStarted", Toast.LENGTH_SHORT).show();
            }
        });

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... voids) {
                Type listType = new TypeToken<ArrayList<BeanPost>>() {}.getType();
                beanPostArrayList = new GsonBuilder().create().fromJson(Util.loadJSONFromAsset(getContext().getResources().getAssets(), Util.GIRL_FRAGMENT + ".json"), listType);

                SharedPreferences.Editor editor = prefs.edit();
                for(int i = 0; i < beanPostArrayList.size(); i ++){
                    Boolean tempData = prefs.getBoolean(Util.GIRL_FRAGMENT + "is_default_show" + i, beanPostArrayList.get(i).getIs_default_show());
                    editor.putBoolean(Util.GIRL_FRAGMENT + "is_default_show" + i, tempData);
                    beanPostArrayList.get(i).setIs_default_show(tempData);
                }
                editor.commit();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (beanPostArrayList != null) {

                    gridView.setAdapter(new ImageAdapter(getContext(), beanPostArrayList, Util.GIRL_FRAGMENT));

                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View v,
                                                int position, long id) {
                            currView = v;
                            currPosition = position;
                            if(beanPostArrayList.get(position).getIs_default_show()) {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(beanPostArrayList.get(position).getNguon_tk()));
                                startActivity(browserIntent);
                            } else {
                                if(mAd.isLoaded()){
                                    mAd.show();
                                }
                            }
                        }
                    });
                }
            }
        }.execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View resultInflater = inflater.inflate(R.layout.girl_fragment, container, false);
        gridView = (GridView) resultInflater.findViewById(R.id.gridview);
        return resultInflater;
    }

    private void loadRewardedVideoAd() {
        mAd.loadAd(STR_REWARD_VIDEO_CODE, new AdRequest.Builder().addTestDevice("91BAF0D14311747AD628F5A5F9629E31").build());
    }
}

//                Fragment frg = getFragmentManager().getFragments().get(1);
//                FragmentTransaction ft = getFragmentManager().beginTransaction();
//                ft.detach(frg);
//                ft.attach(frg);
//                ft.commit();
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//
//                    }
//                });
//getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.gridview,new GirlFragment()).commitAllowingStateLoss();
//Toast.makeText(getActivity(), "onRewarded! currency: " + reward.getType() + "  amount: " + reward.getAmount(), Toast.LENGTH_SHORT).show();
// can not perform this action after onsaveinstancestate fragment
// Reload current fragment
//                editor.putBoolean(Util.GIRL_FRAGMENT + "is_default_show" + currPosition, true);
//                editor.commit();
//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.gridview,new GirlFragment()).commitAllowingStateLoss();
//List<Fragment> allFragments = getFragmentManager().getFragments();
//Fragment  frg = getFragmentManager().findFragmentByTag("GirlFragment");
//                if(frg != null){
//                    if(tempInstance != null) {
//                        frg.onSaveInstanceState(tempInstance);
//                    } else {
//
//                    }
//                } else {
//                    Log.d("FRAGEMENT_TAG","==NULL==");
//                }
//ft.commitNow();
//ft.commit();
//ft.commitAllowingStateLoss();