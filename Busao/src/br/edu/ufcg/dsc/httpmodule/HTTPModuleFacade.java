package br.edu.ufcg.dsc.httpmodule;

import java.util.HashMap;
import java.util.List;
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
	private static HTTPModuleFacade instance = null;

	
	public static HTTPModuleFacade getInstance(String idCity, String latitude, String longitude){
		if(instance == null)
			instance = new HTTPModuleFacade(idCity, latitude, longitude);
		return instance;
	}
	
	public static HTTPModuleFacade getInstance(){
		return instance;
	}
	
	/**
	 * Metodo construtor que recebe por parametro o id da cidade que se deseja
	 * fazer todas as requesições no servidor.
	 * @param idCity: id da cidade
	 */
	private HTTPModuleFacade(String idCity, String latitude, String longitude) {
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
	
	//PONTO TURISTICO
	public List<Map<String,String>> getAllTuristicPoint(){
		try {
			return PointRequest.getAllPointsByCity(getIdCity());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
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
		return getRouteData(id, "url");
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
			e.printStackTrace();
		}
		return mapa;
	}
	
	public Map<String, String> searchRouteBetweenTwoPoints(double lat1, double long1, double lat2, double long2){
		Map<String, String> mapa;
		try {
			mapa = RouteRequest.getRoutesBetweenPoints(getIdCity(), lat1, long1, lat2, long2, 300);
		} catch (Exception e) {
			mapa = new HashMap<String, String>();
			e.printStackTrace();
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
