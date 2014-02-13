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

import java.util.ArrayList;

import net.wisedog.android.whooing.advsms.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

/**
 * @author Wisedog(me@wisedog.net)
 *
 */
public class SmsListAdapter extends BaseAdapter {
    private ArrayList<MessageEntity> mDataArray;
    private LayoutInflater mInflater;
    private Boolean[] mSelected;
    
    public SmsListAdapter(Context context, ArrayList<MessageEntity> dataArray){
        mDataArray = dataArray;
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        setData(mDataArray);
    }
    
    public Boolean[] getSelectedArray(){
    	return mSelected;
    }
    
    public void setData(ArrayList<MessageEntity> dataArray){
        mDataArray = dataArray;
        if(mDataArray.size() > 0){
        	mSelected = new Boolean[mDataArray.size()];
            for(int i = 0; i < mDataArray.size(); i++){
            	mSelected[i] = false;
            }
        }
    }
    
    @Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
		if(mDataArray.size() > 0){
			mSelected = null;
        	mSelected = new Boolean[mDataArray.size()];
            for(int i = 0; i < mDataArray.size(); i++){
            	mSelected[i] = false;
            }
        }
	}

	public int getCount() {
        return mDataArray.size();
    }

    public Object getItem(int position) {
        return mDataArray.get(position);
    }

    public long getItemId(int position) {
        return position;
    }
    
    public void clearAdapter()
    {
    	mDataArray.clear();
        notifyDataSetChanged();
    }

    /*
     * (non-Javadoc)
     * @see android.widget.Adapter#getView(int, android.view.View,
     * android.view.ViewGroup)
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.sms_list_item, parent, false);
            convertView.setTag(position);
        }
        TextView textContent = (TextView) convertView.findViewById(R.id.sms_list_content);
        TextView textDate = (TextView) convertView.findViewById(R.id.sms_list_date);
        TextView textAddress = (TextView) convertView.findViewById(R.id.sms_list_address);
        if(textContent == null || textDate == null || textAddress == null){
        	return null;
        }
        CheckBox checkbox = (CheckBox) convertView.findViewById(R.id.smsListCheckBox);
        if(checkbox != null){
        	checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
					mSelected[pos] = isChecked;
				}
			});
        }
        
        MessageEntity item = mDataArray.get(pos);
        if(item != null){
        	textContent.setText(item.getTrimBody());
        	textDate.setText(item.getDateStrWithTimestamp());
        	textAddress.setText(item.getAddress());
        }
        return convertView;
    }

}
