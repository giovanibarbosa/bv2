package br.edu.ufcg.dsc.httpmodule;

import java.util.Map;

import br.edu.ufcg.dsc.bean.Cache;
import br.edu.ufcg.dsc.bean.Cidade;
import br.edu.ufcg.dsc.config.Configurations;
import br.edu.ufcg.dsc.util.ParserResult;

/**
 * Classe que trata das requisicoes ao banco de dados relacionadas a cidade.
 * 
 * @author Anderson Almeida
 * @author Daniel Leite
 * @author Giovani Barbosa
 * @author Julio Henrique
 */
public class CityRequest extends HTTPModule {

	public static String getDataCity(String id, String campo) throws Exception{
		if(!Cache.isAtualCidade(id)){
			String result = getResult(Configurations.getInstance().getCityData() + id);
			Map<String, String> mapeamento = ParserResult.parse(result);
			Cidade cidade = getObjectCity(mapeamento);
			Cache.setCidade(cidade);
		}
		String valor = "";
		valor = Cache.getCidade().get(campo);	
		return valor;
	} 

	private static Cidade getObjectCity(Map<String, String> mapeamento) {
		return new Cidade(mapeamento);
	}

	/**
	 * Retorna todas as cidades cadastradas no sistema
	 * @return As cidades cadastradas
	 * @throws Exception
	 */
	public static Map<String, String> getAllCity() throws Exception{
		String result = getResult(Configurations.getInstance().getAllCityPath());
		return ParserResult.parseIdName(result);
	}

}
