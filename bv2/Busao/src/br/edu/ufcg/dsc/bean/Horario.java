package br.edu.ufcg.dsc.bean;

/*
 * Entidade que representa os horarios de itinerarios de uma rota
 * 
 * Para facilitar o Service, e os tratamentos com os valores retornado de pesquisas no banco,
 * tratamos todos os atributos, execeto a chaves primária e estrangeiras, como String. 
 */

public class Horario {
	
	private int id;
	private int rotaId;
	private String difEntreOnibus;
	private String difEntreOnibusFDS;
	private String horaInicio;
	private String horaFim;
	private String horaInicioFDS;
	private String horaFimFDS;
	private String tempoPerTotal;
	private String tempoPerTotalFDS;
	
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
	public String getDifEntreOnibus() {
		return difEntreOnibus;
	}
	public void setDifEntreOnibus(String difEntreOnibus) {
		this.difEntreOnibus = difEntreOnibus;
	}
	public String getDifEntreOnibusFDS() {
		return difEntreOnibusFDS;
	}
	public void setDifEntreOnibusFDS(String difEntreOnibusFDS) {
		this.difEntreOnibusFDS = difEntreOnibusFDS;
	}
	public String getHoraInicio() {
		return horaInicio;
	}
	public void setHoraInicio(String horaInicio) {
		this.horaInicio = horaInicio;
	}
	public String getHoraFim() {
		return horaFim;
	}
	public void setHoraFim(String horaFim) {
		this.horaFim = horaFim;
	}
	public String getHoraInicioFDS() {
		return horaInicioFDS;
	}
	public void setHoraInicioFDS(String horaInicioFDS) {
		this.horaInicioFDS = horaInicioFDS;
	}
	public String getHoraFimFDS() {
		return horaFimFDS;
	}
	public void setHoraFimFDS(String horaFimFDS) {
		this.horaFimFDS = horaFimFDS;
	}
	public String getTempoPerTotal() {
		return tempoPerTotal;
	}
	public void setTempoPerTotal(String tempoPerTotal) {
		this.tempoPerTotal = tempoPerTotal;
	}
	public String getTempoPerTotalFDS() {
		return tempoPerTotalFDS;
	}
	public void setTempoPerTotalFDS(String tempoPerTotalFDS) {
		this.tempoPerTotalFDS = tempoPerTotalFDS;
	}

}
