package com.vmcop.simplefive.util;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.View;
import android.widget.Button;

import com.vmcop.simplefive.vr360channel.R;

import static com.vmcop.simplefive.vr360channel.MainActivity.editor;
import static com.vmcop.simplefive.vr360channel.MainActivity.prefs;

public class AppRater {
    private final static String APP_TITLE = "VR 360 Channel";// App Name
    private final static String APP_PNAME = "com.vmcop.simplefive.vr360channel";// Package Name

    private final static double DAYS_UNTIL_PROMPT = 1;//Min number of days
    private final static int LAUNCHES_UNTIL_PROMPT = 0;//Min number of launches

    public static void app_launched(Context mContext) {

        if (prefs.getBoolean("dontshowagain", false)) {
            return;
        }

        long launch_count = prefs.getLong("launch_count", 0) + 1;
        editor.putLong("launch_count", launch_count);

        Long date_firstLaunch = prefs.getLong("date_firstlaunch", 0);

        date_firstLaunch = System.currentTimeMillis();
        editor.putLong("date_firstlaunch", date_firstLaunch);

        // Wait at least n days before opening
        if (launch_count >= LAUNCHES_UNTIL_PROMPT) {
            if (System.currentTimeMillis() >= date_firstLaunch +
                    (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
                showRateDialog(mContext, editor);
            }
        }

        editor.commit();
    }

    public static void showRateDialog(final Context mContext, final SharedPreferences.Editor editor) {

        final Dialog dialog = new Dialog(mContext);
        dialog.show();
        dialog.setTitle("Rate " + APP_TITLE);
        dialog.setCancelable(false);
//        TextView titleView = (TextView)dialog.findViewById(android.R.id.title);
//        titleView.setGravity(Gravity.CENTER);

        dialog.setContentView(R.layout.app_rate);


        Button rateBtnId = (Button)dialog.findViewById(R.id.rateBtnId);
        rateBtnId.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + APP_PNAME)));
                if (editor != null) {
                    editor.putBoolean("dontshowagain", true);
                    editor.commit();
                }
                dialog.dismiss();
            }
        });

        Button rateAfterId = (Button)dialog.findViewById(R.id.rateAfterId);
        rateAfterId.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Button noRateId = (Button)dialog.findViewById(R.id.noRateId);
        noRateId.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (editor != null) {
                    editor.putBoolean("dontshowagain", true);
                    editor.commit();
                }
                dialog.dismiss();
            }
        });

    }
}
