package br.edu.ufcg.dsc.busao;

import it.sephiroth.demo.slider.widget.MultiDirectionSlidingDrawer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import br.edu.ufcg.dsc.R;
import br.edu.ufcg.dsc.dao.Rota;
import br.edu.ufcg.dsc.dao.RotaDataSource;
import br.edu.ufcg.dsc.httpmodule.HTTPModuleFacade;
import br.edu.ufcg.dsc.util.AdapterRouteListView;
import br.edu.ufcg.dsc.util.PontoAdapter;
import br.edu.ufcg.dsc.util.PontoTuristico;
import br.edu.ufcg.dsc.util.RouteListView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class BuscarActivity extends MapActivity {
	
	TableRow rowLocalidade, rowBuscar, rowTurismo, rowAjuda, rowLogoBusao;
	ImageView botaoAlterarCidade;
	ImageView botaoBuscarOnibus = null;
	ImageView botaoBuscarPonto = null; 
	ImageView botaoRotasFavoritas = null;
	View viewInflateOnibus;
	View viewInflateMapa;
	View viewInflateFavoritos;
	
	private String buscaLinha;
	private ImageView pesquisaOnibus, pesquisaDoisPontos;
	
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
	private ListView listView;
	private com.google.android.maps.MyLocationOverlay ondeEstou;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_buscar);
		setTextFont();
		instanciarRows();
		instanciarBotoes();
		selectRowBuscar();
		MultiDirectionSlidingDrawer drawer = (MultiDirectionSlidingDrawer) findViewById(R.id.drawer);
		drawer.close();
		
		setActionsRows(rowLocalidade, R.layout.menu_localidade);
		setActionsRows(rowTurismo, R.layout.menu_turismo);
		setActionsRows(rowAjuda, R.layout.menu_ajuda);
		setActionRowBuscar();
		setActionsRowLogo();
		setActionsBotao(botaoBuscarOnibus, 1);
		setActionsBotao(botaoBuscarPonto, 2);
		setActionsBotao(botaoRotasFavoritas, 3);
		
		service = HTTPModuleFacade.getInstance();
		
		paramBusca = (EditText) findViewById(R.id.paramBusca);
		
		pesquisaOnibus = (ImageView) findViewById(R.id.search);
		
		pesquisaOnibus.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Log.i("clicou", "Clicouu");
				telaConsultar = new Intent(BuscarActivity.this, ResultadoActivity.class);
				Bundle b = new Bundle();
				b.putString("paramBusca", paramBusca.getText().toString());
				telaConsultar.putExtras(b);				
				startActivity(telaConsultar);
			}
		});
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
			//R.layout.menu_localidade
			break;		
			
		case 2:
			//R.layout.buscar_onibus
			mapView = (MapView)findViewById(R.id.mapView);
			pesquisaDoisPontos = (ImageView) findViewById(R.id.image_botao_pesquisar_pontos);
			pesquisaDoisPontos.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					if(mapaPontosSelecionado.size() == 0){
						Toast.makeText(BuscarActivity.this, getString(R.string.sem_ponto_selecionado), Toast.LENGTH_LONG).show();
						return;
					}else if(mapaPontosSelecionado.size() == 2){
						Log.i("mapa1", ""+mapaPontosSelecionado);
						mapaPontosSelecionado.put("latitudeTo", -35.09); //latitude gps
						mapaPontosSelecionado.put("longitudeTo", -35.09); //longitude gps
						
					}
					Log.i("mapa2", ""+mapaPontosSelecionado);
						telaConsultar = new Intent(BuscarActivity.this, ResultadoActivity.class);
						Bundle b = new Bundle();
						b.putDouble("lat1", mapaPontosSelecionado.get("latitudeFrom"));
						b.putDouble("long1", mapaPontosSelecionado.get("longitudeFrom"));
						b.putDouble("lat2", mapaPontosSelecionado.get("latitudeTo"));
						b.putDouble("long2", mapaPontosSelecionado.get("longitudeTo"));
						telaConsultar.putExtras(b);				
						startActivity(telaConsultar);					
					
				}
			});
			zoomMap();
			onCreateMap();
			setMapCenter();
			break;
			
		case 3:
			//rotas favoritas
			rotaDataSource = new RotaDataSource(this);
			rotaDataSource.open();
			
			List<Rota> values = rotaDataSource.getAllRoutes();
			
			listView = (ListView) findViewById(R.id.tela_consulta_listView);
			
			rotas = new ArrayList<RouteListView>();
			
			for (Rota rota : values) {
				rotas.add(new RouteListView(rota.getRoutename(), rota.getColour(), rota.getUrlRoute(), (int) rota.getDifBetweenBus(),
						rota.getStartTime(), rota.getEndTime(), (int) rota.getTimePerTotal(), (int) rota.getNumBus()));
			}
			
			adapterListView = new AdapterRouteListView(this, rotas);
			listView.setAdapter(adapterListView);

			//SE CLICAR EM UM ITEM MOSTRAR A ROTA
			
			//SE PRESSIONAR O ITEM, APAGA
			rotaDataSource.close();
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
				limpaRows();

				row.setBackgroundDrawable(getResources().getDrawable(R.drawable.transparencia));
				RelativeLayout myLayout = (RelativeLayout) findViewById(R.id.include1);
				myLayout.removeAllViews();
				myLayout.addView(getLayoutInflater().inflate(layout, null));
				myLayout.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				setAlteracoesTela(layout);
			}
		});
	}
	
	private void setActionRowBuscar(){
		rowBuscar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				BuscarActivity.this.startActivity(getIntent());
			}
		});
	}
	
	private void setActionsRowLogo() {
		rowLogoBusao.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(R.layout.about);
			}
		});
	}
	
	private void setAlteracoesTela(int id){
		switch (id) {
		case R.layout.menu_localidade:
			//alterar os dados...
			botaoAlterarCidade = (ImageView) findViewById(R.id.botao_alterar_cidade);
			setActionAlterarCidade(botaoAlterarCidade);
			break;		
			
		case R.layout.menu_turismo:
			criaListView();
		default :
			break;
		}
	}
	
	private void setActionAlterarCidade(ImageView botao){
		if(botao == null) return;
		botao.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				showDialog(R.layout.popup_escolher_cidade);
			}
		});
	}
	
	private void criaListView() {
		ListView list = (ListView) findViewById(R.id.turismo_list);;
		List<PontoTuristico> pontos = new ArrayList<PontoTuristico>();
		PontoAdapter adapter;
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
			} else { //imagem default
				imagem = R.drawable.pt_default;
			}
			
			pontos.add(new PontoTuristico(map.get("id"), map.get("nome"), map.get("latitude"), map.get("longitude"), map.get("descricao"), imagem));
		}
		adapter = new PontoAdapter(this,pontos);
		list.setAdapter(adapter);
	}
	
	private void limpaRows(){
		rowLocalidade.setBackgroundDrawable(null);
		rowBuscar.setBackgroundDrawable(null);
		rowTurismo.setBackgroundDrawable(null);
		rowAjuda.setBackgroundDrawable(null);
	}
	
	private void setTextFont(){
		//set Font
		TextView textLocalidade = (TextView) findViewById(R.id.textLocalidade);  
		TextView textBuscar = (TextView) findViewById(R.id.textBuscar);  
		TextView textTurismo = (TextView) findViewById(R.id.textTurismo);  
		TextView textAjuda = (TextView) findViewById(R.id.textAjuda);  
		TextView textAlterarCidade = (TextView) findViewById(R.id.text_alterar_cidade);  
		Typeface font = Typeface.createFromAsset(getAssets(), "font.TTF");  
		textLocalidade.setTypeface(font);
		textBuscar.setTypeface(font);
		textTurismo.setTypeface(font);
		textAjuda.setTypeface(font);
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
		
		if (mapView != null){
	        MyLocationOverlayLocal mapOverlay = new MyLocationOverlayLocal();
	        ondeEstou = new com.google.android.maps.MyLocationOverlay(this, mapView);
	        listOfOverlays = mapView.getOverlays();
	        listOfOverlays.clear();
	        listOfOverlays.add(ondeEstou);
	        listOfOverlays.add(mapOverlay);  
	        
	        mapView.invalidate();
		}
	}

	public void atualizaBotoes(){
		//TODO
//		if(mapaPontosSelecionado.size() == 4){
//			botaoPesquisar.setAlpha(255);
//			botaoPesquisar.setEnabled(true);
//		}else{
//			botaoPesquisar.setAlpha(50);
//			botaoPesquisar.setEnabled(false);
//		}
//		if(mapaPontosSelecionado.size() > 0){
//			botaoLimpar.setAlpha(255);
//			botaoLimpar.setEnabled(true);
//		}else{
//			botaoLimpar.setAlpha(50);
//			botaoLimpar.setEnabled(false);
//		}
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
		Log.i("pontos", mapaPontosSelecionado.toString());
		//atualizaMapa();
	}
	
	public void limparDados(){
		if(listOfOverlays.size() > 1){
			if (listOfOverlays.size() == 3){
				listOfOverlays.remove(2);
			}
			listOfOverlays.remove(1);
		}
		mapaPontosSelecionado.clear();
		//atualizaBotoes();
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
	
	class MyLocationOverlayLocal extends com.google.android.maps.Overlay {

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
			 if(endTime - startTime > 1000 && listOfOverlays.size() < 3){
			 	GeoPoint p = mapView.getProjection().fromPixels((int) event.getX(),(int) event.getY());
	              
			 	MapOverlay novo = new MapOverlay(p);
			 	inserePontoSelecionado(p.getLatitudeE6()/ 1E6, p.getLongitudeE6()/ 1E6);
			 	listOfOverlays.add(novo);
		     } 

	         return false;
	    }     
	}
	
}