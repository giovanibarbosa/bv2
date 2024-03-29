package br.edu.ufcg.dsc.busao;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import br.dsc.ufcg.edu.busao.R;

public class MenuActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		//Quando clicar em cada botao, muda o conteudo dentro do linear layout
		//Inicialmente, criamos um scroll view com os filhos (informacoes sobre o Busao)
		carregaConfiguracoesIniciais();

		//A partir dai, limpa o conteudo do linear layout e adiciona um novo conteudo, dependendo do menu
		limpaLinearLayout();
	}
	
	/**
	 * Carrega e Inicia a configuracao inicial do Aplicativo. Ou seja, um scroll view
	 */
	private void carregaConfiguracoesIniciais() {
		
		HorizontalScrollView scroll = new HorizontalScrollView(this);
		scroll.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		
		LinearLayout elementosScrolls = new LinearLayout(this);
		elementosScrolls.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		
		ArrayList<Integer> titulos = new ArrayList<Integer>();
		ArrayList<Integer> descricoes = new ArrayList<Integer>();
		
		titulos.add(R.string.bem_vindo);
		titulos.add(R.string.menu_localidade);
		titulos.add(R.string.menu_buscar);
		titulos.add(R.string.menu_turismo);
		titulos.add(R.string.menu_ajuda);
		titulos.add(R.string.compartilhar);
		
		descricoes.add(R.string.bem_vindo_desc);
		descricoes.add(R.string.localidade_desc);
		descricoes.add(R.string.buscar_desc);
		descricoes.add(R.string.turismo_desc);
		descricoes.add(R.string.ajuda_desc);
		descricoes.add(R.string.compartilhar_desc);

		for (int i = 0; i < titulos.size(); i++) {
			LinearLayout textos = new LinearLayout(this);
			textos.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			textos.addView(criaTextTitulo(titulos.get(i)));
			textos.addView(criaTextDescricao(descricoes.get(i)));
			
			elementosScrolls.addView(textos);
		}
		
		scroll.addView(elementosScrolls);
		
		LinearLayout linearMenu = (LinearLayout) findViewById(R.id.layout_menu_scrolling);
		linearMenu.addView(scroll);
	}
	
	public TextView criaTextTitulo(int titulo){
		
		TextView tv = new TextView(this);
		tv.setText(titulo);
		tv.setTextSize(24);
		tv.setWidth(40);
		
		return tv;
	}
	
	public TextView criaTextDescricao(int descricao){
		
		TextView tv = new TextView(this);
		tv.setText(descricao);
		tv.setTextSize(18);
		tv.setWidth(40);
		
		return tv;
	}
	
	/**
	 * Limpa a pilha de Views no Linear Layout 
	 */
	public void limpaLinearLayout(){
		LinearLayout scrollView = (LinearLayout)findViewById(R.id.layout_menu_scrolling);
		scrollView.removeAllViews();
	}
}
