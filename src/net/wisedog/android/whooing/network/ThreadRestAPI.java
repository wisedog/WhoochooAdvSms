/*
 * Copyright (C) 2013 Jongha Kim
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.wisedog.android.whooing.network;

import org.json.JSONObject;

import net.wisedog.android.whooing.advsms.AppDefine;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class ThreadRestAPI extends Thread {
	private Handler mHandler;
	private Context mContext;
	private int mApiKind = 0;
	private Bundle mBundle;
	
	public ThreadRestAPI(Handler mHandler, Context context, int apiKind) {
        super();
        this.mHandler = mHandler;
        this.mContext = context;
        this.mApiKind = apiKind;
        checkLoginInfo();
    }
	
	public ThreadRestAPI(Handler mHandler, Context context, int apiKind, Bundle bundle) {
        super();
        this.mHandler = mHandler;
        this.mContext = context;
        this.mApiKind = apiKind;
        this.mBundle = bundle;
        checkLoginInfo();
    }
	
	private void checkLoginInfo() {	    
        SharedPreferences prefs = mContext.getSharedPreferences(AppDefine.SHARED_PREFERENCE, 0);    //0 is MODE_PRIVATE
        AppDefine.REAL_TOKEN = prefs.getString(AppDefine.KEY_SHARED_TOKEN, null);
        AppDefine.PIN = prefs.getString(AppDefine.KEY_SHARED_PIN, null);
        AppDefine.TOKEN_SECRET = prefs.getString(AppDefine.KEY_SHARED_TOKEN_SECRET, null);
        AppDefine.APP_SECTION = prefs.getString(AppDefine.KEY_SHARED_SECTION_ID, null);
        AppDefine.USER_ID = prefs.getInt(AppDefine.KEY_SHARED_USER_ID, 0);
        
		if (AppDefine.IS_DEBUG) {
			Log.i("wisedog", "user_id: " + AppDefine.USER_ID + " app_section : "
					+ AppDefine.APP_SECTION + " real_token:" + AppDefine.REAL_TOKEN
					+ " pin : " + AppDefine.PIN + " token_secret : "
					+ AppDefine.TOKEN_SECRET);
		}		
	}
	
	@Override
	public void run() {
		JSONObject result = null;
		if(mApiKind == AppDefine.API_GET_SECTIONS){
			Section section = new Section();
			result = section.getSections(AppDefine.APP_ID, AppDefine.REAL_TOKEN, 
					AppDefine.APP_SECRET, AppDefine.TOKEN_SECRET);
		}
		else if(mApiKind == AppDefine.API_POST_SMS){
			Outside outside = new Outside();
			try{
				mBundle.putString("section_id", AppDefine.APP_SECTION);
				result = outside.postOutside(AppDefine.APP_ID, AppDefine.REAL_TOKEN, 
						AppDefine.APP_SECRET, AppDefine.TOKEN_SECRET, mBundle);
			}catch(NullPointerException e){
				e.printStackTrace();
			}			
		}
		else if(mApiKind == AppDefine.API_POST_SMS_REPORT){
			Outside outside = new Outside();
			try{
				mBundle.putString("section_id", AppDefine.APP_SECTION);
				result = outside.postReport(AppDefine.APP_ID, AppDefine.REAL_TOKEN, 
						AppDefine.APP_SECRET, AppDefine.TOKEN_SECRET, mBundle);
			}catch(NullPointerException e){
				e.printStackTrace();
			}			
		}
		
		sendMessage(result);
		super.run();
	}
	
	private void sendMessage(JSONObject result){
		Message msg = new Message();
		if(result != null){
			msg.what = AppDefine.MSG_API_OK;
		}
		else{
			msg.what = AppDefine.MSG_API_FAIL;
		}
		msg.obj = result;
		msg.arg1 = this.mApiKind;
		mHandler.sendMessage(msg);
	}
}
