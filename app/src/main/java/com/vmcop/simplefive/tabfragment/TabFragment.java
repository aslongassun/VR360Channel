package com.vmcop.simplefive.tabfragment;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.vmcop.simplefive.model.BeanPost;
import com.vmcop.simplefive.util.ImageAdapter;
import com.vmcop.simplefive.util.Util;
import com.vmcop.simplefive.vr360channel.MainActivity;
import com.vmcop.simplefive.vr360channel.R;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static com.vmcop.simplefive.vr360channel.MainActivity.prefs;


public class TabFragment extends Fragment{
    private String JSONFile;
    private ArrayList<BeanPost> beanPostArrayList;
    private GridView gridView;
    private String tabName;

    public Boolean getHaveInterstitialAd() {return isHaveInterstitialAd;}
    public void setHaveInterstitialAd(Boolean haveInterstitialAd) {isHaveInterstitialAd = haveInterstitialAd;}
    private Boolean isHaveInterstitialAd;
    public String getTabName() {return tabName;}
    public void setTabName(String tabName) {this.tabName = tabName;}
    public void setJSONFile(String JSONFile) { this.JSONFile = JSONFile;}

    public TabFragment() {
        isHaveInterstitialAd = true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... voids) {
                Type listType = new TypeToken<ArrayList<BeanPost>>() {}.getType();
                beanPostArrayList = new GsonBuilder().create().fromJson(Util.loadJSONFromAsset(getContext().getResources().getAssets(), JSONFile + ".json"), listType);
                for(int i = 0; i < beanPostArrayList.size(); i ++){
                    Boolean tempData = prefs.getBoolean(JSONFile + "is_default_show" + i, beanPostArrayList.get(i).getIs_default_show());
                    MainActivity.editor.putBoolean(JSONFile + "is_default_show" + i, tempData);
                    beanPostArrayList.get(i).setIs_default_show(tempData);
                }
                MainActivity.editor.commit();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (beanPostArrayList != null) {

                    gridView.setAdapter(new ImageAdapter(getContext(), beanPostArrayList, JSONFile));
                    gridView.setDrawingCacheEnabled(false);
                    gridView.setScrollingCacheEnabled( false );
                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View v,
                                                int position, long id) {

                            MainActivity.setCurrentBeanPostArrayList(beanPostArrayList);
                            MainActivity.setCurrentGridView(gridView);
                            MainActivity.setCurrentJSONFile(JSONFile);
                            MainActivity.setCurrentPosition(position);
                            MainActivity.setCurrentView(v);

                            if(beanPostArrayList.get(position).getIs_default_show()) {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(beanPostArrayList.get(position).getNguon_tk()));
                                startActivity(browserIntent);
                            } else {
                                if(MainActivity.adRewardedVideo.isLoaded()){
                                    MainActivity.adRewardedVideo.show();
                                }
                            }


                           // currentGridView.invalidateViews();

                        }
                    });

                    gridView.setOnScrollListener(new AbsListView.OnScrollListener(){
                        @Override
                        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
                        {
                            /*
                            Log.d("RunDebug","aaaa");
                            if(isHaveInterstitialAd && (firstVisibleItem + visibleItemCount >= totalItemCount)){
                                long time_show_ad = MainActivity.prefs.getLong("time_show_ad", 0);
                                Long calTime = time_show_ad + (Util.MINUTE_SHOW_AD * 60 * 1000);
                                if(System.currentTimeMillis() >= calTime && MainActivity.mInterstitialAd.isLoaded()){
                                    time_show_ad = System.currentTimeMillis();
                                    MainActivity.editor.putLong("time_show_ad", time_show_ad);
                                    MainActivity.editor.commit();
                                    MainActivity.mInterstitialAd.show();
                                }
                            }
                            */
                        }
                        @Override
                        public void onScrollStateChanged(AbsListView view, int scrollState){}
                    });
                }
            }
        }.execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View resultInflater = inflater.inflate(R.layout.tab_fragment, container, false);
        gridView = (GridView) resultInflater.findViewById(R.id.gridview);
        return resultInflater;
    }

}
