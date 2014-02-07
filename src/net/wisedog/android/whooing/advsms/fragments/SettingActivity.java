package net.wisedog.android.whooing.advsms.fragments;

import net.wisedog.android.whooing.advsms.R;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class SettingActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		
		TextView cardSetting = (TextView)findViewById(R.id.setting_text_card);
		TextView alarmSetting = (TextView)findViewById(R.id.setting_text_alarm_time);
		
		if(cardSetting != null && alarmSetting != null){
			cardSetting.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
				}
			});
			
			alarmSetting.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
				}
			});
		}
	}

	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id, Bundle args) {
		// TODO Auto-generated method stub
		return super.onCreateDialog(id, args);
	}
	
	
	
	
}
