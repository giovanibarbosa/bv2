package br.edu.ufcg.dsc.busao;

import com.google.android.maps.MapActivity;

import it.sephiroth.demo.slider.widget.MultiDirectionSlidingDrawer;
import br.edu.ufcg.dsc.R;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class BuscarActivity extends MapActivity {
	
	TableRow rowLocalidade, rowBuscar, rowTurismo, rowAjuda, rowLogoBusao;
	ImageView botaoBuscarOnibus = null;
	ImageView botaoBuscarPonto = null; 
	ImageView botaoRotasFavoritas = null;
	View viewInflateOnibus;
	View viewInflateMapa;
	View viewInflateFavoritos;
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
		setActionsBotao(botaoBuscarOnibus, 1);
		setActionsBotao(botaoBuscarPonto, 2);
		setActionsBotao(botaoRotasFavoritas, 3);

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
				
			}

		});
		
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
}