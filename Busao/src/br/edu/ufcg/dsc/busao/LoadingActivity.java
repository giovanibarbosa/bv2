package br.edu.ufcg.dsc.busao;

import br.edu.ufcg.dsc.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class LoadingActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loading);
		// checa as atualizações necessárias,
		// depois passa para a tela de menu

		Intent menuPrincipal = new Intent(LoadingActivity.this,
				MenuActivity.class);
		LoadingActivity.this.startActivity(menuPrincipal);
		LoadingActivity.this.finish();
	}
}