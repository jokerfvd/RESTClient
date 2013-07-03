package com.example.restclient.provider;


import android.net.Uri;

public final class EstabelecimentosProviderContract {
	
	

		public static final String AUTHORITY = "com.example.restclient.estabelecimentosprovider";

		// ESTABELECIMENTO TABLE CONTRACT
		public static final class EstabelecimentoTable implements ResourceTable {

			public static final String TABLE_NAME = "estabelecimentos";

			// URI DEFS
			static final String SCHEME = "content://";
			public static final String URI_PREFIX = SCHEME + AUTHORITY;
			private static final String URI_PATH_ESTABELECIMENTO = "/" + TABLE_NAME;
			// Note the slash on the end of this one, as opposed to the URI_PATH_ESTABELECIMENTO, which has no slash.
			private static final String URI_PATH_PRODUTO = "/" + TABLE_NAME + "/";
			public static final int PRODUTO_ID_PATH_POSITION = 1;

			// content://com.example.restclient.estabelecimentosprovider/estabelecimento
			public static final Uri CONTENT_URI = Uri.parse(URI_PREFIX + URI_PATH_ESTABELECIMENTO);

			public static final String[] ALL_COLUMNS;
			public static final String[] DISPLAY_COLUMNS;

			static {
				ALL_COLUMNS = new String[] { 
						EstabelecimentoTable._ID, 
						EstabelecimentoTable._STATUS, 
						EstabelecimentoTable._RESULT, 					
						EstabelecimentoTable.NOME, 
						EstabelecimentoTable.ENDERECO,
						EstabelecimentoTable.TELEFONE,
						EstabelecimentoTable.GOSTEI,
						EstabelecimentoTable.LATITUDE,
						EstabelecimentoTable.LONGITUDE,
						EstabelecimentoTable.TID
				};
				
				DISPLAY_COLUMNS = new String[] { 
						EstabelecimentoTable._ID, 					
						EstabelecimentoTable.NOME, 
						EstabelecimentoTable.ENDERECO,
						EstabelecimentoTable.TELEFONE,
						EstabelecimentoTable.GOSTEI,
						EstabelecimentoTable.LATITUDE,
						EstabelecimentoTable.LONGITUDE,
						EstabelecimentoTable.TID
				};
			}
			
			
			public static final String NOME = "nome";
			public static final String ENDERECO = "endereco";
			public static final String TELEFONE = "telefone";
			public static final String GOSTEI = "gostei";
			public static final String LATITUDE = "latitude";
			public static final String LONGITUDE = "longitude";
			public static final String TID = "tid";

			
			// Prevent instantiation of this class
			private EstabelecimentoTable() {
			}
		}

		private EstabelecimentosProviderContract() {
			// disallow instantiation
		}

}
