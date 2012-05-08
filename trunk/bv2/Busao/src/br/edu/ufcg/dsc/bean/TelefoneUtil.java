package br.edu.ufcg.dsc.bean;

/*
 * Telefones uteis (STTP, Policia, Samu...) de uma cidade
 * 
 * Para facilitar o Service, e os tratamentos com os valores retornado de pesquisas no banco,
 * tratamos todos os atributos, execeto a chaves primária e estrangeiras, como String. 
 */

public class TelefoneUtil {

	private int id;
	private int cidadeId;
	private String telefone;
	private String orgao;
	
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
	public String getTelefone() {
		return telefone;
	}
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	public String getOrgao() {
		return orgao;
	}
	public void setOrgao(String orgao) {
		this.orgao = orgao;
	}
	
}
