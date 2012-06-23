package br.edu.ufcg.dsc.busao;

import it.sephiroth.demo.slider.widget.MultiDirectionSlidingDrawer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import br.edu.ufcg.dsc.R;
import br.edu.ufcg.dsc.util.PontoAdapter;
import br.edu.ufcg.dsc.util.PontoTuristico;

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
	private ImageView pesquisaOnibus;
	
	MapView mapView;
	private List<Overlay> listOfOverlays;
	private Map<String,Double> mapaPontosSelecionado = new HashMap<String, Double>();
	private long startTime=0;
	private long endTime=0;
	
	
	
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
		
		setActionsBotao(botaoBuscarOnibus, 1);
		setActionsBotao(botaoBuscarPonto, 2);
		setActionsBotao(botaoRotasFavoritas, 3);
		
		pesquisaOnibus = (ImageView) findViewById(R.id.search);
		
//		pesquisaOnibus.setOnClickListener(new View.OnClickListener() {
//			
//			public void onClick(View v) {
//				buscaLinha = findViewById(R.id.paramBusca).toString();
//				
//			}
//		});
		
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
						viewInflateFavoritos = getLayoutInflater().inflate(R.layout.buscar_onibus, null);
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
			
			zoomMap();
			setMapCenter();
			onCreateMap();
			break;
		case 3:
			//rotas favoritas
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
 		pontos.add(new PontoTuristico("Canal de bodocongo","Canal de bodocongo eh um lugar para lazer e bla bla bla bla ", R.drawable.icon));
		pontos.add(new PontoTuristico("Acude de bodocongo","Acude de bodocongo eh um otimo lugar para se refrescar, muito limpo e 0 por cento de agua de esgoto ", R.drawable.transparencia));
		pontos.add(new PontoTuristico("Parque do Povo","Parque do povo eh um otimo lugar, extremamente seguro!!! Pode levar seu Android sem medo pra la ¬¬", R.drawable.logo_lrcosta));
	 
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
		GeoPoint centroCidade = new GeoPoint((int) (-07.14 * 1E6),(int) (-35.53 * 1E6)); 
		mc.setCenter(centroCidade);
	}
	
	public void onCreateMap(){
		
		if (mapView != null){
	        MyLocationOverlay mapOverlay = new MyLocationOverlay();
	        listOfOverlays = mapView.getOverlays();
	        listOfOverlays.clear();
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