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

	public UserData getUser(){
		return user;
	}
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
	
//	public String getAtualEndrereco(){
//		String latitude = user.getLatitude();
//		String longitude = user.getLongitude();
//		try {
//			if(latitude.equals("0") && longitude.equals("0"))
//				return "desconhecido";
//			return CityRequest.getEndereco(user.getLatitude(), user.getLongitude());
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			return "desconhecido";
//		}
//	}
	
	public void updateUserAddress(){
		try {
			String addr = CityRequest.getEndereco(user.getLatitude(), user.getLongitude());
			getUser().setAddress(addr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String getCityNome() throws Exception{
		return getDataCity("nome");
	}
	
	public String getCityEstadoId() throws Exception{
		return getDataCity("estado_id");
	}
	
	public String getCityValorTarifa() throws Exception{
		return getDataCity("valorTarifa");
	}
	
	public String getCityLatitude() throws Exception{
		return getDataCity("latitude");
	}
	
	public String getCityLongitude() throws Exception{
		return getDataCity("longitude");
	}
	
	private String getDataCity(String campo) throws Exception{
		return CityRequest.getDataCity(getIdCity(), campo);
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
	public String getRouteNome(String id) throws Exception{
		return getRouteData(id, "nome");
	}
	public String getRouteEmpresaId(String id) throws Exception{
		return getRouteData(id, "Empresa_id");
	}
	public String getRouteVia(String id) throws Exception{
		return getRouteData(id, "via");
	}
	public String getRouteCor(String id) throws Exception{
		return getRouteData(id, "cor");
	}
	public String getRouteUrlRota(String id) throws Exception{
		return getRouteData(id, "url");
	}

	public String getRouteDirecao(String id) throws Exception{
		return getRouteData(id, "direcao");
	}
	
	public String getRouteTimeWait(String id) throws Exception{
		return getRouteData(id, "timeWait");
	}
	
	public boolean isRunning(String id) throws Exception{
		String timeWait = getRouteData(id, "timeWait");
		if(!timeWait.equals("-1")){
			return true;
		}else{
			return false;
		}
	}

	public Map<String, String> searchRoute(String nome) throws Exception {
		Map<String, String> mapa;
		mapa = RouteRequest.searchRoute(getIdCity(), nome);
		return mapa;
	}
	
	public Map<String, String> searchRouteBetweenTwoPoints(double lat1, double long1, double lat2, double long2) throws Exception{
		Map<String, String> mapa;
		mapa = RouteRequest.getRoutesBetweenPoints(getIdCity(), lat1, long1, lat2, long2, 300);
		return mapa;
	}
	
	private String getRouteData(String id, String campo) throws Exception{
			String latitude = user.getLatitude();
			String longitude = user.getLongitude();
			return RouteRequest.getDataRoute(id, campo,latitude, longitude);
	}


	/**
	 * Retorna as coordenadas de um ponto pelo endereco
	 * @param address Um endereco real
	 * @throws Exception
	 */
	public static void getCoordenada(String adr) throws Exception{
		RouteRequest.getCoordenada(adr);
	}
	
	// ======================================================================================================================================
	// TIME
	// ======================================================================================================================================
	
	public String getRouteTimeDifferenceBetweenBusPath(String rotaid) throws Exception{
		return getDataTime(rotaid, "difEntreOnibus");
	}
	
	public String getRouteStartTimePath(String rotaid) throws Exception{
		return getDataTime(rotaid, "horaInicio");
	}
	
	public String getRouteEndTimePath(String rotaid) throws Exception{
		return getDataTime(rotaid, "horaFim");
	}

	public String getRouteTotalTimePath(String rotaid) throws Exception{
		return getDataTime(rotaid, "tempoPerTotal");
	}
	
	public String getRouteNumberBusPath(String rotaid) throws Exception{
		return getDataTime(rotaid,"numOnibus");
	}
	
	public String getRouteDaysPath(String rotaid) throws Exception{
		return getDataTime(rotaid, "dias");
	}
	
	private String getDataTime(String rotaid, String campo) throws Exception{
		return TimeRequest.getDataRouteTime(rotaid, campo);
	}

}
