package com.ongroa.tilepuzzle;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HighScore extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "highscore.db";
	private static final int DATABASE_VERSION = 3;
	private static final String HIGHSCORE_TABLE_NAME = "highscore";
	private static final String HIGHSCORE_TABLE_CREATE =
			"CREATE TABLE " + HIGHSCORE_TABLE_NAME + " (" +
					"SIZE INTEGER," +
					"TIME TEXT," +
					"MOVE INTEGER);";

	HighScore(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(HIGHSCORE_TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + HIGHSCORE_TABLE_NAME);
		onCreate(db);
	}

	void addResult(int size, String time, int move) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("SIZE", size); 
		values.put("TIME", time);
		values.put("MOVE", move);
		db.insert(HIGHSCORE_TABLE_NAME, null, values);
		db.close(); 
	}

	public List<Result> getAllResults(int size) {
		List<Result> resultList = new ArrayList<Result>();
		String selectQuery = "SELECT  * FROM " + HIGHSCORE_TABLE_NAME + 
				" WHERE SIZE = " + size + " ORDER BY MOVE LIMIT 10";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				Result result = new Result();
				result.setSize(Integer.parseInt(cursor.getString(0)));
				result.setTime(cursor.getString(1));
				result.setMove(Integer.parseInt(cursor.getString(2)));
				resultList.add(result);
			} while (cursor.moveToNext());
		}
		return resultList;
	}

	public void deleteCurrentSize(int size) {
		SQLiteDatabase db = this.getWritableDatabase();
        db.delete(HIGHSCORE_TABLE_NAME, "SIZE" + " = ?",
                new String[] { String.valueOf("" + size) });
		db.close();
	}
}
