package com.example.restclient.provider;

import android.net.Uri;

public class EstabelecimentosConstants {

	public static final String TABLE_NAME = "estabelecimentos";
	
	public static final String USUARIOS_TABLE_NAME = "usuarios";
	
	public static final String PRODUTOS_TABLE_NAME = "produtos";
	
	public static final String CARACTERISTICAS_TABLE_NAME = "caracteristicas";
	
	//symbolic name of the entire provider
	public static final String AUTHORITY = "com.example.restclient.estabelecimentos";
	
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);

	// Columns in the Estabelecimentos database
	public static final String NOME = "nome";
	public static final String ENDERECO = "endereco";
	public static final String TELEFONE = "telefone";
	public static final String RANK = "rank";
	public static final String COORDENADA = "coordenada";

}
