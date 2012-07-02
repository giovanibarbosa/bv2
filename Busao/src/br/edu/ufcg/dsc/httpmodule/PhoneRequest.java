package br.edu.ufcg.dsc.httpmodule;

import java.util.List;
import java.util.Map;

import br.edu.ufcg.dsc.bean.Cache;
import br.edu.ufcg.dsc.bean.TelefoneUtil;
import br.edu.ufcg.dsc.config.Configurations;
import br.edu.ufcg.dsc.util.ParserResult;
/**
 * Classe que trata das requisicoes ao banco de dados relacionadas a telefones uteis.
 * 
 * @author Anderson Almeida
 * @author Daniel Leite
 * @author Giovani Barbosa
 * @author Julio Henrique
 */
public class PhoneRequest extends HTTPModule{
	
	
	public static String getDataPhone(String id, String campo) throws Exception{
		if(Cache.isAtualPhone(id)){
			String result = getResult(Configurations.getInstance().getPhonesByCity(id));
			Map<String, String> mapeamento = ParserResult.parse(result);
			TelefoneUtil telefone = getObjectTelefone(mapeamento);
			Cache.setTelefone(telefone);
		}
		String valor = "";
		valor = Cache.getTelefone().get(campo);	
		return valor;
	}
	
	
	
	private static TelefoneUtil getObjectTelefone(Map<String, String> mapeamento) {
		return new TelefoneUtil(mapeamento);
	}



	/**
	 * Retorna os telefones uteis de uma cidade
	 * @param idCity O id da cidade
	 * @return Os telefones uteis da cidade
	 * @throws Exception
	 */
	public static List<Map<String, String>> getPhonesByCity(String idCity) throws Exception {
		String result = getResult(Configurations.getInstance().getPhonesByCity(idCity));
		
		return ParserResult.parseAll(result);
	}


}
