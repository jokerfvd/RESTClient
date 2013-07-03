package com.socialbar.android.restapi.restclient;

import java.net.URI;
import java.util.Arrays;


import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import com.example.restclient.R;
import com.socialbar.android.restapi.provider.EstabelecimentosConstants;
import com.socialbar.android.restapi.rest.Request;
import com.socialbar.android.restapi.rest.Response;
import com.socialbar.android.restapi.rest.RestClient;
import com.socialbar.android.restapi.rest.RestMethodFactory.Method;
import com.socialbar.android.restapi.service.ServiceHelper;
import com.socialbar.android.restapi.util.Logger;

import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class VerEstabelecimento extends Activity {
	private static final String TAG = MainActivity.class.getSimpleName();
	// "authenticity_token"=>"S9IoNL9/cwzQu3mlE0eGE9nDC6pXsVab2vriREcs0NE="
	
	private Long requestId;
	private BroadcastReceiver requestReceiver;
    private ServiceHelper mServiceHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ver_estabelecimento);

		// adicionando permissão para atividade network na main
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		Intent intent = getIntent();
		String id = intent.getStringExtra("ID");
		
		//MANEIRA CERTA
		requestId = mServiceHelper.getEstabelecimento(id);

		/*
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
		*/
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		IntentFilter filter = new IntentFilter(ServiceHelper.ACTION_REQUEST_RESULT);
		requestReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {

				long resultRequestId = intent
						.getLongExtra(ServiceHelper.EXTRA_REQUEST_ID, 0);

				if (resultRequestId == requestId) {

					int resultCode = intent.getIntExtra(ServiceHelper.EXTRA_RESULT_CODE, 0);

					if (resultCode == 200) {

						preencheDados();
					} else {
					}
				} else {
					Logger.debug(TAG, "Result is NOT for our request ID");
				}

			}
		};

		mServiceHelper = ServiceHelper.getInstance(this);
		this.registerReceiver(requestReceiver, filter);

		if (requestId == null) {
		} else if (mServiceHelper.isRequestPending(requestId)) {
		} else {
			preencheDados();
		}	
	}
	
	private void preencheDados() {
		Cursor cursor = getContentResolver().query(EstabelecimentosConstants.CONTENT_URI, null, null, null, null);

		if (cursor.moveToFirst()) {
			EditText nome = (EditText) findViewById(R.id.EditText04);
		    int index = cursor.getColumnIndexOrThrow(EstabelecimentosConstants.NOME);
			nome.setText(cursor.getString(index));
			EditText endereco = (EditText) findViewById(R.id.EditText01);
			index = cursor.getColumnIndexOrThrow(EstabelecimentosConstants.ENDERECO);
			endereco.setText(cursor.getString(index));
			EditText telefone = (EditText) findViewById(R.id.EditText02);
			index = cursor.getColumnIndexOrThrow(EstabelecimentosConstants.TELEFONE);
			telefone.setText(cursor.getString(index));
			EditText gostei = (EditText) findViewById(R.id.EditText03);
			index = cursor.getColumnIndexOrThrow(EstabelecimentosConstants.GOSTEI);
			gostei.setText(cursor.getString(index));

			//EditText idEstabelecimento = (EditText) findViewById(R.id.idEstabelecimento);
			//index = cursor.getColumnIndexOrThrow(EstabelecimentosConstants.);
			//idEstabelecimento.setText(id);
		}
		cursor.close();
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
					.setText("O estabelecimento não pode ser editado!");
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
					.setText("O estabelecimento não pode ser deletado!");
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
		 * ((EditText)mensagem).setText("O estabelecimento não pode ser deletado!"
		 * ); } } catch (ClientProtocolException e) { // TODO Auto-generated
		 * catch block e.printStackTrace(); } catch (IOException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); } //
		 */
	}

}
