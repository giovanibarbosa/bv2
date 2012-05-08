package br.edu.ufcg.dsc.bean;

/*
 * Entidade que representa o objeto Onibus
 * 
 * Para facilitar o Service, e os tratamentos com os valores retornado de pesquisas no banco,
 * tratamos todos os atributos, execeto a chaves primária e estrangeiras, como String. 
 */

public class Onibus {
	
	private int id;
	private int empresaId;
	private String capacidade;
	private String identificador;
	private String gps;
	private String latitude;
	private String longitude;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getEmpresaId() {
		return empresaId;
	}
	public void setEmpresaId(int empresaId) {
		this.empresaId = empresaId;
	}
	public String getCapacidade() {
		return capacidade;
	}
	public void setCapacidade(String capacidade) {
		this.capacidade = capacidade;
	}
	public String getIdentificador() {
		return identificador;
	}
	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}
	
	public String getGps() {
		return gps;
	}
	public void setGps(String gps) {
		this.gps = gps;
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
