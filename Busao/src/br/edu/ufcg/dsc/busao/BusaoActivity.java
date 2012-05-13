package br.edu.ufcg.dsc.busao;

import br.edu.ufcg.dsc.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class BusaoActivity extends Activity {
	private final int SPLASH_SCREEN_DURATION = 2000; //2 segundos
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	 super.onCreate(savedInstanceState);
         setContentView(R.layout.splash_screen);
         
         new Handler().postDelayed(new Runnable() {
 			
 			@Override
 			public void run() {
 				Intent menuPrincipal = new Intent(BusaoActivity.this, LoadingActivity.class);
 				BusaoActivity.this.startActivity(menuPrincipal);
 				BusaoActivity.this.finish();
 			}
 		}, SPLASH_SCREEN_DURATION);
     }
}