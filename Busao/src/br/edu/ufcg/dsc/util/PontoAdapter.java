package br.edu.ufcg.dsc.util;

import java.util.List;

import com.ecs.sample.TwitterUtils;

import br.edu.ufcg.dsc.R;

import br.edu.ufcg.dsc.busao.AuthTwitterActivity;

import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
			// Ver como postar mensagem no face
			Toast.makeText(context, "Face: " + "Eu conheci o " + ponto.getNome() + " pelo Busão!", Toast.LENGTH_LONG).show();
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
			if (!TwitterUtils.isAuthenticated(prefs)) {
				context.startActivity(new Intent().setClass(v.getContext(),
						AuthTwitterActivity.class));
			}
			String tweet = "Eu conheci o " + ponto.getNome() + " pelo @busaoapp !!!";
			try {
				TwitterUtils.sendTweet(prefs, tweet);
				Toast.makeText(context,	"Tweet enviado!", Toast.LENGTH_LONG).show();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}
}
