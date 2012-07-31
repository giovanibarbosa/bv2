package br.edu.ufcg.dsc.util;

import java.util.List;

import com.ecs.sample.TwitterUtils;

import br.com.indigo.android.facebook.SocialFacebook;
import br.com.indigo.android.facebook.SocialFacebook.NewObjectListener;
import br.com.indigo.android.facebook.models.FbSimplePost;
import br.edu.ufcg.dsc.R;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PontoAdapter extends BaseAdapter {
	private List<PontoTuristico> pontos;
	private LayoutInflater mInflater;
	private ViewHolder holder;
	private Context context;

	static class ViewHolder {
		private TextView texto;
		private ImageView imagem;
		private ImageView face, twitter;
	}

	public PontoAdapter(Activity activity, List<PontoTuristico> pontos) {
		this.context = activity;
		mInflater = LayoutInflater.from(context);
		this.pontos = pontos;
	}

	@Override
	public int getCount() {
		return pontos.size();
	}

	@Override
	public Object getItem(int index) {
		return pontos.get(index);
	}

	@Override
	public long getItemId(int index) {
		// return pessoas(index).getImgRes();
		return index;
	}

	@Override
	public View getView(int posicao, View convertView, ViewGroup arg2) {

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.ponto_adapter_item, null);
			holder = new ViewHolder();

			holder.texto = (TextView) convertView.findViewById(R.id.text1);
			holder.imagem = (ImageView) convertView.findViewById(R.id.image1);
			holder.face = (ImageView) convertView.findViewById(R.id.botaoFace);
			holder.twitter = (ImageView) convertView
					.findViewById(R.id.botaoTwitter);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		PontoTuristico p = pontos.get(posicao);

		holder.texto.setText(p.getDescricao());
		holder.imagem.setImageResource(p.getImagem());

		holder.face.setOnClickListener(new FaceClickListener(p));
		holder.twitter.setOnClickListener(new TwitterClickListener(p));

		return convertView;
	}

	private class FaceClickListener implements OnClickListener {
		private PontoTuristico ponto;

		public FaceClickListener(PontoTuristico ponto) {
			this.ponto = ponto;
		}

		@Override
		public void onClick(View v) {
		    FbSimplePost post = new FbSimplePost();
			post.setName("Eu conheci o " + ponto.getNome() + " pelo Busão!");
			post.setCaption("busaoapp.com");
			post.setDescription("O Busão é um aplicativo para Android, que fornece ao usuário o itinerário de rotas de ônibus de algumas cidades.");
			post.setLink("http://www.busaoapp.com");
			//post.setPicture("./res/drawable-hdpi/icon.png");
			post.setActionName("Website do Busão");
			post.setActionLink("http://www.busaoapp.com");
			post.setMessage("Mensagem criado por Busão");
			 
			SocialFacebook.getInstance().publish((Activity)context, post, new NewObjectListener(){

				@Override
				public void onFail(Throwable thr) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onCancel() {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onComplete(String id) {
					Toast.makeText(context, "Post enviado!", Toast.LENGTH_LONG).show();
					
				}
				
			});
			
		}

	}

	private class TwitterClickListener implements OnClickListener {
		private PontoTuristico ponto;
		
		private SharedPreferences prefs;

		public TwitterClickListener(PontoTuristico ponto) {
			this.ponto = ponto;
			this.prefs = PreferenceManager.getDefaultSharedPreferences(context);
		}

		@Override
		public void onClick(View v) {
			try{
			    Intent intentTwitter = new Intent(Intent.ACTION_SEND);
			    String tweet = "Eu conheci o " + ponto.getNome() + " pelo @busaoapp !!!";
			    intentTwitter.putExtra(Intent.EXTRA_TEXT,tweet);
			    intentTwitter.setType("application/twitter");
			    if (isIntentAvailable(v.getContext(),"application/twitter")){
			    	v.getContext().startActivity(Intent.createChooser(intentTwitter,"Share with:"));
			    } else {
			       Toast.makeText(v.getContext(), "Cant twett", Toast.LENGTH_LONG).show();   
			    }
				
			} catch(Exception e) {}

		}
		
		public boolean isIntentAvailable(Context context, String action) {
	        final PackageManager packageManager = context.getPackageManager();
	        final Intent intent = new Intent(action);
	        List<ResolveInfo> list =  packageManager.queryIntentActivities(intent,PackageManager.MATCH_DEFAULT_ONLY);
	        return list.size() > 0;
	    }

	}
}
