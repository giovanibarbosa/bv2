package br.edu.ufcg.dsc.busao;

import br.edu.ufcg.dsc.R;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

public class LocalidadeActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu_localidade);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
}
