package br.edu.ufcg.dsc.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class RotaSQLiteHelper extends SQLiteOpenHelper {

	public static final String TABLE_ROUTE = "routes";
	public static final String COLUMN_ID = "id";
	public static final String COLUMN_ROUTE_NAME = "routename";
	public static final String COLUMN_COLOUR = "colour";
	public static final String COLUMN_URL_ROUTE = "urlRoute";
	public static final String COLUMN_DIF = "difBetweenBus";
	public static final String COLUMN_START_TIME = "startTime";
	public static final String COLUMN_END_TIME = "endTime";
	public static final String COLUMN_PER_TOTAL = "timePerTotal";
	public static final String COLUMN_NUM_ONIBUS = "numBus";
	public static final String COLUMN_DAYS = "days";
	
	private static final String DATABASE_NAME = "favoritesRoutes.db";
	private static final int DATABASE_VERSION = 1;

	// Database creation sql statement
	private static final String DATABASE_CREATE = 
			"create table "	+ TABLE_ROUTE + "( " 
					+ COLUMN_ID + " integer primary key autoincrement, " 
					+ COLUMN_ROUTE_NAME + " text not null, " 
					+ COLUMN_COLOUR + " text, "
					+ COLUMN_URL_ROUTE + " text not null, "
					+ COLUMN_DIF + " integer, "
					+ COLUMN_START_TIME + " text, "
					+ COLUMN_END_TIME + " text, "
					+ COLUMN_PER_TOTAL + " integer, "
					+ COLUMN_NUM_ONIBUS + " integer, "
					+ COLUMN_DAYS + " text);";

	public RotaSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	//Deleta e Recria o BD
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(RotaSQLiteHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS" + TABLE_ROUTE);
		onCreate(db);
	}

}