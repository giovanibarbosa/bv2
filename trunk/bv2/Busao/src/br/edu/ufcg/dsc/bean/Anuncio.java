package br.edu.ufcg.dsc.bean;

/*
 * Entidade que modela um Objeto Anuncio (Qualquer publicidade) no nosso aplicativo.
 * 
 * Para facilitar o Service, e os tratamentos com os valores retornado de pesquisas no banco,
 * tratamos todos os atributos, execeto a chaves primária e estrangeiras, como String. 
 */

public class Anuncio {
	
	private int id;
	private String nome;
	private String link;
	private int cidadeId;
	private String endereco;
	private String imagem;
	private String telefone;
	private String numAcesso;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public int getCidadeId() {
		return cidadeId;
	}
	public void setCidadeId(int cidadeId) {
		this.cidadeId = cidadeId;
	}
	public String getEndereco() {
		return endereco;
	}
	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}
	public String getImagem() {
		return imagem;
	}
	public void setImagem(String imagem) {
		this.imagem = imagem;
	}
	public String getTelefone() {
		return telefone;
	}
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	public String getNumAcesso() {
		return numAcesso;
	}
	public void setNumAcesso(String numAcesso) {
		this.numAcesso = numAcesso;
	}
	

}
