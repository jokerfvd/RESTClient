package com.example.restclient.rest;

import java.net.URI;


import org.json.JSONArray;
import org.json.JSONException;

import com.example.restclient.resource.Estabelecimentos;
import com.example.restclient.rest.RestMethodFactory.Method;

import android.content.Context;

public class GetEstabelecimentosRestMethod extends AbstractRestMethod<Estabelecimentos> {
	
	private Context mContext;

	private static final URI ESTABELECIMENTO_URI = URI
			.create("http://restserveruff.herokuapp.com/estabelecimentos.json");
	
	public GetEstabelecimentosRestMethod(Context context) {
		mContext = context.getApplicationContext();
	}

	@Override
	protected Request buildRequest() {

		return new Request(Method.GET, ESTABELECIMENTO_URI, null, null);
	}
	
	@Override
	protected RestMethodResult<Estabelecimentos> buildResult(Response response) {

		int status = response.status;
		String statusMsg = "";
		String responseBody = new String(response.body);
		Estabelecimentos resource = null;
		try {
			resource = new Estabelecimentos(new JSONArray(responseBody));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

/*		
		try {
			//responseBody = new String(response.body, getCharacterEncoding(response.headers));
			resource = parseResponseBody(responseBody);
		} catch (Exception ex) {
			// TODO Should we set some custom status code?
			status = 506; // spec only defines up to 505
			statusMsg = ex.getMessage();
		}
*/		
		return new RestMethodResult<Estabelecimentos>(status, statusMsg, resource);
	}

	@Override
	protected Estabelecimentos parseResponseBody(String responseBody) throws Exception {
		JSONArray jArray = new JSONArray(new String(responseBody));
		return new Estabelecimentos(jArray);
	}

	@Override
	protected Context getContext() {
		return mContext;
	}

}
