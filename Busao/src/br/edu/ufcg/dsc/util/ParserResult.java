package br.edu.ufcg.dsc.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ParserResult {
	
	

	/**
	 * Trata a string proveniente do banco de dados
	 * @param str A string vinda do bd
	 * @return Todas as cidades em uma so string
	 */
	public static Map<String, String> parse(String str) {
		//elimina sujeira
		Map<String, String> mapaResultado = new HashMap<String, String>();
		str = str.replace("[{\"", "").replace("\"}]", ""); //mudar para ""
		String separado = "\",\"";//Mudar para ""
		String[] resultado = str.split(separado);
		for (String string : resultado) {
			String[] valores = string.split("\":\"");//mudar para ""
			mapaResultado.put(valores[0], valores[1]);
		}		
		return mapaResultado;
	}
	
	public static List<Map<String, String>> parseAll(String str) {
		//elimina sujeira
		List<Map<String, String>> listaTuplas = new ArrayList<Map<String,String>>();
		str = str.replace("[{\"", "").replace("\"}]", ""); //mudar para ""
		String[] separaLinhas = str.split("\"},"); //mudar para ""
		for (String string : separaLinhas) {
			string = string.replace("{\"", "");//mudar ""
			Map<String, String> mapaResultado = new HashMap<String, String>();
			String separado = "\",\"";//Mudar para ""
			String[] resultado = string.split(separado);
			for (String string2 : resultado) {
				String[] valores = string2.split("\":\"");//mudar para ""
				mapaResultado.put(valores[0], valores[1]);
			}
			listaTuplas.add(mapaResultado);
		}
		return listaTuplas;
	}
	
	public static List<String> parseOnlyName(String str) {
		//[{"nome":"Campina Grande"},{"nome":"Jo"}]
		//elimina sujeira
		List<String> listaNomes = new ArrayList<String>();
		str = str.replace("[{\"", "").replace("\"}]", ""); //mudar para ""
		String[] separaLinhas = str.split("\"},"); //mudar para ""
		for (String string : separaLinhas) {
			string = string.replace("{\"", "");//mudar ""
			String separado = "\",'";//Mudar para ""
			String[] resultado = string.split(separado);
			for (String string2 : resultado) {
				String[] valores = string2.split("\":\"");//mudar para ""
				listaNomes.add(valores[1]);
			}
		}
		return listaNomes;
	}
	
	public static Map<String, String> parseIdName(String str) {
		//elimina sujeira
		//"[{'id':'1','nome':'Campina Grande'},{'id':'2','nome':'Jo'}]";
		str = str.replace("[{\"", "").replace("\"}]", ""); //mudar para ""
		Map<String, String> mapaResultado = new HashMap<String, String>();
		String[] separaLinhas = str.split("\"},"); //mudar para ""
		for (String string : separaLinhas) {
			string = string.replace("{\"","");//mudar ""
			String separado = "\",\"";//Mudar para ""
			String[] resultado = string.split(separado);
			List<String> mapa = new ArrayList<String>();
			for (String string2 : resultado) {
				String[] valores = string2.split("\":\"");//mudar para ""
				mapa.add(valores[1]);
			}
			mapaResultado.put(mapa.get(0), mapa.get(1));
		}
		return mapaResultado;
	}
	
	public static void main(String[] args) {
//		String str = "[{'id':'1','nome':'Campina Grande'},{'id':'2','nome':'Jo'}]";
//		Map<String, String> map = parseIdName(str);
//			for (String string : map.keySet()) {
//				System.out.println(string + " - Valor: " + map.get(string));
//			}
//		String str = "[{'nome':'Campina Grande'},{'nome':'Jo'}]";
//		List<String> lista = parseOnlyName(str);
//		for (String string : lista) {
//			System.out.println(string);
//		}
		}
		
	

}
