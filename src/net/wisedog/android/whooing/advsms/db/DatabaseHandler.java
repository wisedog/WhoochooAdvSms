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
package net.wisedog.android.whooing.advsms.db;

import java.util.ArrayList;

import net.wisedog.android.whooing.advsms.fragments.MessageEntity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {
	
	// Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "contactsManager";
 
    // Contacts table name
    private static final String TABLE_SENT_SMS = "sent_sms";
 
    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_TIMESTAMP = "timestamp";

	public DatabaseHandler(Context context, String name, CursorFactory factory,
			int version) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_SENT_SMS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_ADDRESS + " TEXT,"
                + KEY_TIMESTAMP + " INTEGER" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}
	
	public void dropTable(){
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SENT_SMS);
		db.close();
	}
	
	public boolean isSent(long id, String address, long fromTimeStamp, long toTimeStamp){
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("", null);
		if (cursor != null)
	        return true;
		return false;
	}
	
	public boolean addSentSms(ArrayList<MessageEntity> array){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		for(MessageEntity entity: array){
			values.put(KEY_ID, entity.getId());
			values.put(KEY_ADDRESS, entity.getAddress());
			values.put(KEY_TIMESTAMP, entity.getTimestampInt());
		 
		    // Inserting Row
		    db.insert(TABLE_SENT_SMS, null, values);
		}
	    
	    db.close(); // Closing database connection
		return false;
	}
}
