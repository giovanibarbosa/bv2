package br.edu.ufcg.dsc.util;

public class PontoTuristico {
	private String nome;
	private String texto;
	private int imagem;

	public PontoTuristico(String nome, String texto, int imagem) {
		this.nome = nome;
		this.texto = texto;
		this.imagem = imagem;
	}

	public String getTexto() {
		return texto;
	}

	public int getImagem() {
		return imagem;
	}

	public void setTexto(String texto) {
		this.texto = texto;
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
