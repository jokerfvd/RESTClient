package com.example.restclient;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;


import org.json.JSONException;
import org.json.JSONStringer;

import com.example.restclient.rest.Request;
import com.example.restclient.rest.Response;
import com.example.restclient.rest.RestClient;
import com.example.restclient.rest.RestMethodFactory.Method;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class CriarEstabelecimento extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_criar_estabelecimento);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.criar_estabelecimento, menu);
		return true;
	}

	public void criar(View view) {
		URI uri = URI
				.create("http://restserveruff.herokuapp.com/estabelecimentos");

		try {

			JSONStringer json = new JSONStringer()
					.object()
					.key("estabelecimento")
					.object()
					.key("nome")
					.value(((EditText) findViewById(R.id.EditText04)).getText())
					.key("endereco")
					.value(((EditText) findViewById(R.id.EditText01)).getText())
					.key("telefone")
					.value(((EditText) findViewById(R.id.EditText02)).getText())
					.key("rank")
					.value(((EditText) findViewById(R.id.EditText03)).getText())
					.endObject().endObject();

			Request request = new Request(Method.POST, uri, null, json.toString().getBytes());
			RestClient client = new RestClient();
			request.addHeader("Content-Type", Arrays.asList("application/json"));
			Response response = client.execute(request);

			if (response.status == 200) {
				System.out.println("OKKKKKKKK");
				setResult(RESULT_OK, null);
				finish();
			} else {
				setResult(RESULT_CANCELED, null);
				finish();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
