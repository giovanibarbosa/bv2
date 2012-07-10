package br.edu.ufcg.dsc.busao;

import br.edu.ufcg.dsc.R;
import br.edu.ufcg.dsc.httpmodule.HTTPModuleFacade;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

public class LoadingActivity extends Activity {
	private HTTPModuleFacade service;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loading);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		//pegar gps
		service = HTTPModuleFacade.getInstance("1", "0", "0");
		// checa as atualizações necessárias,
		// depois passa para a tela de menu
		Intent principal = new Intent(LoadingActivity.this,	MenuActivity.class);
		LoadingActivity.this.startActivity(principal);
		LoadingActivity.this.finish();

		
		
//		Intent telaConsultar = new Intent(LoadingActivity.this, ResultadoActivity.class);
//		Bundle b = new Bundle();
//		b.putString("paramBusca", "202");
//		telaConsultar.putExtras(b);				
//		startActivity(telaConsultar);

	}
	
	@Override
	protected void onResume(){
		super.onResume();
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
}