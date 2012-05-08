package br.edu.ufcg.dsc.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import br.edu.ufcg.dsc.bean.Horario;

public class HorarioDataSource {

	// Database fields
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allColumns = {MySQLiteHelper.COLUMN_HORARIO_ID, MySQLiteHelper.COLUMN_ROTA_ID, MySQLiteHelper.COLUMN_DIF_ONIBUS,
			MySQLiteHelper.COLUMN_DIF_ONIBUS_FDS, MySQLiteHelper.COLUMN_HORA_INICIO, MySQLiteHelper.COLUMN_HORA_FIM, 
			MySQLiteHelper.COLUMN_HORA_INICIO_FDS, MySQLiteHelper.COLUMN_HORA_FIM_FDS, MySQLiteHelper.COLUMN_TEMPO_TOTAL,
			MySQLiteHelper.COLUMN_TEMPO_TOTAL_FDS};
	
	public HorarioDataSource(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}
	
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}
	
	public Horario createHorario (Horario horario){
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_ROTA_ID, horario.getRotaId());
		values.put(MySQLiteHelper.COLUMN_DIF_ONIBUS, horario.getDifEntreOnibus());
		values.put(MySQLiteHelper.COLUMN_DIF_ONIBUS_FDS, horario.getDifEntreOnibusFDS());
		values.put(MySQLiteHelper.COLUMN_HORA_INICIO, horario.getHoraInicio());
		values.put(MySQLiteHelper.COLUMN_HORA_FIM, horario.getHoraFim());
		values.put(MySQLiteHelper.COLUMN_HORA_INICIO_FDS, horario.getHoraInicioFDS());
		values.put(MySQLiteHelper.COLUMN_HORA_FIM_FDS, horario.getHoraFimFDS());
		values.put(MySQLiteHelper.COLUMN_TEMPO_TOTAL, horario.getTempoPerTotal());
		values.put(MySQLiteHelper.COLUMN_TEMPO_TOTAL_FDS, horario.getTempoPerTotalFDS());

		long insertId = database.insert(MySQLiteHelper.TIME_ROUTES, null, values);
		
		// To show how to query
		Cursor cursor = database.query(MySQLiteHelper.TIME_ROUTES,
				allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null, null, null, null);
		cursor.moveToFirst();
		return cursorToHorario(cursor);
	}
	
	
	private Horario cursorToHorario(Cursor cursor) {
		Horario horario = new Horario();

		horario.setRotaId(cursor.getInt(1));
		horario.setDifEntreOnibus(cursor.getString(2));
		horario.setDifEntreOnibusFDS(cursor.getString(3));
		horario.setHoraInicio(cursor.getString(4));
		horario.setHoraFim(cursor.getString(5));
		horario.setHoraInicioFDS(cursor.getString(6));
		horario.setHoraFimFDS(cursor.getString(7));
		horario.setTempoPerTotal(cursor.getString(8));
		horario.setTempoPerTotal(cursor.getString(9));
		
		return horario;
	}
	
	public void deleteHorario (Horario horario) {
		long id = horario.getId();
		database.delete(MySQLiteHelper.TIME_ROUTES, MySQLiteHelper.COLUMN_ID + " = " + id, null);
	}
	
	public Horario getHorarioByRotaId(int rotaId){
		//TODO
		return null;
	}
	
}
