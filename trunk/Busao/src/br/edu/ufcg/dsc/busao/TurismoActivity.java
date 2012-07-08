package br.edu.ufcg.dsc.busao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.edu.ufcg.dsc.R;
import br.edu.ufcg.dsc.httpmodule.HTTPModuleFacade;
import br.edu.ufcg.dsc.util.PontoAdapter;
import br.edu.ufcg.dsc.util.PontoTuristico;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class TurismoActivity extends Activity {
	private ListView list;
	private List<PontoTuristico> pontos;
	private PontoAdapter adapter;
	private HTTPModuleFacade service;;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu_turismo);
		list = (ListView) findViewById(R.id.turismo_list);
		createListView();
	
		service = HTTPModuleFacade.getInstance("1", "0", "0");
	}

	private void createListView() {
		pontos = new ArrayList<PontoTuristico>();
		List<Map<String, String>> pontosTuri = service.getAllTuristicPoint();
		for (Map<String, String> map : pontosTuri) {
			pontos.add(new PontoTuristico(map.get("id"), map.get("nome"), map.get("latitude"), map.get("longitude"), map.get("descricao"), R.drawable.icon));
		}
		adapter = new PontoAdapter(this,pontos);
		list.setAdapter(adapter);
	}
}
