package br.edu.ufcg.dsc.httpmodule;

public class UserData {
	private String idCity;
	private String latitude;
	private String longitude;
	
	public UserData(String idCity, String latitude, String longitude){
		this.idCity = idCity;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public String getIdCity() {
		return idCity;
	}
	public void setIdCity(String idCity) {
		this.idCity = idCity;
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
