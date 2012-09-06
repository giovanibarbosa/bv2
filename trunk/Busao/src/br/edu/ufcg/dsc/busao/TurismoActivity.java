package br.edu.ufcg.dsc.busao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.edu.ufcg.dsc.R;
import br.edu.ufcg.dsc.httpmodule.HTTPModuleFacade;
import br.edu.ufcg.dsc.util.PontoAdapter;
import br.edu.ufcg.dsc.util.PontoTuristico;
import android.app.Activity;
import android.content.pm.ActivityInfo;
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
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		list = (ListView) findViewById(R.id.turismo_list);
		createListView();
	
		service = HTTPModuleFacade.getInstance("1", "0", "0");
	}

	@Override
	protected void onResume(){
		super.onResume();
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
	
	private void createListView() {
		pontos = new ArrayList<PontoTuristico>();
		int imagem = 0;
		List<Map<String, String>> pontosTuri = service.getAllTuristicPoint();
		for (Map<String, String> map : pontosTuri) {
			
			if (map.get("nome").equalsIgnoreCase("Parque do Povo")){
				imagem = R.drawable.pt_cg_pp;
			} else if (map.get("nome").equalsIgnoreCase("Tropeiros da Borborema")){
				imagem = R.drawable.pt_cg_tropeiros;
			} else if (map.get("nome").equalsIgnoreCase("Acude Velho")){
				imagem = R.drawable.pt_cg_acudevelho;
			} else if (map.get("nome").equalsIgnoreCase("Jackson do Pandeiro")){
				imagem = R.drawable.pt_cg_jackson;
			} else if (map.get("nome").equalsIgnoreCase("Centro Historico")){
				imagem = R.drawable.pt_jp_centrohistorico;
			} else if (map.get("nome").equalsIgnoreCase("Farol do Cabo Branco")){
				imagem = R.drawable.pt_jp_farol;
			} else if (map.get("nome").equalsIgnoreCase("Estacao Cabo Branco")){
				imagem = R.drawable.pt_jp_estacaocb;
			} else if (map.get("nome").equalsIgnoreCase("Lagoa")){
				imagem = R.drawable.pt_jp_lagoa;
			} else if (map.get("nome").equalsIgnoreCase("Bica")){
				imagem = R.drawable.pt_jp_bica;
			} else if (map.get("nome").equalsIgnoreCase("Fortaleza de Santa Catarina")){
				imagem = R.drawable.pt_jp_santacatarina;
			} else if (map.get("nome").equalsIgnoreCase("Por do Sol praia do Jacare")){
				imagem = R.drawable.pt_jp_porsol;
			} else if (map.get("nome").equalsIgnoreCase("Praia de Coqueirinho")){
				imagem = R.drawable.pt_jp_coqueirinho;
			} else { //imagem default
				imagem = R.drawable.pt_default;
			}
			
			pontos.add(new PontoTuristico(map.get("id"), map.get("nome"), map.get("latitude"), map.get("longitude"), map.get("descricao"), imagem));
		}
		adapter = new PontoAdapter(this,pontos);
		list.setAdapter(adapter);
	}
}
