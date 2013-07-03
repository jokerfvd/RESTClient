package com.socialbar.android.restapi.resource;

import org.json.JSONException;
import org.json.JSONObject;


public class Estabelecimento implements Resource {

	private String nome;
	private String endereco;
	private String telefone;
	private int gostei;
	private double latitude;
	private double longitude;
	private int id;

	public Estabelecimento(JSONObject json) throws JSONException {		
		this.nome = json.getString("nome");
		this.endereco = json.getString("endereco");
		this.telefone = json.getString("telefone");
		this.gostei = Integer.parseInt(json.getString("gostei"));
		this.latitude = Double.parseDouble(json.getString("latitude"));
		this.longitude = Double.parseDouble(json.getString("longitude"));
		this.id = Integer.parseInt(json.getString("id"));
	}

	public int getId() {
		return id;
	}

	public String getEndereco() {
		return endereco;
	}

	public String getTelefone() {
		return telefone;
	}

	public int getGostei() {
		return gostei;
	}

	public String getNome() {
		return nome;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}
}
