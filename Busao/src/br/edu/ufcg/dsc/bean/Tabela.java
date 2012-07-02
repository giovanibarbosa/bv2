package br.edu.ufcg.dsc.bean;

import java.util.List;
import java.util.Map;

public class Tabela {
	private Map<String, String> campos;
	
	public Tabela(Map<String, String> valores) {
		this.campos = valores;
	}
	
	public List<String> getCampos(){
		return (List<String>) campos.keySet();
	}
	
	public List<String> getValores(){
		return (List<String>)campos.values();
	}
	
	public String get(String campo) throws NotKeyException{
		if(campos.keySet().contains(campo)){
			return campos.get(campo);
		}
		throw new NotKeyException();
	}

}
