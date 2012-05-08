package br.edu.ufcg.dsc.bean;

/*
 * Entidade que modela um Empresa de Onibus
 * 
 * Para facilitar o Service, e os tratamentos com os valores retornado de pesquisas no banco,
 * tratamos todos os atributos, execeto a chaves primária e estrangeiras, como String. 
 */

public class Empresa {
	
	private int id;
	private int cidadeId;
	private String nome;
	private String anoFundacao;

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
	public String getAnoFundacao() {
		return anoFundacao;
	}
	public void setAnoFundacao(String anoFundacao) {
		this.anoFundacao = anoFundacao;
	}

}
