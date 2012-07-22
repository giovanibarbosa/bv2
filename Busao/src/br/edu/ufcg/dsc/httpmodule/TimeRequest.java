package br.edu.ufcg.dsc.httpmodule;

import java.util.Calendar;
import java.util.Map;

import br.edu.ufcg.dsc.bean.Cache;
import br.edu.ufcg.dsc.bean.Time;
import br.edu.ufcg.dsc.config.Configurations;
import br.edu.ufcg.dsc.util.ParserResult;

public class TimeRequest extends HTTPModule {
	
	public static String getDataRouteTime(String rotaid, String campo) throws Exception {
		if(!Cache.isAtualHorario(rotaid)){
			Calendar calendar = Calendar.getInstance();
			int day = calendar.get(Calendar.DAY_OF_WEEK); // If current day is Sunday, day=1
			
			String result = getResult(Configurations.getInstance().getRouteHorarioPath(rotaid, day+""));
			Map<String, String> mapeamento = ParserResult.parse(result);
			Time horario = getObjectTime(mapeamento);
			Cache.setHorario(horario);
		}
		String valor = "";
		valor = Cache.getHorario().get(campo);	
		return valor;
	}
	
	private static Time getObjectTime(Map<String, String> mapeamento) {
		return new Time(mapeamento);
	}

}