package br.edu.ufcg.dsc.busao;

import java.util.zip.Inflater;

import it.sephiroth.demo.slider.widget.MultiDirectionSlidingDrawer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import br.edu.ufcg.dsc.R;
import br.edu.ufcg.dsc.util.CustomBuilder;

public class MenuActivity extends Activity {
	TableRow rowLocalidade, rowBuscar, rowTurismo, rowAjuda, rowLogoBusao;
	ViewGroup includePrincipal;
	private static final int ALERT_DIALOG_ALTERAR_CIDADE = 1;
	
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
		
		//Abrir PoPup
//		showDialog( ALERT_DIALOG_ALTERAR_CIDADE );			
		
		
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
		if ( id == ALERT_DIALOG_ALTERAR_CIDADE )
		{
			ContextThemeWrapper ctw = new ContextThemeWrapper( this, R.style.MyTheme );
			CustomBuilder builder = new CustomBuilder( ctw, R.layout.popup_escolher_cidade );
			builder.setTitle( "Alert Dialog" );
			builder.setIcon( R.drawable.seta_titulo );
			builder.setCancelable( false );
			builder.setPositiveButton( R.string.botao_confirmar,
						new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick( DialogInterface dialog, int which )
							{
								dialog.dismiss();
							}
						} 
			);
			builder.setNegativeButton(R.string.botao_cancelar, new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick( DialogInterface dialog, int which )
							{
								dialog.dismiss();
							}
						} );
		}
		if ( dialog == null )
		{
			dialog = super.onCreateDialog( id );
		}
		return dialog;
	}
	
}
