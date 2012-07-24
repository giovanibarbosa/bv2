package br.edu.ufcg.dsc.httpmodule;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.edu.ufcg.dsc.bean.Cache;
import br.edu.ufcg.dsc.bean.Empresa;
import br.edu.ufcg.dsc.config.Configurations;
import br.edu.ufcg.dsc.util.ParserResult;
/**
 * Classe que trata das requisicoes ao banco de dados relacionadas a empresas.
 * 
 * @author Anderson Almeida
 * @author Daniel Leite
 * @author Giovani Barbosa
 * @author Julio Henrique
 */
public class CompanyRequest extends HTTPModule{


	public static String getDataCompany(String id, String campo) throws Exception {
		if(Cache.isAtualCompany(id)){
			String result = getResult(Configurations.getInstance().getCompanyPath(id));
			Map<String, String> mapeamento = ParserResult.parse(result);
			Empresa empresa = getObjectCompany(mapeamento);
			Cache.setEmpresa(empresa);
		}
		String valor = "";
		valor = Cache.getEmpresa().get(campo);	
		return valor;
	}
	
	private static Empresa getObjectCompany(Map<String, String> mapeamento) {
		return new Empresa(mapeamento);
	}
	
	public static List<String> getAllCompaniesByCity(String id){
		try {
			String result = getResult(Configurations.getInstance().getAllCompaniesByCity(id));
			return ParserResult.parseOnlyName(result);
		} catch (Exception e) {
			return new ArrayList<String>();
		}
	}
	
}
