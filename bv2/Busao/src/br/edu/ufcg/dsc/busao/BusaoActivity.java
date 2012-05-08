package br.edu.ufcg.dsc.busao;

import br.dsc.ufcg.edu.busao.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class BusaoActivity extends Activity {
    /** Called when the activity is first created. */
	
	private final int SPLASH_SCREEN_DURATION = 2000; //2 segundos
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        
        new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				Intent menuPrincipal = new Intent(BusaoActivity.this, MenuActivity.class);
				BusaoActivity.this.startActivity(menuPrincipal);
				BusaoActivity.this.finish();
			}
		}, SPLASH_SCREEN_DURATION);
    }
}

//OUTRA FORMA:
//
//public class BusaoActivity extends Activity implements Runnable {
//    /** Called when the activity is first created. */
//	
//	private final int SPLASH_SCREEN_DURATION = 2000; //2 segundos
//	
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.splash_screen);
//        
//        new Handler().postDelayed(this, SPLASH_SCREEN_DURATION);
//    }
//
//	@Override
//	public void run() {
//		Intent menuPrincipal = new Intent(BusaoActivity.this, MenuActivity.class);
//		BusaoActivity.this.startActivity(menuPrincipal);
//		BusaoActivity.this.finish();
//	}
//}