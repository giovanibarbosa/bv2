package br.edu.ufcg.dsc.util;

public class PontoTuristico {
	
	private String id;
	private String nome;
	private String latitude;
	private String longitude;
	private String descricao;
	private int imagem;

	public PontoTuristico(String id, String nome, String latitude, String longitude, String descricao, int imagem) {
		this.id = id;
		this.nome = nome;
		this.latitude = latitude;
		this.longitude = longitude;
		this.descricao = descricao;
		this.imagem = imagem;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getDescricao() {
		return descricao;
	}

	public int getImagem() {
		return imagem;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public void setImagem(int imagem) {
		this.imagem = imagem;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

}
