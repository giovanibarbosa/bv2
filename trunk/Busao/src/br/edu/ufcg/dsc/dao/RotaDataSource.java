package br.edu.ufcg.dsc.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.conn.routing.RouteTracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class RotaDataSource {

	private SQLiteDatabase database;
	private RotaSQLiteHelper dbHelper;
	private String[] allColumns = {RotaSQLiteHelper.COLUMN_ID, RotaSQLiteHelper.COLUMN_ROUTE_NAME, RotaSQLiteHelper.COLUMN_COLOUR, 
			RotaSQLiteHelper.COLUMN_URL_ROUTE, RotaSQLiteHelper.COLUMN_DIF, RotaSQLiteHelper.COLUMN_START_TIME, RotaSQLiteHelper.COLUMN_END_TIME,
			RotaSQLiteHelper.COLUMN_PER_TOTAL, RotaSQLiteHelper.COLUMN_NUM_ONIBUS, RotaSQLiteHelper.COLUMN_DAYS};
	
	public RotaDataSource(Context context) {
		dbHelper = new RotaSQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}
	
	public Rota createRota (String routeName, String colour, String url, Integer difEntreOnibus,
			String starTime, String endTime, Integer perTotal, Integer numOnibus, String dias){
		
		Cursor cursor1 = database.query(RotaSQLiteHelper.TABLE_ROUTE,
				allColumns, RotaSQLiteHelper.COLUMN_ROUTE_NAME + " = " + routeName, null, null, null, null);
		
		if (!cursor1.moveToFirst()){ // check if there is data
			
			ContentValues values = new ContentValues();
			values.put(RotaSQLiteHelper.COLUMN_ROUTE_NAME, routeName);
			values.put(RotaSQLiteHelper.COLUMN_COLOUR, colour);
			values.put(RotaSQLiteHelper.COLUMN_URL_ROUTE, url);
			values.put(RotaSQLiteHelper.COLUMN_DIF, difEntreOnibus);
			values.put(RotaSQLiteHelper.COLUMN_START_TIME, starTime);
			values.put(RotaSQLiteHelper.COLUMN_END_TIME, endTime);
			values.put(RotaSQLiteHelper.COLUMN_PER_TOTAL, perTotal);
			values.put(RotaSQLiteHelper.COLUMN_NUM_ONIBUS, numOnibus);
			values.put(RotaSQLiteHelper.COLUMN_DAYS, dias);
			
			long insertId = database.insert(RotaSQLiteHelper.TABLE_ROUTE, null,values);
			
			Cursor cursor = database.query(RotaSQLiteHelper.TABLE_ROUTE,
					allColumns, RotaSQLiteHelper.COLUMN_ID + " = " + insertId, null,
					null, null, null);
			
			cursor.close();
			cursor.moveToFirst();
			return cursorToRota(cursor);
		} else {
			//Rota ja cadastrada
			cursor1.close();
			return null;
		}
		
	}
	
	public void deleteRota(Rota rota) {
		long id = rota.getId();
		System.out.println("Comment deleted with id: " + id);
		database.delete(RotaSQLiteHelper.TABLE_ROUTE, RotaSQLiteHelper.COLUMN_ID + " = " + id, null);
	}
	
	public void deleteRota(String routename) {
		database.delete(RotaSQLiteHelper.TABLE_ROUTE, RotaSQLiteHelper.COLUMN_ROUTE_NAME + " = " + routename, null);
	}
	
	public long getIdFromRoute(String routename){
		String[] strArray = new String[]{RotaSQLiteHelper.COLUMN_ID};
		Cursor cursor = database.query(RotaSQLiteHelper.TABLE_ROUTE,
				strArray, RotaSQLiteHelper.COLUMN_ROUTE_NAME + " = " + routename, null,null, null, null);
		
		if (cursor.moveToFirst()){
			return cursor.getLong(0);
		}
		
		return 0; 
	}
	
	public List<Rota> getAllRoutes() {
		List<Rota> rotas = new ArrayList<Rota>();

		Cursor cursor = database.query(RotaSQLiteHelper.TABLE_ROUTE, allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Rota r = cursorToRota(cursor);
			rotas.add(r);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return rotas;
	}
	
	
	private Rota cursorToRota(Cursor cursor) {
		Rota rota = new Rota();
		
		rota.setId(cursor.getInt(0));
		rota.setRoutename(cursor.getString(1));
		rota.setColour(cursor.getString(2));
		rota.setUrlRoute(cursor.getString(3));
		rota.setDifBetweenBus(cursor.getInt(4));
		rota.setStartTime(cursor.getString(5));
		rota.setEndTime(cursor.getString(6));
		rota.setTimePerTotal(cursor.getInt(7));
		rota.setNumBus(cursor.getInt(8));
		rota.setDays(cursor.getString(9));
		
		return rota;
	}
}