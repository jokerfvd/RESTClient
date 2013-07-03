package com.socialbar.android.restapi.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * This creates, updates, and opens the database.  Opening is handled by the superclass, we handle 
 * the create & upgrade steps
 */
public class ProviderDbHelper extends SQLiteOpenHelper {

	public final String TAG = getClass().getSimpleName();

	//Name of the database file
	private static final String DATABASE_NAME = "restdroid.db";
	private static final int DATABASE_VERSION = 3;

	public ProviderDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/**
	 * Só é chamado na 1ª vez que o apk é instalado, depois é chamado o onUpgrade quando muda a versão
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		
		// CREATE Estabelecimento TABLE
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("CREATE TABLE " + EstabelecimentosConstants.TABLE_NAME + " (");
		sqlBuilder.append(ResourceTable._ID + " INTEGER, ");
		sqlBuilder.append(ResourceTable._STATUS + " TEXT, ");
		sqlBuilder.append(ResourceTable._RESULT + " INTEGER, ");
		sqlBuilder.append(EstabelecimentosConstants.NOME + " TEXT, ");
		sqlBuilder.append(EstabelecimentosConstants.ENDERECO + " TEXT, ");
		sqlBuilder.append(EstabelecimentosConstants.TELEFONE + " TEXT, ");
		sqlBuilder.append(EstabelecimentosConstants.GOSTEI + " INTEGER, ");
		sqlBuilder.append(EstabelecimentosConstants.LATITUDE + " TEXT, ");
		sqlBuilder.append(EstabelecimentosConstants.LONGITUDE + " TEXT ");
		sqlBuilder.append(");");
		
		String sql = sqlBuilder.toString();
		Log.i(TAG, "Creating DB table with string: '" + sql + "'");
		db.execSQL(sql);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//Gets called when the database is upgraded, i.e. the version number changes
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("DROP TABLE " + EstabelecimentosConstants.TABLE_NAME +";");
		String sql = sqlBuilder.toString();
		Log.i(TAG, "Removendo DB table com a sql: '" + sql + "'");
		db.execSQL(sql);
		
		onCreate(db);
	}

}
