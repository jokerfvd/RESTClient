package com.socialbar.android.restapi.service;

import com.socialbar.android.restapi.provider.EstabelecimentosConstants;
import com.socialbar.android.restapi.resource.Estabelecimento;
import com.socialbar.android.restapi.rest.RestMethod;
import com.socialbar.android.restapi.rest.RestMethodFactory;
import com.socialbar.android.restapi.rest.RestMethodResult;
import com.socialbar.android.restapi.rest.RestMethodFactory.Method;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;

class EstabelecimentoProcessor {

	protected static final String TAG = EstabelecimentoProcessor.class
			.getSimpleName();

	private Context mContext;

	public EstabelecimentoProcessor(Context context) {
		mContext = context;
	}

	void getEstabelecimento(ProcessorCallback callback) {
		@SuppressWarnings("unchecked")
		RestMethod<Estabelecimento> getRestMethod = RestMethodFactory
				.getInstance(mContext).getRestMethod(
						EstabelecimentosConstants.CONTENT_URI, Method.GET,
						null, null);
		RestMethodResult<Estabelecimento> result = getRestMethod.execute();
		updateContentProvider(result);
		callback.send(result.getStatusCode());

	}

	private void updateContentProvider(RestMethodResult<Estabelecimento> result) {

		if (result != null && result.getResource() != null) {

			Estabelecimento resource = result.getResource();

			ContentValues values = new ContentValues();
			values.put(EstabelecimentosConstants.NOME, resource.getNome());
			values.put(EstabelecimentosConstants.ENDERECO,
					resource.getEndereco());
			values.put(EstabelecimentosConstants.TELEFONE,
					resource.getTelefone());
			values.put(EstabelecimentosConstants.GOSTEI, resource.getGostei());
			values.put(EstabelecimentosConstants.LATITUDE,
					resource.getLatitude());
			values.put(EstabelecimentosConstants.LONGITUDE,
					resource.getLatitude());

			Cursor cursor = mContext.getContentResolver().query(
					EstabelecimentosConstants.CONTENT_URI, null, null, null,
					null);
			if (cursor.moveToFirst()) {
				int id = cursor.getInt(cursor
						.getColumnIndexOrThrow(BaseColumns._ID));
				mContext.getContentResolver().update(
						ContentUris.withAppendedId(
								EstabelecimentosConstants.CONTENT_URI, id),
						values, null, null);
			} else {
				mContext.getContentResolver().insert(
						EstabelecimentosConstants.CONTENT_URI, values);
			}
			cursor.close();
		}

	}

}
