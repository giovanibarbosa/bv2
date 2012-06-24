package br.edu.ufcg.dsc.busao;

import br.edu.ufcg.dsc.R;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

public class LoadingActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loading);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		// checa as atualizações necessárias,
		// depois passa para a tela de menu
		Intent principal = new Intent(LoadingActivity.this,	MenuActivity.class);
		LoadingActivity.this.startActivity(principal);
		LoadingActivity.this.finish();

	}
	
	@Override
	protected void onResume(){
		super.onResume();
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
}