package br.edu.ufcg.dsc.httpmodule;

import br.edu.ufcg.dsc.bean.Anuncio;
import br.edu.ufcg.dsc.bean.Cache;

public class AnuncioRequest extends HTTPModule{
	
	public static boolean setUp(){
		if(isOldVersion()){
			//requisição de todos os novos anuncios
			//limpa anuncios atuais e armazena no BD local os novos
			//atualiza Cache com os anuncios
		}
		return true;
	}
	
	public static String getDataAnuncio(String id, String campo) throws Exception{
		Anuncio anuncio = Cache.getAnuncio(id);
		return anuncio.get(campo);
	}
	
	private static boolean isOldVersion(){
		//verifica se a tabela atualização é a antiga
		//se for, retorna true
		return true;
	}
	

}
