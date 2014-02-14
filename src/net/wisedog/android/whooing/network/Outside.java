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

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.os.Bundle;


/**
 * Section API routine class
 * @author	Wisedog(me@wisedog.net)
 * @see		https://whooing.com/#forum/developer/en/api_reference/sections
 * */
public class Outside extends AbstractAPI {
	/**
	 * Post SMS Messages
	 * @param	appID		Application ID
	 * @param	token		Token
	 * @param	appKey		Application Key
	 * @param	tokenSecret	Secret key for token
	 * @return	Returns JSONObject for result, or null if it fails		
	 * */
	public JSONObject postOutside(String appID, String token, String appKey, String tokenSecret, 
			Bundle bundle){
		String url = "https://whooing.com/api/outside.json_array";
		if(bundle == null){
			return null;
		}
		
		String appSection = bundle.getString("section_id", null);
		String rows = bundle.getString("rows", null);
		if(appSection == null || rows == null){
			return null;
		}
		
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("section_id", appSection));
		nameValuePairs.add(new BasicNameValuePair("rows", rows));
		return callRawApiPost(url, appID, token, appKey, tokenSecret, appSection, nameValuePairs);
	}
	
	/**
	 * Post SMS Messages
	 * @param	appID		Application ID
	 * @param	token		Token
	 * @param	appKey		Application Key
	 * @param	tokenSecret	Secret key for token
	 * @return	Returns JSONObject for result, or null if it fails		
	 * */
	public JSONObject postReport(String appID, String token, String appKey, String tokenSecret, 
			Bundle bundle){
		String url = "https://whooing.com/api/outside_report.json";
		if(bundle == null){
			return null;
		}
		
		String source = bundle.getString("rows", null);
		if(source == null){
			return null;
		}
		
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("source", source));
		return callRawApiPost(url, appID, token, appKey, tokenSecret, null, nameValuePairs);
	}
}
