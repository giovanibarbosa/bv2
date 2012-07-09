package br.edu.ufcg.dsc.busao;

import it.sephiroth.demo.slider.widget.MultiDirectionSlidingDrawer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import br.edu.ufcg.dsc.R;
import br.edu.ufcg.dsc.httpmodule.HTTPModuleFacade;
import br.edu.ufcg.dsc.util.CustomBuilder;
import br.edu.ufcg.dsc.util.PontoAdapter;
import br.edu.ufcg.dsc.util.PontoTuristico;

public class MenuActivity extends Activity {
	
	TableRow rowLocalidade, rowBuscar, rowTurismo, rowAjuda, rowLogoBusao;
	ViewGroup includePrincipal;
	LinearLayout linearLayoutScrollView;
	ImageView botaoAlterarCidade, botaoSearch,botaoFace,botaoTwitter;
	HTTPModuleFacade service;
	private Map<String, String> cidadesApp = new HashMap<String, String>();
	private Spinner featuresSelection;
	private List<CharSequence> featuresList;
	private ArrayAdapter<CharSequence> featuresAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		setTextFont();
		MultiDirectionSlidingDrawer drawer = (MultiDirectionSlidingDrawer) findViewById(R.id.drawer);
		drawer.open();

		//scroll.setItemWidth(width);
		instanciarRows();
		setActionsRows(rowLocalidade, R.layout.menu_localidade);
		//setActionsRows(rowBuscar, R.layout.buscar_onibus);
		setActionRowBuscar();
		setActionsRows(rowTurismo, R.layout.menu_turismo);
		setActionsRows(rowAjuda, R.layout.menu_ajuda);
		setActionsRowLogo();
		service = HTTPModuleFacade.getInstance("1", "0", "0");

		//cria o spinner na tela
        featuresList = new ArrayList<CharSequence>();
        featuresAdapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, featuresList);       
		featuresAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
	
	private void setActionAlterarCidade(ImageView botao){
		if(botao == null) return;
		botao.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				showDialog( 1 );
			}
		});
	}
	
	private void setActionRowBuscar(){
		rowBuscar.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent buscarPrincipal = new Intent(MenuActivity.this,
						BuscarActivity.class);
				Bundle b = new Bundle();
			//	b.put("paramBusca", paramBusca.getText().toString());
				buscarPrincipal.putExtras(b);	
				MenuActivity.this.startActivity(buscarPrincipal);
				MenuActivity.this.finish();
				
			}
		});
	}
	
	
	private void setActionsRows(final TableRow row, final int layout) {
		row.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				limpaRows();
				row.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.transparencia));

				RelativeLayout myLayout = (RelativeLayout) findViewById(R.id.includePrincipal);
				myLayout.removeAllViews();
				myLayout.addView(getLayoutInflater().inflate(layout, null));
				setAlteracoesTela(layout);
			}
		});
		
	}
	
	private void setActionsRowLogo() {
		rowLogoBusao.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				showDialog( 2 );
			}
		});
		
	}
	
	private void setAlteracoesTela(int id){
		switch (id) {
		case R.layout.menu_localidade:
			//alterar os dados...
			botaoAlterarCidade = (ImageView) findViewById(R.id.botao_alterar_cidade);
			setActionAlterarCidade(botaoAlterarCidade);
			TextView textTarifa = (TextView) findViewById(R.id.text_preco_tarifa);
			textTarifa.setText(textTarifa.getText() + " " + service.getCityValorTarifa());
			
			featuresSelection = (Spinner) findViewById(R.id.spinner_cidade);		
			featuresSelection.setAdapter(featuresAdapter);
			
			cidadesApp = service.getAllCitys();
			
			if(cidadesApp.size() > 0){
				
				featuresAdapter.clear();
				for (String idCidadeEncontrada : cidadesApp.values()) {
					featuresAdapter.add(idCidadeEncontrada);	
				}
				
				featuresSelection.setEnabled(true);
			}
			
			break;		
			
		case R.layout.buscar_onibus:
			botaoSearch = (ImageView) findViewById(R.id.search);
			botaoSearch.setOnClickListener(new View.OnClickListener() {

					public void onClick(View v) {
						showDialog( 3 );
					}
				});
			break;
			
		case R.layout.menu_turismo:
			criaListView();
		default :
			break;
		}
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

	private void instanciarRows() {
		rowLocalidade = (TableRow) findViewById(R.id.rowLocalidade);
		rowBuscar = (TableRow) findViewById(R.id.rowBuscar);
		rowTurismo = (TableRow) findViewById(R.id.rowTurismo);
		rowAjuda = (TableRow) findViewById(R.id.rowAjuda);
		rowLogoBusao = (TableRow) findViewById(R.id.rowLogoBusao);
		
	}
	
	private void limpaRows(){
		rowLocalidade.setBackgroundDrawable(null);
		rowBuscar.setBackgroundDrawable(null);
		rowTurismo.setBackgroundDrawable(null);
		rowAjuda.setBackgroundDrawable(null);
	}

	/**
	 * Limpa a pilha de Views no Linear Layout 
	 */
	public void limpaLinearLayout(){
//		LinearLayout scrollView = (LinearLayout)findViewById(R.id.layout_menu_scrolling);
//		scrollView.removeAllViews();
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
	protected Dialog onCreateDialog( int id )
	{
		Dialog dialog = null;
		ContextThemeWrapper ctw = new ContextThemeWrapper( this, R.style.MyTheme );
		CustomBuilder builder = null;
		switch (id) {
		case 1:
			builder = new CustomBuilder( ctw, R.layout.popup_escolher_cidade );
			builder.setTitle( getString(R.string.titulo_alterar_cidade) );
			builder.setIcon( R.drawable.seta_titulo );
			builder.setCancelable( false );
			builder.setPositiveButton( getString(R.string.botao_confirmar),
						new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick( DialogInterface dialog, int which )
							{
								dialog.dismiss();
							}
						} 
			);
			builder.setNegativeButton(getString(R.string.botao_cancelar), new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick( DialogInterface dialog, int which )
							{
								dialog.dismiss();
							}
						} );
			break;

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
		
		case 3:
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
							}
						} );
			builder.setPositiveButton(getString(R.string.result_ok), new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick( DialogInterface dialog, int which )
				{
					dialog.dismiss();
				}
			} );
			builder.setNeutralButton(getString(R.string.result_mapa), new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick( DialogInterface dialog, int which )
				{
					//abrir mapa
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
	
}