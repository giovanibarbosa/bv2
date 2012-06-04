package br.edu.ufcg.dsc.busao;

import java.util.ArrayList;
import java.util.List;

import br.edu.ufcg.dsc.R;
import br.edu.ufcg.dsc.util.PontoAdapter;
import br.edu.ufcg.dsc.util.PontoTuristico;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class TurismoActivity extends Activity {
	private ListView list;
	private List<PontoTuristico> pontos;
	private PontoAdapter adapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu_turismo);
		list = (ListView) findViewById(R.id.turismo_list);
		createListView();
	
		}

	private void createListView() {
		pontos = new ArrayList<PontoTuristico>();
 		pontos.add(new PontoTuristico("Canal de bodocongo","Canal de bodocongo eh um lugar para lazer e bla bla bla bla ", R.drawable.icon));
		pontos.add(new PontoTuristico("Acude de bodocongo","Acude de bodocongo eh um otimo lugar para se refrescar, muito limpo e 0 por cento de agua de esgoto ", R.drawable.transparencia));
		pontos.add(new PontoTuristico("Parque do Povo","Parque do povo eh um otimo lugar para nao ser assaltado!!!!!", R.drawable.logo_lrcosta));
	 
		adapter = new PontoAdapter(this,pontos);
 
		list.setAdapter(adapter);
	}
}
