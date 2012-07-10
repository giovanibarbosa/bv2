package br.edu.ufcg.dsc.httpmodule;


import java.util.List;
import java.util.Map;


import br.edu.ufcg.dsc.bean.Cache;
import br.edu.ufcg.dsc.bean.Rota;
import br.edu.ufcg.dsc.config.Configurations;
import br.edu.ufcg.dsc.util.ParserResult;
/**
 * Classe que trata das requisicoes ao banco de dados relacionadas a rotas.
 * 
 * @author Anderson Almeida
 * @author Daniel Leite
 * @author Giovani Barbosa
 * @author Julio Henrique
 */
public class RouteRequest extends HTTPModule {

	
	public static String getDataRoute(String id, String campo, String latitude, String longitude) throws Exception {
		if(!Cache.isAtualRota(id)){
			String result = getResult(Configurations.getInstance().getRoutesPathSmall(id, latitude, longitude));
			Map<String, String> mapeamento = ParserResult.parse(result);
			Rota rota = getObjectRota(mapeamento);
			Cache.setRota(rota);
		}
		String valor = "";
		valor = Cache.getRota().get(campo);	
		return valor;
	}
	
	public static Map<String, String> searchRoute(String idCidade, String nome) throws Exception{
		String result = getResult(Configurations.getInstance().searchRoute(idCidade) + nome);
		return ParserResult.parseIdName(result);
	}
	
	private static Rota getObjectRota(Map<String, String> mapeamento) {
		return new Rota(mapeamento);
	}



	/**
	 * Retorna a(s) rota(s) que passam entre dois pontos selecionados
	 * @param lat1 A latitude do primeiro ponto
	 * @param long1 A longitude do primeiro ponto
	 * @param lat2 A latitude do segundo ponto
	 * @param long2 A longitude do segundo ponto
	 * @param distance A distancia entre os dois pontos
	 * @return A rota que passa pelos dois pontos
	 * @throws Exception 
	 */
	public static List<String> getRoutesBetweenPoints(String city, double lat1, double long1,
			double lat2, double long2, int distance) throws Exception {
		String result = getResult(Configurations.getInstance()
				.getRoutesBetweenPointsPath(city, lat1, long1, lat2, long2, distance));
		return (List<String>) ParserResult.parseIdName(result).values();
	}


	/**
	 * Retorna as coordenadas de um ponto pelo endereco
	 * @param address Um endereco real
	 * @throws Exception
	 */
	public static String getCoordenada(String address) throws Exception {
		String result = getResult(Configurations.getInstance().getGeoCoderPath() + address);
		return result;
	}
	



}
