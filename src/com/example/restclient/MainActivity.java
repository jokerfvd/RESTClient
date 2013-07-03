package com.example.restclient;

import java.net.URI;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.restclient.provider.EstabelecimentosConstants;
import com.example.restclient.provider.ResourceTable;
import com.example.restclient.resource.Estabelecimento;
import com.example.restclient.resource.Estabelecimentos;
import com.example.restclient.rest.GetEstabelecimentosRestMethod;
import com.example.restclient.rest.Request;
import com.example.restclient.rest.Response;
import com.example.restclient.rest.RestClient;
import com.example.restclient.rest.RestMethodResult;
import com.example.restclient.rest.RestMethodFactory.Method;
import com.example.restclient.service.ServiceHelper;
import com.example.restclient.util.Logger;

import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;


public class MainActivity extends ListActivity {
	private static final String TAG = MainActivity.class.getSimpleName();
	
	//LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
    ArrayList<String> listItems=new ArrayList<String>();

    //DEFINING STRING ADAPTER WHICH WILL HANDLE DATA OF LISTVIEW
    ArrayAdapter<String> adapter;
    
    private Long requestId;
	private BroadcastReceiver requestReceiver;
    private ServiceHelper mServiceHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,listItems);
	    setListAdapter(adapter);
	    
	    mServiceHelper = ServiceHelper.getInstance(this.getApplicationContext());
	    
	    //monitorando o click no listView
	    getListView().setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	Intent intent = new Intent(MainActivity.this, VerEstabelecimento.class);
                intent.putExtra("ID", parent.getItemAtPosition(position).toString());
                startActivity(intent);      
			}
		});
		
		//adicionando permissão para atividade network na main
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	    StrictMode.setThreadPolicy(policy);
				
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

				Logger.debug(TAG, "Received intent " + intent.getAction() + ", request ID "
						+ resultRequestId);

				if (resultRequestId == requestId) {

					Logger.debug(TAG, "Result is for our request ID");

					int resultCode = intent.getIntExtra(ServiceHelper.EXTRA_RESULT_CODE, 0);

					Logger.debug(TAG, "Result code = " + resultCode);

					if (resultCode == 200) {

						Logger.debug(TAG, "Updating UI with new data");

						atualizaEstabelecimentos();
						//showNameToast(name);

					} else {
						//showToast(getString(R.string.error_occurred));
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
			///setRefreshing(true);
		} else {
			//setRefreshing(false);
			atualizaEstabelecimentos();
			//showNameToast(name);
		}	
	}
	
	
	private void atualizaEstabelecimentos() {
		Cursor cursor = getContentResolver().query(EstabelecimentosConstants.CONTENT_URI, null, null, null, null);

		if (cursor.moveToFirst()) {
		    int index = cursor.getColumnIndexOrThrow(EstabelecimentosConstants.NOME);
			String nome = cursor.getString(index);
			listItems.add(nome);
		}
		cursor.close();
		adapter.notifyDataSetChanged();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void listarEstabelecimentos(View view){
		System.out.println("listarEstabelecimentos");
		
		//MANEIRA CERTA
		requestId = mServiceHelper.getEstabelecimentos();
		
		/* MANEIRA ERRADA
		URI uri = URI.create("http://restserveruff.herokuapp.com/estabelecimentos.json");
		Request request = new Request(Method.GET, uri, null, null);
		RestClient client = new RestClient();
		Response response = client.execute(request);
		if (response.status == 200)
		{
			JSONArray jArray;
			try {
				jArray = new JSONArray(new String(response.body));
				listItems.clear();
				for(int i=0; i < jArray.length(); i++){
				      JSONObject jObject = jArray.getJSONObject(i);
				      String id = jObject.getString("id");
				      String nome = jObject.getString("nome");
				      listItems.add(id);
				 }
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			adapter.notifyDataSetChanged();
			
		}
		//*/	
		
    }
	
	public void criarEstabelecimento(View view) {
		Intent intent = new Intent(MainActivity.this, CriarEstabelecimento.class);
		int requestCode = 1; // Or some number you choose
		startActivityForResult(intent,requestCode); 
	}
	
	@Override
	protected void onPause() {
		super.onPause();

		// Unregister for broadcast
		if (requestReceiver != null) {
			try {
				this.unregisterReceiver(requestReceiver);
			} catch (IllegalArgumentException e) {
				Logger.error(TAG, e.getLocalizedMessage(), e);
			}
		}
	}

}
