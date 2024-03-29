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
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class LoadingActivity extends Activity {
	private HTTPModuleFacade service;
	private ThreadedClass m_t;
	double latitude = 0;
	double longitude = 0;
	private LocationManager mlocManager;
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
		  		mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

				LocationListener mlocListener = new MyLocationListener();
				String bestProvider = mlocManager.getBestProvider(new Criteria(),false);
				Log.i("best", ""+bestProvider);
				if(bestProvider != null)
					mlocManager.requestLocationUpdates(bestProvider, 0, 0,
						mlocListener);
				service = HTTPModuleFacade.getInstance("1", latitude+"", longitude+"");
				// checa as atualizações necessárias,
				// depois passa para a tela de menu
				Intent principal = new Intent(LoadingActivity.this,	MenuActivity.class);
				LoadingActivity.this.startActivity(principal);
				LoadingActivity.this.finish();
			  }
		  }
		
		};
		
		/* Class My Location Listener */
//best
		public class MyLocationListener implements LocationListener

		{

			@Override
			public void onLocationChanged(Location loc){

				double latitude = loc.getLatitude();
				double longitude = loc.getLongitude();
				Log.i("Localização mudou", latitude +","+longitude);
				Log.i("user", service.getUser().getLatitude() +","+service.getUser().getLongitude());
				if(service != null && service.getUser().getLatitude().equals("0.0") && service.getUser().getLongitude().equals("0.0")){
					service.getUser().setLatitude(""+latitude);
					service.getUser().setLongitude(""+longitude);
					service.updateUserAddress();
					Log.i("Localização atualizada", latitude +","+longitude);				
				}
			}

			@Override
			public void onProviderDisabled(String provider)	{
				Toast.makeText(getApplicationContext(),	"Localização desabilitada",	Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onProviderEnabled(String provider){
				Toast.makeText(getApplicationContext(),"Localização habilitada",Toast.LENGTH_SHORT).show();

			}

			@Override
			public void onStatusChanged(String provider, int status, Bundle extras){}

		}/* End of Class MyLocationListener */
	
		
		
	@Override
	protected void onResume(){
		super.onResume();
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
}
