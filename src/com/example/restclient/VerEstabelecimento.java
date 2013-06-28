package com.example.restclient;

import java.net.URI;
import java.util.Arrays;


import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import com.example.restclient.rest.Request;
import com.example.restclient.rest.Response;
import com.example.restclient.rest.RestClient;
import com.example.restclient.rest.RestMethodFactory.Method;

import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class VerEstabelecimento extends Activity {

	// "authenticity_token"=>"S9IoNL9/cwzQu3mlE0eGE9nDC6pXsVab2vriREcs0NE="

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ver_estabelecimento);

		// adicionando permiss�o para atividade network na main
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		Intent intent = getIntent();
		String id = intent.getStringExtra("ID");

		URI uri = URI
				.create("http://restserveruff.herokuapp.com/estabelecimentos/"
						+ id + ".json");
		// URI uri =
		// URI.create("http://localhost:3000/estabelecimentos/"+id+".json");
		Request request = new Request(Method.GET, uri, null, null);

		RestClient client = new RestClient();
		Response response = client.execute(request);

		if (response.status == 200) {
			try {
				JSONObject jObject = new JSONObject(new String(response.body));

				EditText endereco = (EditText) findViewById(R.id.EditText01);
				endereco.setText(jObject.getString("endereco"));
				EditText telefone = (EditText) findViewById(R.id.EditText02);
				telefone.setText(jObject.getString("telefone"));
				EditText rank = (EditText) findViewById(R.id.EditText03);
				rank.setText(jObject.getString("rank"));
				EditText nome = (EditText) findViewById(R.id.EditText04);
				nome.setText(jObject.getString("nome"));

				EditText idEstabelecimento = (EditText) findViewById(R.id.idEstabelecimento);
				idEstabelecimento.setText(id);

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ver_estabelecimento, menu);
		return true;
	}

	public void EditarEstabelecimento(View view) throws JSONException {
		System.out.println("EditarEstabelecimento");

		EditText idEstabelecimento = (EditText) findViewById(R.id.idEstabelecimento);

		URI uri = URI
				.create("http://restserveruff.herokuapp.com/estabelecimentos/"
						+ idEstabelecimento.getText() + ".json");

		JSONStringer json = new JSONStringer().object().key("estabelecimento")
				.object().key("nome")
				.value(((EditText) findViewById(R.id.EditText04)).getText())
				.key("endereco")
				.value(((EditText) findViewById(R.id.EditText01)).getText())
				.key("telefone")
				.value(((EditText) findViewById(R.id.EditText02)).getText())
				.key("rank")
				.value(((EditText) findViewById(R.id.EditText03)).getText())
				.endObject().endObject();

		Request request = new Request(Method.PUT, uri, null, json.toString()
				.getBytes());
		request.addHeader("Content-Type", Arrays.asList("application/json"));
		RestClient client = new RestClient();
		Response response = client.execute(request);
		View mensagem = findViewById(R.id.mensagem);
		mensagem.setVisibility(View.VISIBLE);

		if (response.status == 204)// NO CONTENT
		{
			System.out.println(new String(response.body));

			((EditText) mensagem)
					.setText("Estabelecimento editado com sucesso!");
		} else {
			((EditText) mensagem)
					.setText("O estabelecimento n�o pode ser editado!");
		}

	}

	public void DeletarEstabelecimento(View view) {
		System.out.println("DeletarEstabelecimento");
		EditText idEstabelecimento = (EditText) findViewById(R.id.idEstabelecimento);
		URI uri = URI
				.create("http://restserveruff.herokuapp.com/estabelecimentos/"
						+ idEstabelecimento.getText());
		// URI uri =
		// URI.create("http://localhost:3000/estabelecimentos/"+idEstabelecimento.getText());

		// /*
		Request request = new Request(Method.DELETE, uri, null, null);
		RestClient client = new RestClient();
		Response response = client.execute(request);
		View mensagem = findViewById(R.id.mensagem);
		mensagem.setVisibility(View.VISIBLE);
		if (response.status == 200) {
			((EditText) mensagem)
					.setText("Estabelecimento deletado com sucesso!");
			finish();
		} else {
			((EditText) mensagem)
					.setText("O estabelecimento n�o pode ser deletado!");
		}
		// */

		/*
		 * HttpClient client = new DefaultHttpClient(); HttpResponse response;
		 * try { response = client.execute(new HttpDelete(uri));
		 * 
		 * View mensagem = findViewById(R.id.mensagem);
		 * mensagem.setVisibility(View.VISIBLE);
		 * 
		 * //if (response.status == 200) if
		 * (response.getStatusLine().getStatusCode() == 200) {
		 * //System.out.println(new String(response.body));
		 * 
		 * ((EditText)mensagem).setText("Estabelecimento deletado com sucesso!");
		 * finish(); } else {
		 * ((EditText)mensagem).setText("O estabelecimento n�o pode ser deletado!"
		 * ); } } catch (ClientProtocolException e) { // TODO Auto-generated
		 * catch block e.printStackTrace(); } catch (IOException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); } //
		 */
	}

}