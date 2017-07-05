package sffmobile.cesar.com.br.sffmobile;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper{

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = " sff.db";
	private static final String DATABASE_CREATE_SCRIPT = "CREATE TABLE tipo_gasto(id INTEGER PRIMARY KEY, versao INTEGER, descricao TEXT NOT NULL, desativado INTEGER NOT NULL)";
	
	
	public SQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE_SCRIPT);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(" DROP TABLE IF EXISTS tipo_gasto" );
		onCreate(db);
	}

}
