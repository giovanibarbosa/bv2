package br.edu.ufcg.dsc.busao;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.edu.ufcg.dsc.R;
import br.edu.ufcg.dsc.httpmodule.HTTPModuleFacade;
import br.edu.ufcg.dsc.util.CustomBuilder;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DigitalClock;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

public class ResultadoActivity extends Activity {
	private HTTPModuleFacade service;
	private String campoBusca = "";
	private Double lat1, long1, lat2, long2;
	private Map<String, String> resultado;
	private Spinner spinnerRotas;
	private ArrayAdapter<CharSequence> featuresAdapter;
	private List<CharSequence> featuresList;
	private String nome;
	private View bodyResult;
	public String atualURLMap = "";
	public String idRota = "";
	private ImageView iconeFavoritos;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loading_result);
		Bundle b = getIntent().getExtras();
		campoBusca = b.getString("paramBusca");
		service = HTTPModuleFacade.getInstance();
		if(campoBusca == null){
			lat1 = b.getDouble("lat1");
			long1 = b.getDouble("long1");
			lat2 = b.getDouble("lat2");
			long2 = b.getDouble("long2");
			resultado = service.searchRouteBetweenTwoPoints(lat1, long1, lat2, long2);
		}else{
			resultado = service.searchRoute(campoBusca);
		}
		Log.i("Campo", campoBusca);
		
		Log.i("Tempo", service.getCityValorTarifa());
		// problems
		
		Log.i("Resultadooo", resultado.toString());
					
		if(resultado.size() == 0){
			Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.result_sem_resultado), Toast.LENGTH_SHORT);
			toast.show();
			finish();
		}
		showDialog(1);
		
		iconeFavoritos = (ImageView) bodyResult.findViewById(R.id.image_favoritos);
		iconeFavoritos.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				switch (R.drawable.estrela_cinza) { //mudar isso para pegar a atual imagem carregada
				case R.drawable.estrela_cinza:
					iconeFavoritos.setImageResource(R.drawable.estrela_amarela);
					//insere dados
					break;
				case R.drawable.estrela_amarela:
					iconeFavoritos.setImageResource(R.drawable.estrela_cinza);
					//remove dados
					break;

				default:
					break;
				}
				
				
			}
		});
	 
//		DigitalClock relogio = (DigitalClock) bodyResult.findViewById(R.id.digital_clock);
//		relogio.set
		 
	}
	
	protected void onPostExecute(Object result) {
	    if (!isFinishing()) {
	        showDialog(1);
	    }
	}
	
	@Override
	protected Dialog onCreateDialog( int id )
	{
		Dialog dialog = null;
		ContextThemeWrapper ctw = new ContextThemeWrapper( this, R.style.MyTheme );
		CustomBuilder builder = null;
		switch (id) {		
		case 1:
			builder = new CustomBuilder( ctw, R.layout.result );
			builder.setTitle(getString(R.string.result_result));
			builder.setIcon(null);
			builder.setCancelable( false );	
			builder.setNegativeButton(getString(R.string.result_voltar), new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick( DialogInterface dialog, int which )
							{
								dialog.dismiss();
								finish();
							}
						} );
			builder.setPositiveButton(getString(R.string.result_ok), new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick( DialogInterface dialog, int which )
				{
					dialog.dismiss();
					finish();
				}
			} );
			builder.setNeutralButton(getString(R.string.result_mapa), new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick( DialogInterface dialog, int which )
				{
					String urlRota = atualURLMap;
					Log.i("url", urlRota);
					
					if(!urlRota.equals("")){
						Uri uri1 = Uri.parse(urlRota);					
						Intent mapIntent = new Intent(Intent.ACTION_VIEW, uri1);
						mapIntent.setData(uri1);
						startActivity(Intent.createChooser(mapIntent, idRota));				
					}else{
						Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.result_sem_mapa) , Toast.LENGTH_SHORT);
						toast.show();
					}
				}
			} );

			break;

		default:
			break;
		}
		dialog = builder.create();
		if ( dialog == null ){
			dialog = super.onCreateDialog( id );
		}
		
		bodyResult = builder.getTemplateBody();
		//Identifica o Spinner no layout
		spinnerRotas = (Spinner) bodyResult.findViewById(R.id.spinner_rotas);
		//Cria um ArrayAdapter usando um padrão de layout da classe R do android, passando o ArrayList nomes
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
				new ArrayList(resultado.values()));
		ArrayAdapter<String> spinnerArrayAdapter = arrayAdapter;
		spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerRotas.setAdapter(spinnerArrayAdapter);
 
		//Método do Spinner para capturar o item selecionado
		spinnerRotas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
 
			@Override
			public void onItemSelected(AdapterView<?> parent, View v, int posicao, long id) {
				//pega nome pela posição
				nome = parent.getItemAtPosition(posicao).toString();
				//imprime um Toast na tela com o nome que foi selecionado
				//Toast.makeText(ResultadoActivity.this, "Nome Selecionado: " + nome, Toast.LENGTH_LONG).show();
				String idRota = getKeyByValue(resultado, nome);
				atualizaDados(idRota);
			}
 
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
 
			}
		});

		return dialog;
	}
	
	private String getKeyByValue(Map<String, String> mapa, String value){
		for (String key : mapa.keySet()) {
			if(mapa.get(key).equals(value)){
				return key;
			}
		}
		return "";
	}
	
	private void atualizaDados(String idRota){
		this.idRota = idRota;
		TextView textMinutos = (TextView) bodyResult.findViewById(R.id.text_tempo_restante);
		textMinutos.setText(service.getRouteTimeWait(idRota));
		this.atualURLMap = service.getRouteUrlRota(idRota);
		Log.i("atualizandoDados", idRota);
		Log.i("URLRota", atualURLMap);
	}

}
