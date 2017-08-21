package com.vmcop.simplefive.tabfragment;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.vmcop.simplefive.model.BeanPost;
import com.vmcop.simplefive.util.ImageAdapter;
import com.vmcop.simplefive.util.Util;
import com.vmcop.simplefive.vr360channel.R;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ActionFragment extends Fragment{
    private ArrayList<BeanPost> beanPostArrayList;
    private GridView gridView;

    public ActionFragment() {
        // Required empty public constructor
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
                beanPostArrayList = new GsonBuilder().create().fromJson(Util.loadJSONFromAsset(getContext().getResources().getAssets(), Util.ACTION_FRAGMENT + ".json"), listType);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (beanPostArrayList != null) {

                    gridView.setAdapter(new ImageAdapter(getContext(), beanPostArrayList, Util.ACTION_FRAGMENT));

                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View v,
                                                int position, long id) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(beanPostArrayList.get(position).getNguon_tk()));
                            startActivity(browserIntent);
                        }
                    });

                }
            }
        }.execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View resultInflater = inflater.inflate(R.layout.action_fragment, container, false);
        gridView = (GridView) resultInflater.findViewById(R.id.gridview);
        return resultInflater;
    }

}
