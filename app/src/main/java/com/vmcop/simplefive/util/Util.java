package com.vmcop.simplefive.util;

import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;

public class Util {
	public static final int NUMBER_OF_DEFAULT_SHOW = 6;
	public final static String GIRL_FRAGMENT = "girl_fragment";
	public final static String ACTION_FRAGMENT = "action_fragment";

	public static final String CONS_JSON_PART = "+";
	public static final String CONS_SHOW_PART = "- ";

	public static final String CONS_FONT_GRID_MONAN = "fonts/DancingScript-Bold.ttf";
	public static final String CONS_FONT_TITLE_NAME_MONAN = "fonts/DancingScript-Bold.ttf";
	public static final String CONS_FONT_TTILE_CHILD_MONAN = "fonts/Nunito-Bold.ttf";
	public static final String CONS_FONT_DETAIL_MONAN = "fonts/YanoneKaffeesatz-Regular.ttf";

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

	/*
	public static int getImageId(Context context, String imageName) {
	    return context.getResources().getIdentifier("drawable/" + imageName, null, context.getPackageName());
		//return context.getResources().getIdentifier("assets/images/mon_an/" + imageName, null, context.getPackageName());
	}
	*/
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
