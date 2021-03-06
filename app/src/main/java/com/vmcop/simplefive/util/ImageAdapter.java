package com.vmcop.simplefive.util;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vmcop.simplefive.model.BeanPost;
import com.vmcop.simplefive.vr360channel.R;

import java.util.ArrayList;
import java.util.List;

public final class ImageAdapter extends BaseAdapter {
    private final List<Item> mItems = new ArrayList<Item>();
    private final LayoutInflater mInflater;
    private final Typeface typeface_grid_monan;
    private final Context myContext;

    public ImageAdapter(Context context, ArrayList<BeanPost> inBeanPostArrayList, String imageFolder) {
        myContext = context;
        mInflater = LayoutInflater.from(context);
        typeface_grid_monan = Typeface.createFromAsset(mInflater.getContext().getAssets(), Util.CONS_FONT_GRID_MONAN);

        for(int i = 0; i < inBeanPostArrayList.size(); i ++){
            mItems.add(new Item(
                                Util.getTenMon(inBeanPostArrayList.get(i).getTitle()) ,
                                inBeanPostArrayList.get(i).getImage_name(),
                                inBeanPostArrayList.get(i).getIs_default_show())
            );
        }
    }

    public int getCount() {
        return mItems.size();
    }

    public Item getItem(int i) {
        return mItems.get(i);
    }

    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View myView = view;
        ImageView picture;
        TextView name;

        if (myView == null) {
            myView = mInflater.inflate(R.layout.image_item, viewGroup, false);
            myView.setTag(R.id.picture, myView.findViewById(R.id.picture));
            myView.setTag(R.id.text, myView.findViewById(R.id.text));
        }

        picture = (ImageView) myView.getTag(R.id.picture);
        name = (TextView) myView.getTag(R.id.text);
        name.setTypeface(typeface_grid_monan);
        Item item = getItem(i);

        name.setDrawingCacheEnabled(false);
        picture.setDrawingCacheEnabled(false);

        // 2017/12/25 Vinh Hua Quoc modified start
        name.setText(item.name);
        Glide.with(myContext)
                .load(item.imageUrl)
                .into(picture);
        /*
        if(item.is_default_show){
            name.setText(item.name);
            Glide.with(myContext)
                    .load(item.imageUrl)
                    //.signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                    .into(picture);
        } else {
            name.setText("See advertise to open");
            Glide.with(myContext)
                    .load(R.drawable.question)
                    //.signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                    .into(picture);
        }
        */
        // 2017/12/25 Vinh Hua Quoc modified end

        return myView;
    }

    private static class Item {
        public final String name;
        public final String imageUrl;
        public final Boolean is_default_show;

        Item(String name, String imageUrl, Boolean is_default_show) {
            this.name = name;
            this.imageUrl = imageUrl;
            this.is_default_show = is_default_show;
        }
    }
}