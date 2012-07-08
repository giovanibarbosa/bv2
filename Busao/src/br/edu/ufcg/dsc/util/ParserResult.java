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
		if(str.equals("null")) return new HashMap<String, String>();
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
		if(str.equals("null")) return null;
		Map<String, String> mapaResultado = new HashMap<String, String>();
		List<Map<String, String>> listaTuplas = new ArrayList<Map<String,String>>();
		str = str.substring(2, str.length()-2);
		str = str.replaceAll("\"", "").trim();
		String[] pontos = str.split("id");
		char id;
		for (String s : pontos) {
			if (s.length() > 0){
				id = s.substring(0, 2).toCharArray()[1];
				mapaResultado.put("id", id+"");
				s = s.substring(3, s.length());
						
				if (s.endsWith("{")) s = s.substring(0, s.length() - 3);
				
				String[] att = s.split(",");
				for (String a : att) {
					String[] fields = a.split(":");
					mapaResultado.put(fields[0], fields[1]);
				}
				listaTuplas.add(mapaResultado);
			}
		}
		
		return listaTuplas;
		
//		if(str.equals("null")) return null;
//		List<Map<String, String>> listaTuplas = new ArrayList<Map<String,String>>();
//		str = str.replace("[{\"", "").replace("\"}]", ""); //mudar para ""
//		String[] separaLinhas = str.split("\"},"); //mudar para ""
//		for (String string : separaLinhas) {
//			string = string.replace("{\"", "");//mudar ""
//			Map<String, String> mapaResultado = new HashMap<String, String>();
//			String separado = "\",\"";//Mudar para ""
//			String[] resultado = string.split(separado);
//			for (String string2 : resultado) {
//				String[] valores = string2.split("\":\"");//mudar para ""
//				mapaResultado.put(valores[0], valores[1]);
//			}
//			listaTuplas.add(mapaResultado);
//		}
//		return listaTuplas;
	}
	
	public static List<String> parseOnlyName(String str) {
		if(str.equals("null")) return new ArrayList<String>();
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
		if(str.equals("null")) return new HashMap<String, String>();
		str = str.replace("[{\"", "").replace("\"}]", "");
		Map<String, String> mapaResultado = new HashMap<String, String>();
		String [] separaLinhas = str.split(",");
		for (String s : separaLinhas){
			s = s.replaceAll("\"", "");
			String[] fields = s.split(":");
			mapaResultado.put(fields[0], fields[1]);
		}
		return mapaResultado;
		
//		if(str.equals("null")) return new HashMap<String, String>();
//		str = str.replace("[{\"", "").replace("\"}]", ""); //mudar para ""
//		Map<String, String> mapaResultado = new HashMap<String, String>();
//		String[] separaLinhas = str.split("\"},"); //mudar para ""
//		for (String string : separaLinhas) {
//			string = string.replace("{\"","");//mudar ""
//			String separado = "\",\"";//Mudar para ""
//			String[] resultado = string.split(separado);
//			List<String> mapa = new ArrayList<String>();
//			for (String string2 : resultado) {
//				String[] valores = string2.split("\":\"");//mudar para ""
//				mapa.add(valores[1]);
//			}
//			mapaResultado.put(mapa.get(0), mapa.get(1));
//		}
//		return mapaResultado;
	}
	
	public static void main(String[] args) {
		String str = "[{\"id\":\"15\",\"nome\":\"202-Ramadinha\"},{\"id\":\"16\",\"nome\":\"202\"},{\"id\":\"17\",\"nome\":\"202-A\"}]";
		Map<String, String> map = parseIdName(str);
			for (String string : map.keySet()) {
				System.out.println(string + " - Valor: " + map.get(string));
			}
//		String str = "[{'nome':'Campina Grande'},{'nome':'Jo'}]";
//		List<String> lista = parseOnlyName(str);
//		for (String string : lista) {
//			System.out.println(string);
//		}
//		String str = "[{\"id\":\"1\",\"nome\":\"Parque do Povo\",\"latitude\":\"-7.224269\",\"longitude\":\"-35.887624\",\"descricao\":\"O Parque do Povo, onde e realizado\"},{\"id\":\"2\",\"nome\":\"Acude Velho\",\"latitude\":\"-7.226611\",\"longitude\":\"-35.885167\",\"descricao\":\"null\"}]";
//		List<Map<String, String>> lista = parseAll(str);
//		for (Map string : lista) {
//			System.out.println(string);
//		}
	}
		
	

}
