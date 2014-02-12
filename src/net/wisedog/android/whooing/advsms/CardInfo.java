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

import java.util.ArrayList;
import java.util.StringTokenizer;

public class CardInfo {
	// Do not change order!
	public static final String[] cardAddressList = {
		"15447000", "신한카드",
		"15888700", "삼성카드",
		"15661000", "씨티카드",
		"15991155", "하나SK카드",
		"15883200", "외환카드",
		"15884000", "우리카드",
		"15776000", "현대카드",
		"15881600", "NH농협카드",
		"15881788", "국민카드",
		"15888100", "롯데카드",
		"15884477", "전북은행",
		"15881900", "우체국",
		"15662566", "기업은행", 
		"15665050", "대구은행",
		"15666000", "신협",
		"15881500", "산업은행",  
		"15881515", "수협",
		"15881599", "SC",
		"15999000", "새마을금고",
		"16008585", "경남은행", 
		"15443311", "HSBC"};
	
	public static String convertIntArrayToString(ArrayList<Integer> array){
		String str="";
		if(array != null){
			int length = array.size();
			
			for(int i = 0; i < length; i++){
				str = str + array.get(i) + ",";
			}
			str = str.substring(0, str.length()-1);	//remove last ,
		}
		
		return str;
	}
	
	public static ArrayList<Integer> convertStringToIntArray(String cards){
		ArrayList<Integer> array = null;
		if(cards != null){
			array = new ArrayList<Integer>();
			StringTokenizer st = new StringTokenizer(cards, ",");
			while (st.hasMoreElements()) {
				array.add(Integer.valueOf(st.nextToken()));
			}
		}		
		return array;
	}
}
