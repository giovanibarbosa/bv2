package br.edu.ufcg.dsc.busao;

import java.util.zip.Inflater;

import it.sephiroth.demo.slider.widget.MultiDirectionSlidingDrawer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import br.edu.ufcg.dsc.R;

public class MenuActivity extends Activity {
	TableRow rowLocalidade, rowBuscar, rowTurismo, rowAjuda, rowLogoBusao;
	ViewGroup includePrincipal;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		setTextFont();
		MultiDirectionSlidingDrawer drawer = (MultiDirectionSlidingDrawer) findViewById(R.id.drawer);
		drawer.open();
		instanciarRows();
		setActionsRows(rowLocalidade, R.layout.menu_localidade);
		setActionsRows(rowBuscar, R.layout.menu_localidade);
		setActionsRows(rowTurismo, R.layout.menu_localidade);
		setActionsRows(rowAjuda, R.layout.menu_ajuda);
		includePrincipal = (ViewGroup) findViewById(R.id.includePrincipal);
		


        // The listener for the second button also has to be defined here as opposed to in the onCreate, as the score_submitted.xml isn't loaded yet at activity first run
//        Button button = (Button)findViewById(R.id.new_game);
//        button.setOnClickListener(newGameListener);
		
//		LayoutParams params = null;
//		params = getResources().generateLayoutParams(getResources().getLayout(R.layout.principal_localidade));
//		includePrincipal.setLayoutParams(new LayoutParams(getResources().getLayout(R.layout.principal_localidade)));
		//Setar aqui a ação do butão para abrir o popup
				
		
		
	}
	
	private void setActionsRows(final TableRow row, final int layout) {
		row.setOnClickListener(new View.OnClickListener() {

	        public void onClick(View v) {
	           limpaRows();
	           row.setBackgroundDrawable(getResources().getDrawable(R.drawable.transparencia));
	           
//	   		Intent menuPrincipal = new Intent(MenuActivity.this,
//					LocalidadeActivity.class);
//	   		MenuActivity.this.startActivity(menuPrincipal);
//	   		MenuActivity.this.finish();
//	   		 Get a reference to the score_name_entry object in score.xml

	           LinearLayout myLayout = (LinearLayout)findViewById(R.id.includePrincipal);
	           myLayout.removeAllViews();
	          // RelativeLayout menuLayout = (RelativeLayout)findViewById(R.id.includeMenu);
	           myLayout.addView(getLayoutInflater().inflate(layout, null));
	     //      myLayout.addView(getLayoutInflater().inflate(R.layout.menu_localidade, (RelativeLayout)findViewById(R.layout.main)));
	         //  myLayout.addView((View)getResources().getLayout(R.layout.menu_localidade));

	        }
	    });
		
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
		Typeface font = Typeface.createFromAsset(getAssets(), "font.TTF");  
		textLocalidade.setTypeface(font);
		textBuscar.setTypeface(font);
		textTurismo.setTypeface(font);
		textAjuda.setTypeface(font);
	}
	
}
