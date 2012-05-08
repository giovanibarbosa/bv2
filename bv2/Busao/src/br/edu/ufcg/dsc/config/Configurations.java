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

	private static final String serverUrl = "";

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

/*	Exemplo:
	public String getCity(id) {
		return getServerPath() + "city/getCityById.php?id=";
	}
*/

}
