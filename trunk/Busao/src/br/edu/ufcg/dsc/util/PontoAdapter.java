package br.edu.ufcg.dsc.util;

import java.util.List;

import br.edu.ufcg.dsc.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PontoAdapter extends BaseAdapter {
	private List<PontoTuristico> pontos;
	private LayoutInflater mInflater;
	private ViewHolder holder;
 
 
	static class ViewHolder{
		private TextView texto;
		private ImageView imagem;
	}
 
 
	public PontoAdapter(Context context, List<PontoTuristico> pontos) {
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
		//return pessoas(index).getImgRes();
		return index;
	}
 
	@Override
	public View getView(int posicao, View convertView, ViewGroup arg2) {
 
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.ponto_adapter_item, null);
			holder = new ViewHolder();
 
			holder.texto = (TextView) convertView.findViewById(R.id.text1);
			holder.imagem = (ImageView) convertView.findViewById(R.id.image1);
 
			convertView.setTag(holder);
 
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
 
 
		PontoTuristico p = pontos.get(posicao);
 
		holder.texto.setText(p.getTexto());
		holder.imagem.setImageResource(p.getImagem());
 
		return convertView;
	}
	
}
 