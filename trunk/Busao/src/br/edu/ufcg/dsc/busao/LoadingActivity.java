package br.edu.ufcg.dsc.busao;


import br.edu.ufcg.dsc.R;
import br.edu.ufcg.dsc.httpmodule.HTTPModuleFacade;
import br.edu.ufcg.dsc.util.ThreadedClass;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class LoadingActivity extends Activity {
	private HTTPModuleFacade service;
	private ThreadedClass m_t;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loading);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


		m_t = new ThreadedClass(myHandler); 
        m_t.Start(); 
		
		
//		Intent telaConsultar = new Intent(LoadingActivity.this, BuscarActivity.class);
//		Bundle b = new Bundle();
//		b.putString("paramBusca", "202");
//		telaConsultar.putExtras(b);				
//		startActivity(telaConsultar);

	}
	
	private Handler myHandler = new Handler() {
		
		  @Override
		  public void handleMessage(Message msg) {

			  switch (msg.what) {
		      default:
		  		LocationManager LM = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
				String bestProvider = LM.getBestProvider(new Criteria(),true);
				//System.out.println("*************"+LM.getBestProvider(new Criteria(),true));
				Log.i("best", bestProvider);
				Location l = LM.getLastKnownLocation(bestProvider);
				double latitude = 0;
				double longitude = 0;
				if(l != null){
					Log.i("lat", ""+l.getLatitude());
					Log.i("long", ""+l.getLongitude());
					latitude = l.getLatitude();
					longitude = l.getLongitude();
				}
				
				//pegar gps
				service = HTTPModuleFacade.getInstance("1", latitude+"", longitude+"");
				// checa as atualizações necessárias,
				// depois passa para a tela de menu
				Intent principal = new Intent(LoadingActivity.this,	MenuActivity.class);
				LoadingActivity.this.startActivity(principal);
				LoadingActivity.this.finish();
			  }
		  }
		
		};
	
	@Override
	protected void onResume(){
		super.onResume();
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
}