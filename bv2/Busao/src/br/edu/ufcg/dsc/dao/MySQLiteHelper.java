package br.edu.ufcg.dsc.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

	//Rota
	public static final String TABLE_ROUTES = "rotas";
	public static final String COLUMN_ID = "id";
	public static final String COLUMN_ROUTE_NAME = "nome";
	public static final String COLUMN_EMPRESA = "empresaId";
	public static final String COLUMN_VIA = "via";
	public static final String COLUMN_COR = "cor";
	public static final String COLUMN_URL = "url";
	public static final String COLUMN_NUM_ONIBUS = "numOnibus";
	public static final String COLUMN_NUM_ONIBUS_FDS = "numOnibusFDS";
	public static final String COLUMN_LAT_INICIAL = "latInicio";
	public static final String COLUMN_LGN_INICIAL = "lgnInicio";
	public static final String COLUMN_DIRECAO = "direcao";
	public static final String COLUMN_FDS = "FDS";
	
	//Horario
	public static final String TIME_ROUTES = "horario";
	public static final String COLUMN_HORARIO_ID = "id";
	public static final String COLUMN_ROTA_ID = "rotaId";
	public static final String COLUMN_DIF_ONIBUS = "difEntreOnibus";
	public static final String COLUMN_DIF_ONIBUS_FDS = "difEntreOnibusFDS";
	public static final String COLUMN_HORA_INICIO = "horaInicio";
	public static final String COLUMN_HORA_FIM = "horaFim";
	public static final String COLUMN_HORA_INICIO_FDS = "horaInicioFDS";
	public static final String COLUMN_HORA_FIM_FDS = "horaFimFDS";
	public static final String COLUMN_TEMPO_TOTAL = "tempoPerTotal";
	public static final String COLUMN_TEMPO_TOTAL_FDS = "tempoPerTotalFDS";

	private static final String DATABASE_NAME = "rotas.db";
	private static final int DATABASE_VERSION = 1;

	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table " + TABLE_ROUTES + "( " 
			+ COLUMN_ID + " integer primary key autoincrement, " 
			+ COLUMN_ROUTE_NAME + " text not null, "
			+ COLUMN_EMPRESA + " integer not null, "
			+ COLUMN_VIA + " text, "
			+ COLUMN_COR + " text, "
			+ COLUMN_URL + " text not null, " 
			+ COLUMN_NUM_ONIBUS + " integer not null, "
			+ COLUMN_LAT_INICIAL + " real, "
			+ COLUMN_LGN_INICIAL + " real, "
			+ COLUMN_NUM_ONIBUS_FDS + " integer not null, "
			+ COLUMN_DIRECAO + " text, "
			+ COLUMN_FDS + " text, );\n"
			
			+ "create table " + TIME_ROUTES + "( " 
			+ COLUMN_HORARIO_ID + " integer primary key autoincrement, " 
			+ COLUMN_ROTA_ID + " integer not null, "
			+ COLUMN_DIF_ONIBUS + " integer not null, "
			+ COLUMN_DIF_ONIBUS_FDS +  "integer not null, "
			+ COLUMN_HORA_INICIO + " integer not null, "
			+ COLUMN_HORA_FIM +  "integer not null, "
			+ COLUMN_HORA_INICIO_FDS + " integer not null, "
			+ COLUMN_HORA_FIM_FDS +  "integer not null, "
			+ COLUMN_TEMPO_TOTAL +  "integer not null, "
			+ COLUMN_TEMPO_TOTAL_FDS + " integer not null, );";

	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(MySQLiteHelper.class.getName(), "Upgrading database from version " + oldVersion + " to " 	+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS" + TABLE_ROUTES);
		onCreate(db);
	}

}

