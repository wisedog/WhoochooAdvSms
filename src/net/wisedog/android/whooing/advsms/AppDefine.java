package net.wisedog.android.whooing.advsms;

public class AppDefine {
	static final public boolean IS_DEBUG = true; 
	
	public static String APP_ID = "143";
	public static String APP_SECRET = "8ac573c24c6d59961219ab4578464fee28a41fe9";
	public static String PIN = null;
	public static String REAL_TOKEN = null;
	public static int USER_ID = -1;
	public static String TOKEN_SECRET = null;
	public static String APP_SECTION = null;
	
	public static String CURRENCY_CODE = null;
	public static String COUNTRY_CODE = null;
	public static String TIMEZONE = null;
	public static String LOCALE_LANGUAGE = null;
	
	// General Key and Shared Preference Key
	public static String SHARED_PREFERENCE = "SHARED_AUTH";
	public static String KEY_SHARED_PIN = "SHARED_PIN";
	public static String KEY_SHARED_TOKEN = "SHARED_TOKEN";
	public static String KEY_SHARED_TOKEN_SECRET = "SHARED_TOKEN_SECRET";
	public static String KEY_SHARED_USER_ID = "SHARED_USER_ID";
	public static String KEY_SHARED_SECTION_ID = "SHARED_SECTION_ID";
	public static String KEY_SHARED_USER_INFO = "SHARED_USER_INFO";
	
	public static String KEY_SHARED_CURRENCY_CODE = "SHARED_CURRENCY_CODE";
	public static String KEY_SHARED_COUNTRY_CODE = "SHARED_COUNTRY_CODE";
	public static String KEY_SHARED_TIMEZONE = "SHARED_TIMEZONE";
	public static String KEY_SHARED_LOCALE_LANGUAGE = "SHARED_LOCALE_LANG";
	
	// Response/Request Key
	public static final int RESPONSE_EXIT = 10;
	public static final int REQUEST_AUTH = 50;
	public static final int REQUEST_NORMAL = 51;
	
	//Message
	public static final int MSG_REQ_AUTH = 100;
	public static final int MSG_FAIL = 101;
	public static final int MSG_AUTH_DONE = 102;
	public static final int MSG_AUTH_TOTAL_DONE = 103;
	public static final int MSG_USER_SETTING_DONE = 104;
	public static final int MSG_SETTING_DONE = 105;
	
	public static final int MSG_API_OK = 1000;
	public static final int MSG_API_FAIL = 1003;
	public static final int MSG_API_FAIL_WITH_404 = 404;
		
	public static final int API_GET_SECTIONS = 2010;
	public static final int API_GET_MAIN = 2020;
	public static final int API_GET_BUDGET = 2030;
	public static final int API_GET_BALANCE = 2040;

}
