package com.example.restclient.resource;

import org.json.JSONException;
import org.json.JSONObject;


public class Estabelecimento implements Resource {

	private String nome;
	private String endereco;
	private String telefone;
	private String rank;
	private int id;

	public Estabelecimento(JSONObject json) throws JSONException {		
		this.nome = json.getString("nome");
		this.endereco = json.getString("endereco");
		this.telefone = json.getString("telefone");
		this.rank = json.getString("rank");
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

	public String getRank() {
		return rank;
	}

	public String getNome() {
		return nome;
	}
}
