package com.socialbar.android.restapi.restclient;

import java.math.BigDecimal;
import java.util.List;

public interface IModelo {
	
	List<Object> getEstabelecimentos();
	IUsuario getUsuario(String nome, String senha);
	IUsuario getUsuario();
	List<Object> getEstabelecimentos(double latitude, double longitude);
	List<Object> getEstabelecimentos(String... args);

}

interface IEstabelecimento{
	List<Object> getProdutos();
	void addGostei(Object usuario);
	void removeGostei(Object usuario);
	void addComentario(Object usuario);
	void removeComentario(Object usuario);
	/*
	 *O checkin é feito manualmente pelo usuário, porem o checkout é feito automaticamete
	 *quando o usário se afasta de estabelecimento.
	 */
	boolean checkIn();
	int getClientesDoEstabelecimento();
	
	//atributos
	String getNome();
	String getEndereco();
	String getTelefone();
	String getCoordenada();
	List<Object> getCaracteristicas();
	
	void setNome(String nome);
	void setEndereco(String endereco);
	void setTelefone(String telefone);
	void setCoordenada(double latitude, double longitude);
	void setCaracteristicas(List<Object> caracteristicas);
	boolean addCaracteristica(Object caracteristica);
	boolean removeCaracteristica(Object caracteristica);	
}

enum Tipo { COMIDA, BEBIDA, SORVETE, RODIZIO, };


interface IProduto{
	String getNome();
	BigDecimal getPreco();
	Tipo getTipo(); 
	
	void setNome(String nome);
	void getPreco(BigDecimal preco);
	void setTipo(Tipo tipo);
}

interface ICaracteristica{
	String getNome();
	
	void setNome(String nome);
}

interface IUsuario{
	String getNome();
	String getToken();//token de sessao
	boolean isLogado();
	
	/**
	 * coordenada atual do usuário, calculada pelo GPS	
	 */
	void getCoordenada(double latitude, double longitude);
	
	void setNome(String nome);
	void setSenha(String senha);
}

interface ISessao{
	IUsuario getUsuario();
	void getModelo();
	IEstabelecimento getEstabelecimentoAtual();
	void setEstabelecimentoAtual(IEstabelecimento estabelecimento);
	boolean isWifi();
}
