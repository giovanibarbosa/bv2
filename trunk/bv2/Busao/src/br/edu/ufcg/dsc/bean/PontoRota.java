package br.edu.ufcg.dsc.bean;

/*
 * Entidade que engloba uma parte coordenadas geograficas de um percurso de uma rota
 * 
 * Para facilitar o Service, e os tratamentos com os valores retornado de pesquisas no banco,
 * tratamos todos os atributos, execeto a chaves primária e estrangeiras, como String. 
 */

public class PontoRota {

	private int id;
	private int rotaId;
	private String longitude;
	private String latitude;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getRotaId() {
		return rotaId;
	}
	public void setRotaId(int rotaId) {
		this.rotaId = rotaId;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	
}
