package br.edu.ufcg.dsc.config;

/**
 * Classe que se comunica com o banco de dados, acessando os enderecos
 * correspondentes as consultas desejadas
 * 
 * @author Anderson Almeida
 * @author Daniel Leite
 * @author Giovani Barbosa
 * @author Julio Henrique
 */
public class Configurations {

	private static Configurations instance;

	private static final String serverUrl = "http://busaoapp.com/service/";

	public static Configurations getInstance() {

		if (instance == null) {
			Configurations config = new Configurations();
			return config;
		}

		return instance;
	}

	public String getServerPath() {
		return serverUrl;
	}

	// ======================================================================================================================================
	// CITY
	// ======================================================================================================================================
	/**
	 * Retorna uma cidade, dado o id dela
	 */
	public String getCityData() {
		return getServerPath() + "city/getCityData.php?id=";
	}

	/**
	 * 
	 * @return Todas as cidades do aplicativo
	 */
	public String getAllCityPath() {
		return getServerPath() + "city/getAllCitys.php";
	}

	/**
	 * Retorna as tarifas de onibus de uma cidade
	 * 
	 * @param cityId
	 *            O id da cidade
	 * @return As tarifas de onibus da cidade
	 */
	public String getBusTaxesByCityPath(int cityId) {
		return getServerPath() + "city/getCityData.php?id=" + cityId
				+ "&coluna=valorTarifa";
	}

	// ======================================================================================================================================
	// ROUTE
	// ======================================================================================================================================
	/**
	 * Retorna uma rota, dado seu id
	 * @param longitude 
	 * @param latitude 
	 */
	public String getRoutesPath(String idRota, String latitude, String longitude) {
		return getServerPath() + "route/getRouteData.php?id=" + idRota +"&coordinates="+latitude+","+longitude ;
	}

	public String getRoutesPathSmall(String idRota, String latitude, String longitude) {
		return getServerPath() + "route/getRouteData.php?id=" + idRota +"&coordinates="+latitude+","+longitude+"&atribute=id, nome, url" ;
	}

	/**
	 * Retorna, se houver, uma rota de onibus que passe entre dois pontos no
	 * mapa
	 * 
	 * @param lat1
	 *            A latirude do primeiro ponto
	 * @param long1
	 *            A longitude do primeiro ponto
	 * @param lat2
	 *            A latitude do segundo ponto
	 * @param long2
	 *            A longitude do segundo ponto
	 * @param distancia
	 *            A distancia entre os dois pontos
	 * @return Uma rota que passe entre os dois pontos
	 */
	public String getRoutesBetweenPointsPath(String city, double lat1, double long1,
			double lat2, double long2, int distancia) {
		// alterar city
		String url = "route/getRouteBetweenPoints.php?city="+ city +"&distance="
				+ distancia + "&from=" + lat1 + "," + long1;
		if (!(lat2 == 0 || long2 == 0)) {
			url += "&to=" + lat2 + "," + long2;
		}
		return getServerPath() + url;
	}
	
	public String searchRoute(String idCity) {
		return getServerPath() + "route/searchRouteByName.php?idCity=" + idCity + "&nome=";
	}

	/**
	 * 
	 * @return As coordenadas de um ponto(latitude e longitude) pelo endereco
	 */
	public String getGeoCoderPath() {
		return getServerPath() + "route/geoCoder.php?adr=";
	}

	/**
	 * Retorna as empresas de uma cidade
	 * 
	 * @param id
	 *            O id da cidade
	 * @return Todas as empresas da cidade
	 */
	public String getCompanyPath(String id) {
		return getServerPath() + "company/getCompanyData.php?id=" + id;
	}
	
	public String getAllCompaniesByCity(String id) {
		return getServerPath() + "company/getCompaniesByIdCity.php?id=" + id;
	}

	/**
	 * Retorna todos os telefones uteis de uma cidade
	 * 
	 * @param idCity
	 *            O id da cidade
	 * @return Todos os telefones uteis da cidade
	 */
	public String getPhonesByCity(String idCity) {
		return getServerPath() + "phone/getPhonesByCity.php?id=" + idCity;
	}

	/**
	 * Retorna os pontos turisticos de uma cidade
	 * 
	 * @param id
	 *            O id da cidade
	 * @return Todos os pontos turisticos da cidade
	 */
	public String getTuristicPontsByCity(int id) {
		return getServerPath() + "point/getTuristicPointsByCity.php?id=" + id;
	}

	/**
	 * Retorna a latitude da cidade
	 * 
	 * @param idCity
	 *            O id da cidade
	 * @return A latitude da cidade
	 */
	public String getLatitudeByCityIdPath(int idCity) {
		return getServerPath() + "point/getLatitudeByCityId.php?id=" + idCity;
	}

	/**
	 * Retorna a longitude da cidade
	 * 
	 * @param idCity
	 *            O id da cidade
	 * @return A longitude da cidade
	 */
	public String getLongitudeByCityIdPath(int idCity) {
		return getServerPath() + "point/getLongitudeByCityId.php?id=" + idCity;
	}

	/**
	 * Retorna a latitude de um ponto turistioo
	 * 
	 * @param name
	 *            O nome do ponto turistico
	 * @param idCity
	 *            O id da cidade
	 * @return A latitude do ponto turistico
	 */
	public String getLatitudeByTouristPointPath(String name, int idCity) {
		return getServerPath() + "point/getLatitudeByTouristPoint.php?name="
				+ name + "&id=" + idCity;
	}
	
	public String getCoordinatesByTouristPointPath(int id, int idCity) {
		return getServerPath() + "point/getCoordinatesByTouristPoint.php?id="
				+ id + "&cidadeId=" + idCity;
	}

	/**
	 * Retorna a longitude de um ponto turistioo
	 * 
	 * @param name
	 *            O nome do ponto turistico
	 * @param idCity
	 *            O id da cidade
	 * @return A longitude do ponto turistico
	 */
	public String getLongitudeByTouristPointPath(String name, int idCity) {
		return getServerPath() + "point/getLongitudeByTouristPoint.php?name="
				+ name + "&id=" + idCity;
	}

	public String getCoordinatesByCityIdPath(int idCity) {
		return getServerPath() + "point/getCoordinatesByCityId.php?id=" + idCity;
	}

	public String getAllTouristPointByCityPath(int city) {
		return getServerPath() + "touristpoint/getAllTouristPointByCity.php?city=" + city + "&atribute=id, nome";
	}

	public String getTouristicPointPath(String id) {
		return getServerPath() + "touristpoint/getTouristPointData.php?id=" + id;
	}

	public String getAllTuristicPontsByCity(String idCity) {
		return getServerPath() + "touristpoint/getAllTouristPointByCity.php?city=" + idCity + "&atribute=id, nome, latitude, longitude, descricao";
	}

	public String getRouteHorarioPath(String id, String dia) {
		return getServerPath() + "route/getRouteHorarioData.php?id=" + id + "$filterDay=" + dia;
	}
	
	// ======================================================================================================================================
	// TIME
	// ======================================================================================================================================
	
	public String getRouteTimeDifferenceBetweenBusPath(String id){
		return getServerPath() + "route/getRouteHorarioData.php?id=" + id + "&atribute=difEntreOnibus";
	}
	
	public String getRouteStartTimePath(String id){
		return getServerPath() + "route/getRouteHorarioData.php?id=" + id + "&atribute=horaInicio";
	}
	
	public String getRouteEndTimePath(String id){
		return getServerPath() + "route/getRouteHorarioData.php?id=" + id + "&atribute=horaFim";
	}

	public String getRouteTotalTimePath(String id){
		return getServerPath() + "route/getRouteHorarioData.php?id=" + id + "&atribute=tempoPerTotal";
	}
	
	public String getRouteNumberBusPath(String id){
		return getServerPath() + "route/getRouteHorarioData.php?id=" + id + "&atribute=numOnibus";
	}
	
	public String getRouteDaysPath(String id){
		return getServerPath() + "route/getRouteHorarioData.php?id=" + id + "&atribute=dias";
	}
	
}
