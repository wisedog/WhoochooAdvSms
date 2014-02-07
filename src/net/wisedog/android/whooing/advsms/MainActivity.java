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
package net.wisedog.android.whooing.advsms;

import net.wisedog.android.whooing.advsms.auth.WhooingAuthMain;
import net.wisedog.android.whooing.advsms.fragments.MainSmsActivity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_main);	//TODO Guide page
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onResume() {	
		SharedPreferences prefs = getSharedPreferences(AppDefine.SHARED_PREFERENCE, 0);
        AppDefine.REAL_TOKEN = prefs.getString(AppDefine.KEY_SHARED_TOKEN, null);
        AppDefine.PIN = prefs.getString(AppDefine.KEY_SHARED_PIN, null);
        
        if(AppDefine.REAL_TOKEN == null || AppDefine.PIN == null){	//need to auth
        	Intent intent = new Intent(this, WhooingAuthMain.class);
			startActivityForResult(intent, AppDefine.MSG_SETTING_DONE);
        }
        else{
        	Intent intent = new Intent(this, MainSmsActivity.class);
			startActivityForResult(intent, AppDefine.REQUEST_NORMAL);
        }
		super.onResume();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == AppDefine.REQUEST_NORMAL){
			finish();
		}
		else if(requestCode == AppDefine.MSG_SETTING_DONE){
			if(resultCode == RESULT_OK){
				Intent intent = new Intent(this, MainSmsActivity.class);
				startActivityForResult(intent, AppDefine.REQUEST_NORMAL);
			}
			else{
				//TODO 
			}
		}
		finish();
	}
}
