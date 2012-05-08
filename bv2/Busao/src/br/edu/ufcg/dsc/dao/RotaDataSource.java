package br.edu.ufcg.dsc.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import br.edu.ufcg.dsc.bean.Rota;

public class RotaDataSource {

	// Database fields
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allColumns = {MySQLiteHelper.COLUMN_ID, MySQLiteHelper.COLUMN_ROUTE_NAME, MySQLiteHelper.COLUMN_EMPRESA,
			MySQLiteHelper.COLUMN_VIA, MySQLiteHelper.COLUMN_COR, MySQLiteHelper.COLUMN_URL, MySQLiteHelper.COLUMN_NUM_ONIBUS,
			MySQLiteHelper.COLUMN_NUM_ONIBUS_FDS, MySQLiteHelper.COLUMN_LAT_INICIAL, MySQLiteHelper.COLUMN_LGN_INICIAL,
			MySQLiteHelper.COLUMN_DIRECAO, MySQLiteHelper.COLUMN_FDS};
	
	public RotaDataSource(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}
	
	public Rota createRota (Rota rota){
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_ROUTE_NAME, rota.getRotaNome());
		values.put(MySQLiteHelper.COLUMN_EMPRESA, rota.getEmpresaId());
		values.put(MySQLiteHelper.COLUMN_VIA, rota.getVia());
		values.put(MySQLiteHelper.COLUMN_COR, rota.getCor());
		values.put(MySQLiteHelper.COLUMN_URL, rota.getUrlRota());
		values.put(MySQLiteHelper.COLUMN_NUM_ONIBUS, rota.getNumOnibus());
		values.put(MySQLiteHelper.COLUMN_NUM_ONIBUS_FDS, rota.getNumOnibusFDS());
		values.put(MySQLiteHelper.COLUMN_LAT_INICIAL, rota.getLatInicial());
		values.put(MySQLiteHelper.COLUMN_LGN_INICIAL, rota.getLgnInicial());
		values.put(MySQLiteHelper.COLUMN_DIRECAO, rota.getDirecao());
		values.put(MySQLiteHelper.COLUMN_FDS, rota.getFDS());
		long insertId = database.insert(MySQLiteHelper.TABLE_ROUTES, null, values);
		
		// To show how to query
		Cursor cursor = database.query(MySQLiteHelper.TABLE_ROUTES,
				allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null, null, null, null);
		cursor.moveToFirst();
		return cursorToRota(cursor);
	}

	public void deleteRota (Rota rota) {
		long id = rota.getId();
		database.delete(MySQLiteHelper.TABLE_ROUTES, MySQLiteHelper.COLUMN_ID + " = " + id, null);
	}
	
	public List<Rota> getAllRotas(){
		List<Rota> rotas = new ArrayList<Rota>();
		Cursor cursor = database.query(MySQLiteHelper.TABLE_ROUTES, allColumns, null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()){
			Rota rota = cursorToRota(cursor);
			rotas.add(rota);
			cursor.moveToNext();
		}
		
		// Make sure to close the cursor
		cursor.close();
		return rotas;
	}
	
	public List<Rota> getRota(String identificador) {
		return null;
		//TODO
	}
	
	private Rota cursorToRota(Cursor cursor) {
		Rota rota = new Rota();
		rota.setRotaNome(cursor.getString(1));
		rota.setEmpresaId(cursor.getInt(2));
		rota.setVia(cursor.getString(3));
		rota.setCor(cursor.getString(4));
		rota.setUrlRota(cursor.getString(5));
		rota.setNumOnibus(cursor.getString(6));
		rota.setNumOnibusFDS(cursor.getString(7));
		rota.setLatInicial(cursor.getString(8));
		rota.setLgnInicial(cursor.getString(9));
		rota.setDirecao(cursor.getString(10));
		rota.setFDS(cursor.getString(11));
		
		//Criar um objeto Horario (HorarioDataSource)
		//Isso tem que ser feito no Activity
		
		return rota;
	}
	
}
