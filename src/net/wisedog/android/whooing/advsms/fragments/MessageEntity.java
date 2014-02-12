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

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MessageEntity {
	private long msgId;
	private long threadId;
	private String address;
	private long contactId;
	private long timestamp;
	private String body;
	
	public MessageEntity(long _msgId, long _threadID, String _addr,
			long _contactId, long _timestamp, String _body) {
		msgId = _msgId;
		threadId = _threadID;
		address = _addr;
		contactId = _contactId;
		timestamp = _timestamp;
		body = _body;
	}
	

	public String getAddress(){
		return address;
	}
	
	public String getBody(){
		return body;
	}
	
	public String getTrimBody(){
		String trimBody = body.replaceAll("[\n\r]", " ");
		return trimBody;
	}
	
	
	/**
	 * @return date string with timestamp
	 * */
	public String getDateStrWithTimestamp(){
		Date date = new Date(timestamp * 10);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		Locale locale = new Locale("kr", "KR");
	    java.text.DateFormat df = java.text.DateFormat.getDateInstance(java.text.DateFormat.SHORT, locale);
	    String dateStr = df.format(calendar.getTime()).toString();
	    return dateStr;
	}
	
	public long getTimestampInt(){
		return timestamp;
	}

}
