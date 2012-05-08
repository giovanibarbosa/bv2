package br.edu.ufcg.dsc.bean;

/*
 * Entidade principal do aplicativo. Representa uma Rota no sistema de transportes coletivos
 * 
 * Para facilitar o Service, e os tratamentos com os valores retornado de pesquisas no banco,
 * tratamos todos os atributos, execeto a chaves primária e estrangeiras, como String. 
 */

public class Rota {
	
	private int id;
	private int empresaId;
	private String nome;
	private String via;
	private String cor;
	private String url;
	private String numOnibus;
	private String numOnibusFDS;
	private String latInicial;
	private String lgnInicial;
	private String direcao;
	private String FDS;
	
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
	public String getRotaNome() {
		return nome;
	}
	public void setRotaNome(String rotaNome) {
		this.nome = rotaNome;
	}
	public String getVia() {
		return via;
	}
	public void setVia(String via) {
		this.via = via;
	}
	public String getCor() {
		return cor;
	}
	public void setCor(String cor) {
		this.cor = cor;
	}
	public String getUrlRota() {
		return url;
	}
	public void setUrlRota(String urlRota) {
		this.url = urlRota;
	}
	public String getNumOnibus() {
		return numOnibus;
	}
	public void setNumOnibus(String numOnibus) {
		this.numOnibus = numOnibus;
	}
	public String getNumOnibusFDS() {
		return numOnibusFDS;
	}
	public void setNumOnibusFDS(String numOnibusFDS) {
		this.numOnibusFDS = numOnibusFDS;
	}
	public String getLatInicial() {
		return latInicial;
	}
	public void setLatInicial(String latInicial) {
		this.latInicial = latInicial;
	}
	public String getLgnInicial() {
		return lgnInicial;
	}
	public void setLgnInicial(String lgnInicial) {
		this.lgnInicial = lgnInicial;
	}
	public String getDirecao() {
		return direcao;
	}
	public void setDirecao(String direcao) {
		this.direcao = direcao;
	}
	public String getFDS() {
		return FDS;
	}
	public void setFDS(String fDS) {
		FDS = fDS;
	}

}
