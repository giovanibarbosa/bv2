package br.edu.ufcg.dsc.busao;

import it.sephiroth.demo.slider.widget.MultiDirectionSlidingDrawer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.Toast;
import br.edu.ufcg.dsc.R;
import br.edu.ufcg.dsc.dao.Rota;
import br.edu.ufcg.dsc.dao.RotaDataSource;
import br.edu.ufcg.dsc.httpmodule.HTTPModuleFacade;
import br.edu.ufcg.dsc.util.AdapterRouteListView;
import br.edu.ufcg.dsc.util.CustomBuilder;
import br.edu.ufcg.dsc.util.RouteListView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class BuscarActivity extends MapActivity implements LocationListener{
	
	TableRow rowLocalidade, rowBuscar, rowTurismo, rowAjuda, rowLogoBusao;
	ImageView botaoAlterarCidade;
	ImageView botaoBuscarOnibus = null;
	ImageView botaoBuscarPonto = null; 
	ImageView botaoRotasFavoritas = null;
	View viewInflateOnibus;
	View viewInflateMapa;
	View viewInflateFavoritos;
	
	private ImageView pesquisaOnibus, pesquisaDoisPontos, limparOverlays;
	
	MapView mapView;
	private List<Overlay> listOfOverlays;
	private Map<String,Double> mapaPontosSelecionado = new HashMap<String, Double>();
	private long startTime=0;
	private long endTime=0;
	private Intent telaConsultar;
	private EditText paramBusca;
	private HTTPModuleFacade service;
	private RotaDataSource rotaDataSource;
	private ArrayList<RouteListView> rotas;
	private AdapterRouteListView adapterListView;
	private ListView listView, list;
	private com.google.android.maps.MyLocationOverlay ondeEstou;
	private List<Rota> values;
	private InputMethodManager imm;
	private MapController controlador;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_buscar);
		instanciarRows();
		instanciarBotoes();
		selectRowBuscar();
		MultiDirectionSlidingDrawer drawer = (MultiDirectionSlidingDrawer) findViewById(R.id.drawer);
		drawer.close();
		
		setActionsRows(rowLocalidade, R.layout.menu_localidade);
		setActionsRows(rowTurismo, R.layout.menu_turismo);
		setActionsRows(rowAjuda, R.layout.menu_ajuda);
		setActionsRowLogo();
		paramBusca = (EditText) findViewById(R.id.paramBusca);
		imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		paramBusca.clearFocus();
		imm.hideSoftInputFromWindow(paramBusca.getWindowToken(), 0); 
		pesquisaOnibus = (ImageView) findViewById(R.id.search);
		
		pesquisaOnibus.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				telaConsultar = new Intent(BuscarActivity.this, ResultadoActivity.class);
				Bundle b = new Bundle();
				b.putString("paramBusca", paramBusca.getText().toString());
				telaConsultar.putExtras(b);				
				startActivity(telaConsultar);
			}
		});
		
		setActionsBotao(botaoBuscarOnibus, 1);
		setActionsBotao(botaoBuscarPonto, 2);
		setActionsBotao(botaoRotasFavoritas, 3);
		
		service = HTTPModuleFacade.getInstance();
	}
	
	private void instanciarBotoes() {
		botaoBuscarOnibus = (ImageView) findViewById(R.id.botao_icone_buscar_onibus);
		botaoBuscarPonto = (ImageView) findViewById(R.id.botao_icone_buscar_ponto);
		botaoRotasFavoritas = (ImageView) findViewById(R.id.botao_icone_rotas_favoritas);
		botaoBuscarOnibus.setAlpha(100);
		botaoBuscarOnibus.setEnabled(false);
	}

	private void setActionsBotao(final ImageView botao, final int codigo) {
		if(botao == null) return;
		botao.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				limpaBotoes();
				botao.setAlpha(100);
				botao.setEnabled(false);

				RelativeLayout myLayout = (RelativeLayout) findViewById(R.id.includePrincipal);
				myLayout.removeAllViewsInLayout();
				switch (codigo) {
				case 1:
					if(viewInflateOnibus == null)
						viewInflateOnibus = getLayoutInflater().inflate(R.layout.buscar_onibus, null);
					myLayout.addView(viewInflateOnibus);
					break;
				case 2:
					if(viewInflateMapa == null)
						viewInflateMapa = getLayoutInflater().inflate(R.layout.buscar_mapa, null);
					if(mapaPontosSelecionado != null)
						mapaPontosSelecionado.clear();
					myLayout.addView(viewInflateMapa);
					break;
				case 3:
					if(viewInflateFavoritos == null)
						viewInflateFavoritos = getLayoutInflater().inflate(R.layout.list_routes, null);
					myLayout.addView(viewInflateFavoritos);
					break;
				default:
					break;
				}
				
				setAlteracoesBuscar(codigo);
			}
		});
	}
	
	private void setAlteracoesBuscar(int id){
		switch (id) {
		case 1:	
			pesquisaOnibus = (ImageView) findViewById(R.id.search);
			paramBusca = (EditText) findViewById(R.id.paramBusca);
			paramBusca.clearFocus();
			imm.hideSoftInputFromWindow(paramBusca.getWindowToken(), 0); 
			pesquisaOnibus.setOnClickListener(new View.OnClickListener() {				
				public void onClick(View v) {
					telaConsultar = new Intent(BuscarActivity.this, ResultadoActivity.class);
					Bundle b = new Bundle();
					b.putString("paramBusca", paramBusca.getText().toString());
					telaConsultar.putExtras(b);				
					startActivity(telaConsultar);
				}
			});
			break;		
			
		case 2:
			//R.layout.buscar_onibus
			if (mapView == null) {
				mapView = (MapView) findViewById(R.id.mapView);
				controlador = mapView.getController();
				zoomMap();				
			}

				limparOverlays = (ImageView) findViewById(R.id.image_botao_limpar_pontos);
				limparOverlays.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						limparDados();			
					}
				});
			
				pesquisaDoisPontos = (ImageView) findViewById(R.id.image_botao_pesquisar_pontos);
				pesquisaDoisPontos
						.setOnClickListener(new View.OnClickListener() {

							public void onClick(View v) {
								if (mapaPontosSelecionado.size() == 0) {
									Toast.makeText(
											BuscarActivity.this,
											getString(R.string.sem_ponto_selecionado),
											Toast.LENGTH_LONG).show();
									return;
								} else if (mapaPontosSelecionado.size() == 2) {
									double latitude = Double.parseDouble(service.getUser().getLatitude());
									double longitude = Double.parseDouble(service.getUser().getLongitude());
									if(ondeEstou.getMyLocation() != null){
										latitude = ondeEstou.getMyLocation().getLatitudeE6()/1E6;
										longitude = ondeEstou.getMyLocation().getLongitudeE6()/1E6;
									}									
									mapaPontosSelecionado.put("latitudeTo",
											latitude); // latitude gps
									mapaPontosSelecionado.put("longitudeTo",
											longitude); // longitude gps

								}
								telaConsultar = new Intent(BuscarActivity.this,
										ResultadoActivity.class);
								Bundle b = new Bundle();
								b.putDouble("lat1", mapaPontosSelecionado
										.get("latitudeFrom"));
								b.putDouble("long1", mapaPontosSelecionado
										.get("longitudeFrom"));
								b.putDouble("lat2",
										mapaPontosSelecionado.get("latitudeTo"));
								b.putDouble("long2", mapaPontosSelecionado
										.get("longitudeTo"));
								telaConsultar.putExtras(b);
								startActivity(telaConsultar);

							}
						});			
				onCreateMap();
				setMapCenter();
			break;
			
		case 3:
			//rotas favoritas
			rotaDataSource = new RotaDataSource(this);
			rotaDataSource.open();
			
			listView = (ListView) findViewById(R.id.tela_consulta_listView);
			
			values = rotaDataSource.getAllRoutes();
			
			rotas = new ArrayList<RouteListView>();
			
			for (Rota rota : values) {
				rotas.add(new RouteListView(rota.getRoutename(), rota.getColour(), rota.getUrlRoute(), (int) rota.getDifBetweenBus(),
						rota.getStartTime(), rota.getEndTime(), (int) rota.getTimePerTotal(), (int) rota.getNumBus()));
			}
			
			adapterListView = new AdapterRouteListView(this, rotas);
			listView.setAdapter(adapterListView);
			rotaDataSource.close();
			
			//SE PRESSIONAR O ITEM, APAGA
			listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

	            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {
	                // TODO Auto-generated method stub
	            	
	            	String routename = adapterListView.getItem(pos).getRoutename(); 
	            	rotaDataSource.open();
	            	rotaDataSource.deleteRota(routename); //Ja que nao tem 2 rotas com o mesmo nome cadastradas, pode apagar pelo nome
	            	
	            	values = rotaDataSource.getAllRoutes();
	    			
	    			rotas = new ArrayList<RouteListView>();
	    			
	    			for (Rota rota : values) {
	    				rotas.add(new RouteListView(rota.getRoutename(), rota.getColour(), rota.getUrlRoute(), (int) rota.getDifBetweenBus(),
	    						rota.getStartTime(), rota.getEndTime(), (int) rota.getTimePerTotal(), (int) rota.getNumBus()));
	    			}
	            	
	    			adapterListView = new AdapterRouteListView(BuscarActivity.this, rotas);
	    			listView.setAdapter(adapterListView);
	    			
	            	rotaDataSource.close();

	            	Toast.makeText(BuscarActivity.this, R.string.delete_rota, Toast.LENGTH_LONG).show();
	                return true;
	            }
	        }); 

			//SE CLICAR EM UM ITEM MOSTRAR A ROTA
			listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,	int pos, long id) {
					String rota = adapterListView.getItem(pos).getUrlRoute();
					System.out.println(rota);
					
					if(!rota.equals("")){
						Uri uri1 = Uri.parse(rota);					
						Intent mapIntent = new Intent(Intent.ACTION_VIEW, uri1);
						mapIntent.setData(uri1);
						
						rotaDataSource.open();
						
						long idRota = rotaDataSource.getIdFromRoute(adapterListView.getItem(pos).getRoutename());
						
						if ((int) idRota != 0){
							startActivity(Intent.createChooser(mapIntent, idRota+""));				
						} else {
							Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.result_sem_mapa) , Toast.LENGTH_SHORT);
						}
						
					}else{
						Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.result_sem_mapa) , Toast.LENGTH_SHORT);
						toast.show();
					}
				}
			});
			
			break;
		default :
			break;
		}
	}
	
	private void limpaBotoes() {
		botaoBuscarOnibus.setAlpha(255);
		botaoBuscarPonto.setAlpha(255);
		botaoRotasFavoritas.setAlpha(255);
		botaoBuscarOnibus.setEnabled(true);
		botaoBuscarPonto.setEnabled(true);
		botaoRotasFavoritas.setEnabled(true);
	}
	
	private void selectRowBuscar(){
		rowBuscar.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.transparencia));
	}
	
	private void instanciarRows() {
		rowLocalidade = (TableRow) findViewById(R.id.rowLocalidade);
		rowBuscar = (TableRow) findViewById(R.id.rowBuscar);
		rowTurismo = (TableRow) findViewById(R.id.rowTurismo);
		rowAjuda = (TableRow) findViewById(R.id.rowAjuda);
		rowLogoBusao = (TableRow) findViewById(R.id.rowLogoBusao);
		
	}
	
	private void setActionsRows(final TableRow row, final int layout) {
		row.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Intent menuPrincipal = new Intent(BuscarActivity.this,
						MenuActivity.class);
				Bundle b = new Bundle();
				b.putInt("row", layout);
				menuPrincipal.putExtras(b);	
				BuscarActivity.this.startActivity(menuPrincipal);
				BuscarActivity.this.finish();
			}
		});
	}
	
	
	private void setActionsRowLogo() {
		rowLogoBusao.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(2);
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
		case 2:
			builder = new CustomBuilder( ctw, R.layout.about );
			builder.setTitle("");
			builder.setIcon(null);
			builder.setCancelable( false );
			builder.setNegativeButton(getString(R.string.botao_voltar), new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick( DialogInterface dialog, int which )
							{
								dialog.dismiss();
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
		return dialog;
	}



	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	private void zoomMap(){
		LinearLayout zoomLayout = (LinearLayout)findViewById(R.id.zoom);  
        View zoomView = mapView.getZoomControls();
        zoomLayout.addView(zoomView, new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)); 
        mapView.displayZoomControls(true);
	}
	
	private void setMapCenter(){
		MapController mc = mapView.getController();
		GeoPoint centroCidade = new GeoPoint((int) (-07.23 * 1E6),(int) (-35.88 * 1E6)); 
		mc.setCenter(centroCidade);
		mc.setZoom(16);
	}
	
	public void onCreateMap(){
		MyLocationOverlay mapOverlay = new MyLocationOverlay();
	    ondeEstou = new com.google.android.maps.MyLocationOverlay(this, mapView);
	    listOfOverlays = mapView.getOverlays();
	    listOfOverlays.clear();
	    listOfOverlays.add(ondeEstou);
	    listOfOverlays.add(mapOverlay);  
	        
	    mapView.invalidate();
	}

	public void inserePontoSelecionado(double lat, double longi){
		if(mapaPontosSelecionado.size()< 4){
			if (mapaPontosSelecionado.size() == 2){
				mapaPontosSelecionado.put("latitudeTo", lat);
				mapaPontosSelecionado.put("longitudeTo", longi);
			}else{
				mapaPontosSelecionado.put("latitudeFrom", lat);
				mapaPontosSelecionado.put("longitudeFrom", longi);
			}
		}
	}
	
	public void limparDados(){
		if(listOfOverlays.size() > 1){
			if (listOfOverlays.size() == 3){
				listOfOverlays.remove(2);
			}
			listOfOverlays.remove(1);
		}
		mapaPontosSelecionado.clear();
		onCreateMap();
	}
	
	class MapOverlay extends Overlay{
        private GeoPoint point;
        public MapOverlay(GeoPoint point) {
        	this.point = point;
        }
        
        @Override
		public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when) {
            super.draw(canvas, mapView, shadow);                   
 
            //---translate the GeoPoint to screen pixels---
            Point screenPts = new Point();
            mapView.getProjection().toPixels(point, screenPts);
 
            //---add the marker---
            Bitmap bmp = BitmapFactory.decodeResource(
                getResources(), R.drawable.icon_touch_bus);            
            canvas.drawBitmap(bmp, screenPts.x-6, screenPts.y-20, null);         
            return true;
        }
              
        
    }
	
	class MyLocationOverlay extends com.google.android.maps.Overlay {

		 @Override
	     public boolean onTouchEvent(MotionEvent event, MapView mapView) {          	
	
			 if(event.getAction() == MotionEvent.ACTION_DOWN){
				 //record the start time
	             startTime = event.getEventTime();
	             Log.d("LC", "IN DOWN");
	            
			 }else if(event.getAction() == MotionEvent.ACTION_UP){
	             //record the end time
	             endTime = event.getEventTime();
	             Log.d("LC", "IN UP");
	            
			 }else if(event.getAction() == MotionEvent.ACTION_MOVE){
	                 Log.d("LC", "IN move");
	                 endTime=0;
			 }

	         //verify
			 if(endTime - startTime > 1000 && listOfOverlays.size() < 4){
			 	GeoPoint p = mapView.getProjection().fromPixels((int) event.getX(),(int) event.getY());
	              
			 	MapOverlay novo = new MapOverlay(p);
			 	inserePontoSelecionado(p.getLatitudeE6()/ 1E6, p.getLongitudeE6()/ 1E6);
			 	listOfOverlays.add(novo);
		     } 

	         return false;
	    }     
	}

	@Override
	public void onLocationChanged(Location loc) {
		int latitude = (int) (loc.getLatitude() * 1E6);
		int longitude = (int) (loc.getLongitude() * 1E6);
		GeoPoint ponto = new GeoPoint(latitude, longitude);

		controlador.animateTo(ponto);
		controlador.setZoom(15);
		mapView.invalidate();
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	
	private LocationManager getLocationManager() {
		return (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	}

	@Override
	protected void onResume() {
		super.onResume();
		// Registra o Listener
		if(ondeEstou != null)
		ondeEstou.enableMyLocation();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// Remove o listener
		if(ondeEstou != null)
		ondeEstou.disableMyLocation();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// Remove o Listener para não ficar atualizando mesmo depois de sair
		getLocationManager().removeUpdates(this);
	}
	
}