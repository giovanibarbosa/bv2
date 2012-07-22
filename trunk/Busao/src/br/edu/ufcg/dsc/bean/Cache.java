package br.edu.ufcg.dsc.bean;

import java.util.Iterator;
import java.util.List;

public class Cache {
	private static Cidade cidade;
	private static Rota rota;
	private static TelefoneUtil telefone;
	private static Empresa empresa;
	private static PontoTuristico pontoTuristico;
	private static List<Anuncio> anuncios;
	private static Time horario;
	
	public static Time getHorario() {
		return horario;
	}

	public static void setHorario(Time horario) {
		Cache.horario = horario;
	}

	public static List<Anuncio> getAnuncios() {
		return anuncios;
	}

	public static void setAnuncios(List<Anuncio> anuncios) {
		Cache.anuncios = anuncios;
	}

	public static TelefoneUtil getTelefone() {
		return telefone;
	}

	public static void setTelefone(TelefoneUtil telefone) {
		Cache.telefone = telefone;
	}

	public static Empresa getEmpresa() {
		return empresa;
	}

	public static void setEmpresa(Empresa empresa) {
		Cache.empresa = empresa;
	}


	public static Cidade getCidade() {
		return cidade;
	}

	public static void setCidade(Cidade cidade) {
		Cache.cidade = cidade;
	}
	
	public static boolean isAtualCidade(String id){
		try {
			return cidade != null ? cidade.get("id").equals(id) : false;
		} catch (NotKeyException e) {
			return false;
		}
	}
	
	public static PontoTuristico getPontoTuristico() {
		return pontoTuristico;
	}

	public static void setPontoTuristico(PontoTuristico pontoTuristico) {
		Cache.pontoTuristico = pontoTuristico;
	}

	public static boolean isAtualRota(String id){
		try {
			return rota != null ? rota.get("id").equals(id) : false;
		} catch (NotKeyException e) {
			// TODO Auto-generated catch block
			return false;
		}
	}

	public static Rota getRota() {
		return rota;
	}

	public static void setRota(Rota rota) {
		Cache.rota = rota;
	}

	public static boolean isAtualPhone(String id) {
		try {
			return telefone != null ? telefone.get("id").equals(id): false;
		} catch (NotKeyException e) {
			// TODO Auto-generated catch block
			return false;
		}
	}

	public static boolean isAtualCompany(String id) {
		try {
			return empresa != null ? empresa.get("id").equals(id) : false;
		} catch (NotKeyException e) {
			// TODO Auto-generated catch block
			return false;
		}
	}
	
	public static boolean isAtualTouristicPoint(String id) {
		try {
			return pontoTuristico != null ? pontoTuristico.get("id").equals(id) : false;
		} catch (NotKeyException e) {
			// TODO Auto-generated catch block
			return false;
		}
	}
	
	public static boolean isAtualHorario(String id){
		try {
			return horario != null ? horario.get("rotaId").equals(id) : false;
		} catch (NotKeyException e) {
			return false;
		}
	}

	public static Anuncio getAnuncio(String id) throws Exception {
		Iterator<Anuncio> itAnuncio = anuncios.iterator();
		while(itAnuncio.hasNext()){
			Anuncio anuncio = itAnuncio.next();
			if(anuncio.get("id").equals(id)){
				return anuncio;
			}
		}
		throw new Exception("Anuncio nao existe");
	}
	
}