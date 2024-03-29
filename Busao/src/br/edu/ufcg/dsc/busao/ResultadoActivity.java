package br.edu.ufcg.dsc.busao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.edu.ufcg.dsc.R;
import br.edu.ufcg.dsc.dao.Rota;
import br.edu.ufcg.dsc.dao.RotaDataSource;
import br.edu.ufcg.dsc.exception.NoReturnDataException;
import br.edu.ufcg.dsc.httpmodule.HTTPModuleFacade;
import br.edu.ufcg.dsc.util.CustomBuilder;
import br.edu.ufcg.dsc.util.RouteListView;
import br.edu.ufcg.dsc.util.ThreadedClass;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

public class ResultadoActivity extends Activity {
	private HTTPModuleFacade service = HTTPModuleFacade.getInstance();;
	private String campoBusca = "";
	private Double lat1, long1, lat2, long2;
	private Map<String, String> resultado;
	private Spinner spinnerRotas;
	private String nome;
	private View bodyResult;
	public String atualURLMap = "";
	public String idRota = "";
	private ImageView iconeFavoritos;
	private RotaDataSource datasource;
	private View viewInflateFavoritos;
	private ArrayList<RouteListView> rotas;
	private ListView listView;
	ThreadedClass m_t = null; 

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loading_result);
		m_t = new ThreadedClass(myHandler); 
        m_t.Start(); 
	}
	
	@Override
	protected void onResume() {
		Log.i("voltou", "voltou");
		super.onResume();
	}
	
	private Handler myHandler = new Handler() {
		
		  @Override
		  public void handleMessage(Message msg) {

			  switch (msg.what) {
		      default:
		        if (!isFinishing()) {
				Bundle b = getIntent().getExtras();
				campoBusca = b.getString("paramBusca");
				if(campoBusca == null){
					lat1 = b.getDouble("lat1");
					long1 = b.getDouble("long1");
					lat2 = b.getDouble("lat2");
					long2 = b.getDouble("long2");
					resultado = searchRouteBetweenTwoPoints(lat1, long1, lat2, long2);
				}else{
					resultado = searchRoute(campoBusca);
				}
							
				if(resultado.size() == 0){
					finish();
				}
		        showDialog(1);
		        create();
		    }
			  }
		  }
		
		};
	
	private void create(){
		datasource = new RotaDataSource(this);
		iconeFavoritos = (ImageView) bodyResult.findViewById(R.id.image_favoritos);
		iconeFavoritos.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				switch (R.drawable.estrela_cinza) { //mudar isso para pegar a atual imagem carregada
				case R.drawable.estrela_cinza:

					if(viewInflateFavoritos == null)
						viewInflateFavoritos = getLayoutInflater().inflate(R.layout.list_routes, null);
					
					datasource.open();
					List<Rota> values = datasource.getAllRoutes();
					listView = (ListView) findViewById(R.id.tela_consulta_listView);
					rotas = new ArrayList<RouteListView>();
					
					for (Rota rota : values) {
						rotas.add(new RouteListView(rota.getRoutename(), rota.getColour(), rota.getUrlRoute(), (int) rota.getDifBetweenBus(),
								rota.getStartTime(), rota.getEndTime(), (int) rota.getTimePerTotal(), (int) rota.getNumBus()));
					}
					
					Rota r = null;
					//ver depois
					try {
						r = datasource.createRota(spinnerRotas.getSelectedItem().toString(), service.getRouteCor(idRota), atualURLMap, Integer.parseInt(service.getRouteTimeDifferenceBetweenBusPath(idRota)), 
								service.getRouteStartTimePath(idRota), service.getRouteEndTimePath(idRota), Integer.parseInt(service.getRouteTotalTimePath(idRota)), 
								Integer.parseInt(service.getRouteNumberBusPath(idRota)), service.getRouteDaysPath(idRota));
						
						if (r == null){
							//Rota ja tem l�
							Toast.makeText(ResultadoActivity.this, getString(R.string.rota_ja_existe), Toast.LENGTH_LONG).show();
						} else {
							rotas.add(new RouteListView(r.getRoutename(), r.getColour(), r.getUrlRoute(), (int) r.getDifBetweenBus(), 
									r.getStartTime(), r.getEndTime(), (int)r.getTimePerTotal(), (int) r.getNumBus()));
							
							Toast.makeText(ResultadoActivity.this, getString(R.string.add_rota), Toast.LENGTH_LONG).show();
							iconeFavoritos.setImageResource(R.drawable.estrela_amarela);
						}
						
					} catch (NumberFormatException e) {
						Toast.makeText(ResultadoActivity.this, R.string.erro_rota, Toast.LENGTH_LONG).show();
						e.printStackTrace();
					} catch (Exception e) {
						Toast.makeText(ResultadoActivity.this, R.string.erro_rota, Toast.LENGTH_LONG).show();
						e.printStackTrace();
					}
					
					datasource.close();
					break;
				case R.drawable.estrela_amarela:
					Toast.makeText(ResultadoActivity.this, getString(R.string.rota_ja_existe), Toast.LENGTH_LONG).show();
					break;

				default:
					break;
				}
			}
		});
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
					
					if(!urlRota.equals("")){
						Uri uri1 = Uri.parse(urlRota);					
						Intent mapIntent = new Intent(Intent.ACTION_VIEW, uri1);
						mapIntent.setData(uri1);
						startActivity(Intent.createChooser(mapIntent, idRota));	
						finish();
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
				
				datasource.open();
				
				if(datasource.favouriteRoute(nome)){
					iconeFavoritos.setImageResource(R.drawable.estrela_amarela);
				} else {
					iconeFavoritos.setImageResource(R.drawable.estrela_cinza);
				}

				datasource.close();
				
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
		TextView textTempo = (TextView) bodyResult.findViewById(R.id.text_tempo_restante);
		TextView textFaltam = (TextView) bodyResult.findViewById(R.id.text_faltam);
		TextView textMinutos = (TextView) bodyResult.findViewById(R.id.text_minutos);
		textTempo.setText("");
		int tempo = Integer.parseInt(getRouteTimeWait(idRota));
		if(tempo < 0){
			textFaltam.setText(getString(R.string.indisponivel));
			textMinutos.setText("");
		}else{
			textFaltam.setText(getString(R.string.result_faltam));
			textMinutos.setText(getString(R.string.result_minutos));
			textTempo.setText(""+tempo);
		}		
		this.atualURLMap = "http://busaoapp.com/service/"+getRouteUrlRota(idRota);
	}


	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// Remove o Listener para n�o ficar atualizando mesmo depois de sair
	}
	
	public Map<String, String> searchRouteBetweenTwoPoints(double lat1, double long1, double lat2, double long2){
		try {
			return service.searchRouteBetweenTwoPoints(lat1, long1, lat2, long2);
		} catch (NoReturnDataException no) {
			 Toast.makeText(ResultadoActivity.this,
			 getString(R.string.nenhum_dado_retornado),
			 Toast.LENGTH_LONG).show();
			no.printStackTrace();
			return new HashMap<String, String>();
		} catch (Exception e) {
			 Toast.makeText(ResultadoActivity.this,
					 getString(R.string.sem_conexao),
					 Toast.LENGTH_LONG).show();
					e.printStackTrace();
					return new HashMap<String, String>();
		}
	}
	
	public Map<String, String> searchRoute(String campoBusca){
		try {
			Log.i("CampoBuscar", ""+campoBusca);
			return service.searchRoute(campoBusca);
		} catch (NoReturnDataException no) {
			 Toast.makeText(ResultadoActivity.this,
			 getString(R.string.nenhum_dado_retornado),
			 Toast.LENGTH_LONG).show();
			no.printStackTrace();
			return new HashMap<String, String>();
		} catch (Exception e) {
			 Toast.makeText(ResultadoActivity.this,
					 getString(R.string.sem_conexao),
					 Toast.LENGTH_LONG).show();
					e.printStackTrace();
					return new HashMap<String, String>();
		}
	}
	
	public String getRouteTimeWait(String idRota){
		try {
			return service.getRouteTimeWait(idRota);
		} catch (NoReturnDataException no) {
			 Toast.makeText(ResultadoActivity.this,
			 getString(R.string.nenhum_dado_retornado),
			 Toast.LENGTH_LONG).show();
			no.printStackTrace();
			return "";
		} catch (Exception e) {
			 Toast.makeText(ResultadoActivity.this,
					 getString(R.string.sem_conexao),
					 Toast.LENGTH_LONG).show();
					e.printStackTrace();
					return "";
		}
	}
	
	public String getRouteUrlRota(String idRota){
		try {
			return service.getRouteUrlRota(idRota);
		} catch (NoReturnDataException no) {
			 Toast.makeText(ResultadoActivity.this,
			 getString(R.string.nenhum_dado_retornado),
			 Toast.LENGTH_LONG).show();
			no.printStackTrace();
			return "";
		} catch (Exception e) {
			 Toast.makeText(ResultadoActivity.this,
					 getString(R.string.sem_conexao),
					 Toast.LENGTH_LONG).show();
					e.printStackTrace();
					return "";
		}
	}

}