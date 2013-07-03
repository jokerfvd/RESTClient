package com.socialbar.android.restapi.rest;

import java.net.URI;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.socialbar.android.restapi.resource.Estabelecimento;
import com.socialbar.android.restapi.rest.RestMethodFactory.Method;

import android.content.Context;

public class GetEstabelecimentoRestMethod extends
		AbstractRestMethod<Estabelecimento> {

	private Context mContext;

	private URI ESTABELECIMENTO_URI;

	public GetEstabelecimentoRestMethod(Context context, Map<String, List<String>> header) {
		mContext = context.getApplicationContext();
		int id = Integer.parseInt(header.get("ID").get(0));
		ESTABELECIMENTO_URI = URI
				.create("http://restserveruff.herokuapp.com/estabelecimentos/"
						+ Integer.toString(id) + ".json");
	}

	@Override
	protected Request buildRequest() {
		return new Request(Method.GET, ESTABELECIMENTO_URI, null, null);
	}

	@Override
	protected RestMethodResult<Estabelecimento> buildResult(Response response) {

		int status = response.status;
		String statusMsg = "";
		String responseBody = new String(response.body);
		Estabelecimento resource = null;
		try {
			resource = new Estabelecimento(new JSONObject(responseBody));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new RestMethodResult<Estabelecimento>(status, statusMsg,
				resource);
	}

	@Override
	protected Estabelecimento parseResponseBody(String responseBody)
			throws Exception {
		JSONObject json = new JSONObject(new String(responseBody));
		return new Estabelecimento(json);
	}

	@Override
	protected Context getContext() {
		return mContext;
	}

}
