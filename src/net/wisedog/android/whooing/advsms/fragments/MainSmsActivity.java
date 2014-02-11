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
package net.wisedog.android.whooing.advsms.fragments;

import net.wisedog.android.whooing.advsms.R;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;

public class MainSmsActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		MainFragment f = new MainFragment();
		getFragmentManager().beginTransaction().addToBackStack(null)
		.add(R.id.content_frame, f).commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		android.view.MenuInflater inflater = getMenuInflater();		
		inflater.inflate(R.menu.menus, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, android.view.MenuItem item) {
		switch(item.getItemId())
		{
		case R.id.menu_action_setting:
			SettingFragment f = new SettingFragment();
			getFragmentManager().beginTransaction().addToBackStack(null)
			.replace(R.id.content_frame, f).commit();
			break;
		case R.id.menu_action_rating:
			final String appName = "net.wisedog.android.whooing.advsms";
        	try {
        	    startActivity(
        	    		new Intent(Intent.ACTION_VIEW, 
        	    				Uri.parse("market://details?id="+appName)));
        	} catch (android.content.ActivityNotFoundException anfe) {
        	    startActivity(
        	    		new Intent(Intent.ACTION_VIEW, 
        	    				Uri.parse("http://play.google.com/store/apps/details?id="+appName)));
        	}
			break;
		case R.id.menu_action_about:
			AboutDialog newFragment = AboutDialog.newInstance();
            newFragment.show(getFragmentManager(), "dialog");
			break;
		default:
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	public void onBackPressed() {
		int count = getFragmentManager().getBackStackEntryCount();
		if(count == 1){
			finish();
			return;
		}
		super.onBackPressed();
	}
	
	
}
