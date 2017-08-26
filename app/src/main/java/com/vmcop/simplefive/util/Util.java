package com.vmcop.simplefive.util;

import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;

public class Util {
	public final static String GIRL_FRAGMENT = "girl_fragment";
	public final static String ACTION_FRAGMENT = "action_fragment";

	public static final String CONS_JSON_PART = "+";
	public static final String CONS_SHOW_PART = "- ";

	public static final String CONS_FONT_GRID_MONAN = "fonts/DancingScript-Bold.ttf";

	public static final String STR_APPLICATION_ID = "ca-app-pub-8354689046611467~8985809679";
	public static final String STR_REWARD_VIDEO_CODE = "ca-app-pub-8354689046611467/4063110891";
	public static final String STR_INTERSTITIAL_ID = "ca-app-pub-8354689046611467/2212695376";
	public static final String DEVICE_ID = "91BAF0D14311747AD628F5A5F9629E31";
	public static final int MINUTE_SHOW_AD = 1;//Min number of minutes

	public static String loadJSONFromAsset( AssetManager inAssetManager, String jsonFileName) {
        String json = null;
        try {
            InputStream is = inAssetManager.open(jsonFileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
       } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }


	public static String getImageUrl(String imageFolder, String imageName) {
		return "file:///android_asset/images/" + imageFolder + "/" + imageName + ".jpg";
	}
	
	public static String getJSONContent(String contentData){
		String resultStr = "";
		if(isBlankString(contentData)){
			return resultStr;
		}
		
		String trimItem = "";
		String[] slipData =  contentData.split("\\"+CONS_JSON_PART);
		for(String item : slipData){
			trimItem = item.trim();
			if("".endsWith(trimItem)){
				continue;
			}
			if("".endsWith(resultStr)){
				resultStr += CONS_SHOW_PART + item.trim();
			} else {
				resultStr += "\n" + CONS_SHOW_PART + item.trim();
			}
		}
		return resultStr;
	}

	public static String getTenMon(String inTitle){
		String[] strArr = inTitle.split(" ");
		StringBuffer res = new StringBuffer();
		for (String str : strArr) {
			char[] stringArray = str.trim().toCharArray();
			stringArray[0] = Character.toUpperCase(stringArray[0]);
			str = new String(stringArray);
			res.append(str).append(" ");
		}
		return res.toString().trim();
	}

	public static Boolean isBlankString(String inString){
		if(inString != null && !"".endsWith(inString.trim())){
			return false;
		}
		return true;
	}
	
}
