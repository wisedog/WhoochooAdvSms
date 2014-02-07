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

public class CardInfo {
	// This array is phone number of banks, securities who sms send to you
	// Well.. I know the credit card transaction notification service by SMS only in South Korea, 
	// but I will write in English for future extension. Written in Korean is allowed.
	static final String[] cardAddressList = {
		"15661000", "씨티카드", //Citi card
		"15888700", "삼성카드",
		"15991155", "하나SK카드",
		"15447000", "신한카드", //Shinhan Card		
		"15776000", "현대카드", //Hyundai Card / 현대카드
		"15881600", "NH농협카드",//NH농협 카드
		"15881788", "국민카드",//국민카드
		"15883200", "외환카드", //외환카드
		"15884000", "우리카드", 
		"15888100", "롯데카드",		
		"15884477", "전북은행",
		"15881900", "우체국", //우체국
		"15662566", "기업은행", //Kiup bank 
		"15665050", "대구은행",//Daegu bank / 대구은행
		"15666000", "신협",//신협		
		"15778000", "신한은행",//Shinhan Bank / 신한은행
		"15881500", "산업은행", //KDB 산업은행  
		"15881515", "수협", //수협은행
		"15881599", "SC", //SC
		"15889999", "국민은행",
		"15991111", "하나은행",
		"15995000", "우리은행",
		"15998000", "신한은행",
		"15999000", "새마을금고",
		"16008585", "경남은행", 
		"15443311", "HSBC"};
}
