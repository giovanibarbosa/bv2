package br.edu.ufcg.dsc.httpmodule;

import java.util.List;
import java.util.Map;

import br.edu.ufcg.dsc.bean.Cache;
import br.edu.ufcg.dsc.bean.PontoTuristico;
import br.edu.ufcg.dsc.config.Configurations;
import br.edu.ufcg.dsc.util.ParserResult;
/**
 * Classe que trata das requisicoes ao banco de dados relacionadas a pontos turisticos.
 * 
 * @author Anderson Almeida
 * @author Daniel Leite
 * @author Giovani Barbosa
 * @author Julio Henrique
 */
public class PointRequest extends HTTPModule{
		
	public static String getDataRoute(String id, String campo) throws Exception {
		if(Cache.isAtualTouristicPoint(id)){
			String result = getResult(Configurations.getInstance().getTouristicPointPath(id));
			Map<String, String> mapeamento = ParserResult.parse(result);
			PontoTuristico pontoTuristico = getObjectTourist(mapeamento);
			Cache.setPontoTuristico(pontoTuristico);
		}
		String valor = "";
		valor = Cache.getPontoTuristico().get(campo);	
		return valor;
	}
	
	private static PontoTuristico getObjectTourist(
			Map<String, String> mapeamento) {
		return new PontoTuristico(mapeamento);
	}

	/**
	 * Retorna os pontos turisticos de uma cidade
	 * @param idCity O id da cidade
	 * @return Os pontos turisticos da cidade
	 * @throws Exception
	 */
	public static List<Map<String, String>> getAllPointsByCity(String idCity) throws Exception {	
		String result = getResult(Configurations.getInstance().getAllTuristicPontsByCity(idCity));
		return ParserResult.parseAll(result);
	}

}