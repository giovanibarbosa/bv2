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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

public class ResultadoActivity extends Activity {
	private HTTPModuleFacade service;
	private String campoBusca = "";
	private Map<String, String> resultado;
	private Spinner spinnerRotas;
	private ArrayAdapter<CharSequence> featuresAdapter;
	private List<CharSequence> featuresList;
	private String nome;
	private View bodyResult;
	public String atualURLMap = "";
	public String idRota = "";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loading_result);
		 new Thread(new Runnable() {
			 public void run() {
					Bundle b = getIntent().getExtras();
					campoBusca = b.getString("paramBusca");
					Log.i("Campo", campoBusca);
					service = HTTPModuleFacade.getInstance();
					Log.i("Tempo", service.getCityValorTarifa());
					//problems
					//resultado = service.searchRoute(campoBusca);
					//Log.i("Resultado", resultado.toString());

					//showDialog aqui
					
				}
		    }).start();
		resultado = new HashMap<String, String>();
		resultado.put("15", "202-Ramadinha");
		resultado.put("16", "202");
		resultado.put("17", "202-A");

		showDialog(1);
	 
		 
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
						Toast toast = Toast.makeText(getApplicationContext(), "Rota Indisponivel", Toast.LENGTH_SHORT);
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
		//Cria um ArrayAdapter usando um padr�o de layout da classe R do android, passando o ArrayList nomes
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
				new ArrayList(resultado.values()));
		ArrayAdapter<String> spinnerArrayAdapter = arrayAdapter;
		spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerRotas.setAdapter(spinnerArrayAdapter);
 
		//M�todo do Spinner para capturar o item selecionado
		spinnerRotas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
 
			@Override
			public void onItemSelected(AdapterView<?> parent, View v, int posicao, long id) {
				//pega nome pela posi��o
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
	}

}