package br.edu.ufcg.dsc.httpmodule;

import java.util.HashMap;
import java.util.Map;
/**
 * Fachada que permite a camada de negocio acessar a camada de banco de dados.
 * 
 * @author Anderson Almeida
 * @author Daniel Leite
 * @author Giovani Barbosa
 * @author Julio Henrique
 */
public class HTTPModuleFacade {
	private UserData user;
	
	/**
	 * Metodo construtor que recebe por parametro o id da cidade que se deseja
	 * fazer todas as requesições no servidor.
	 * @param idCity: id da cidade
	 */
	public HTTPModuleFacade(String idCity, String latitude, String longitude) {
		user = new UserData(idCity, latitude, longitude);
		setUp(idCity);
	}
	
	private boolean setUp(String idCity){
		//deverá checar as atualizações do BD;
		//baixar tudo que deve ser baixado
		return true;
	}

	//CITY
	public String getCityNome(){
		return getDataCity("nome");
	}
	
	public String getCityEstadoId(){
		return getDataCity("estado_id");
	}
	
	public String getCityValorTarifa(){
		return getDataCity("valorTarifa");
	}
	
	public String getCityLatitude(){
		return getDataCity("latitude");
	}
	
	public String getCityLongitude(){
		return getDataCity("longitude");
	}
	
	private String getDataCity(String campo){
		try {
			return CityRequest.getDataCity(getIdCity(), campo);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}
	
	public String getIdCity() {
		return user.getIdCity();
	}

	public Map<String, String> getAllCitys(){
		try {
			return CityRequest.getAllCity();
		} catch (Exception e) {
			return new HashMap<String, String>();
		}
	}
	

	//ROTA
	public String getRouteNome(String id){
		return getRouteData(id, "nome");
	}
	public String getRouteEmpresaId(String id){
		return getRouteData(id, "Empresa_id");
	}
	public String getRouteVia(String id){
		return getRouteData(id, "via");
	}
	public String getRouteCor(String id){
		return getRouteData(id, "cor");
	}
	public String getRouteUrlRota(String id){
		return getRouteData(id, "urlRota");
	}

	public String getRouteDirecao(String id){
		return getRouteData(id, "direcao");
	}
	
	public String getRouteTimeWait(String id){
		return getRouteData(id, "timeWait");
	}
	
	public boolean isRunning(String id){
		String timeWait = getRouteData(id, "timeWait");
		if(!timeWait.equals("-1")){
			return true;
		}else{
			return false;
		}
	}

	public Map<String, String> searchRoute(String nome){
		Map<String, String> mapa;
		try {
			mapa = RouteRequest.searchRoute(getIdCity(), nome);
		} catch (Exception e) {
			mapa = new HashMap<String, String>();
		}
		return mapa;
	}
	
	private String getRouteData(String id, String campo){
		try {
			String latitude = user.getLatitude();
			String longitude = user.getLongitude();
			return RouteRequest.getDataRoute(id, campo,latitude, longitude);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}


	/**
	 * Retorna as coordenadas de um ponto pelo endereco
	 * @param address Um endereco real
	 * @throws Exception
	 */
	public static void getCoordenada(String adr) throws Exception{
		RouteRequest.getCoordenada(adr);
	}


	
}
