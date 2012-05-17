package br.edu.ufcg.dsc.busao;

import it.sephiroth.demo.slider.widget.MultiDirectionSlidingDrawer;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import br.edu.ufcg.dsc.R;

public class MenuActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu_principal);
		setTextFont();
		MultiDirectionSlidingDrawer drawer = (MultiDirectionSlidingDrawer) findViewById(R.id.drawer);
		drawer.open();
		
		
		
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
