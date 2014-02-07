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
package net.wisedog.android.whooing.advsms.auth;

import net.wisedog.android.whooing.advsms.AppDefine;
import net.wisedog.android.whooing.advsms.R;
import net.wisedog.android.whooing.network.ThreadHandshake;
import net.wisedog.android.whooing.network.ThreadRestAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Authorization activity
 * */
public class WhooingAuthMain extends Activity {

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.whooing_auth_main);

    	if(AppDefine.PIN == null || AppDefine.REAL_TOKEN == null){
    		ThreadHandshake thread = new ThreadHandshake(mHandler, this, false);
    		thread.start();
    	}
	}
    
    protected void updateProgress(int index){
        TextView indicator0 = (TextView)findViewById(R.id.auth_doing_first_step);
        TextView indicator1 = (TextView)findViewById(R.id.auth_doing_second_step);
        TextView indicator2 = (TextView)findViewById(R.id.auth_doing_third_step);
        
        TextView text0 = (TextView)findViewById(R.id.auth_text_first_step);
        TextView text1 = (TextView)findViewById(R.id.auth_text_second_step);
        TextView text2 = (TextView)findViewById(R.id.auth_text_third_step);
        
        ProgressBar progress0 = (ProgressBar)findViewById(R.id.auth_progress_first);
        ProgressBar progress1 = (ProgressBar)findViewById(R.id.auth_progress_second);
        ProgressBar progress2 = (ProgressBar)findViewById(R.id.auth_progress_third);
        
        if(index == 1){
            indicator0.setVisibility(View.GONE);
            indicator1.setVisibility(View.VISIBLE);
            progress0.setVisibility(View.INVISIBLE);
            progress1.setVisibility(View.VISIBLE);
            text0.setTextColor(Color.GRAY);
            text0.setTypeface(null, Typeface.NORMAL);
            text1.setTextColor(Color.BLACK);
            text1.setTypeface(null, Typeface.BOLD);
            
        }else if(index == 2){
            indicator1.setVisibility(View.GONE);
            indicator2.setVisibility(View.VISIBLE);
            progress1.setVisibility(View.INVISIBLE);
            progress2.setVisibility(View.VISIBLE);
            text1.setTextColor(Color.GRAY);
            text1.setTypeface(null, Typeface.NORMAL);
            text2.setTextColor(Color.BLACK);
            text2.setTypeface(null, Typeface.BOLD);
        }
    }
    
    @SuppressLint("HandlerLeak")
	Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if(msg.what == AppDefine.MSG_FAIL){
				//dialog.dismiss();
				Toast.makeText(WhooingAuthMain.this, getString(R.string.msg_auth_fail), Toast.LENGTH_LONG).show();
			}
			else if(msg.what == AppDefine.MSG_REQ_AUTH){
				WhooingAuthMain.this.runOnUiThread(new Runnable(){
					@Override
					public void run() {
						updateProgress(1);
					}
				});
			    
			    final String token = (String)msg.obj;
			    postDelayed(new Runnable(){

                    @Override
                    public void run() {
                        Intent intent = new Intent(WhooingAuthMain.this, WhooingAuthWeb.class);
                        intent.putExtra("first_token", token);
                        startActivityForResult(intent, AppDefine.REQUEST_AUTH);
                        
                    }}, 500);
				
			}
			else if(msg.what == AppDefine.MSG_AUTH_DONE){
			    updateProgress(2);
				ThreadRestAPI thread = new ThreadRestAPI(mHandler, WhooingAuthMain.this);
				thread.start();
			}
			else if(msg.what == AppDefine.MSG_API_OK){
				JSONObject result = (JSONObject)msg.obj;					
				try {
					JSONArray array = result.getJSONArray("results");
					JSONObject obj = (JSONObject) array.get(0);
					String section = obj.getString("section_id");
					if(section != null){
						AppDefine.APP_SECTION = section;
						Log.d("wisedog", "APP SECTION:"+ AppDefine.APP_SECTION);
						SharedPreferences prefs = WhooingAuthMain.this.getSharedPreferences(AppDefine.SHARED_PREFERENCE,
								Activity.MODE_PRIVATE);
						SharedPreferences.Editor editor = prefs.edit();
						editor.putString(AppDefine.KEY_SHARED_SECTION_ID, section);
						editor.commit();
						/*Intent intent = new Intent(WhooingAuthMain.this, SettingActivity.class);
						startActivityForResult(intent, AppDefine.MSG_SETTING_DONE);*/
					}
					else{
						throw new JSONException("Error in getting section id");
					}
				} catch (JSONException e) {
				    setErrorHandler("통신 오류! Err-SCT1");
					e.printStackTrace();
				}
			}
		}
	};

	@Override
	public void onBackPressed() {
    	AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle(getString(R.string.exit));
		alert.setMessage(getString(R.string.is_exit));
		alert.setCancelable(true);
		alert.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.dismiss();
				setResult(RESULT_CANCELED);
				finish();
			}
		});
		alert.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		}).show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_OK){
			if(requestCode == AppDefine.REQUEST_AUTH){
			    if(data == null){
			        setErrorHandler("인증오류! Err No.1");
			        return;
			    }
			    String secondtoken = data.getStringExtra("token");
			    String pin = data.getStringExtra("pin");
			    if(secondtoken == null || pin == null){
			        setErrorHandler("인증오류! Err No.2");
			        return;
			    }
			    AppDefine.PIN = pin;
				SharedPreferences prefs = getSharedPreferences(AppDefine.SHARED_PREFERENCE,
						MODE_PRIVATE);
				SharedPreferences.Editor editor = prefs.edit();
				editor.putString(AppDefine.KEY_SHARED_PIN, pin);
				editor.commit();
				
				ThreadHandshake thread = new ThreadHandshake(mHandler, this, true, secondtoken);
	    		thread.start();
			}
			else if(requestCode == AppDefine.MSG_SETTING_DONE){
			    setResult(RESULT_OK);
			    finish();
			}
		}
		else if(resultCode == RESULT_CANCELED){
			if(requestCode == AppDefine.REQUEST_AUTH){
				Toast.makeText(this, getString(R.string.msg_auth_fail), Toast.LENGTH_LONG).show();
			}
			else if(requestCode == AppDefine.MSG_SETTING_DONE){
			    setResult(RESULT_CANCELED);
			    finish();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	public void setErrorHandler(String errorMsg){
	    Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
	}
}
