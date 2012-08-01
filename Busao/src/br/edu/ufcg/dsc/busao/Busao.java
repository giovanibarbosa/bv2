package br.edu.ufcg.dsc.busao;

import br.com.indigo.android.facebook.SocialFacebook;
import android.app.Application;

public class Busao extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		String[] permissions = {"publish_stream"}; // App permissions
		SocialFacebook.getInstance(this, "151638851626681", "66758c81845cf49a4f243d8a0c74ab61", permissions);
		//SocialFacebook.getInstance(this, "151638851626681", "66758c81845cf49a4f243d8a0c74ab61", permissions);
	}
}