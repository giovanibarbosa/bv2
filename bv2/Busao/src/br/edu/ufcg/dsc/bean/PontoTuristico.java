package br.edu.ufcg.dsc.bean;

/*
 * Pontos Turisticos de uma cidade 
 * 
 * Para facilitar o Service, e os tratamentos com os valores retornado de pesquisas no banco,
 * tratamos todos os atributos, execeto a chaves primária e estrangeiras, como String. 
 */

public class PontoTuristico {

	private int id;
	private int cidadeId;
	private String nome;
	private String latitude;
	private String longitude;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCidadeId() {
		return cidadeId;
	}
	public void setCidadeId(int cidadeId) {
		this.cidadeId = cidadeId;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	
}
