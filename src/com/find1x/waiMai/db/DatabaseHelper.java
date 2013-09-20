package com.find1x.waiMai.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final int VERSION = 1;

	public DatabaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	public DatabaseHelper(Context context, String name) {
		this(context, name, VERSION);
	}

	public DatabaseHelper(Context context, String name, int version) {
		this(context, name, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		System.out.println("create a Database");
		db.execSQL("create table user(_id int,name varchar,tel vercher)");
		String[] name = new String[] { "肯德基", "麦当劳", "必胜客" };
		String[] tel = new String[] { "4008823823", "4008517517", "4008123123" };
		ContentValues values = new ContentValues();
		for (int i = 0; i < name.length; i++) {
			values = new ContentValues();
			values.put("name", name[i]);
			values.put("tel", tel[i]);
			db.insert("user", null, values);
			System.out.println("插入名称:" + name[i]);
			System.out.println("插入电话:" + tel[i]);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		System.out.println("update a Database");
	}

}
