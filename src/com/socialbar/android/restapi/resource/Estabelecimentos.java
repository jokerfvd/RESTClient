package com.socialbar.android.restapi.resource;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;


public class Estabelecimentos implements Resource {

	private ArrayList<Estabelecimento> lista;

	public Estabelecimentos(JSONArray jArray) throws JSONException {
		lista = new ArrayList<Estabelecimento>();		
		for(int i=0; i < jArray.length(); i++){
			Estabelecimento estabelecimento = new Estabelecimento(jArray.getJSONObject(i));
		    lista.add(estabelecimento);
		 }
	}

	public ArrayList<Estabelecimento> getLista() {
		return lista;
	}

}
