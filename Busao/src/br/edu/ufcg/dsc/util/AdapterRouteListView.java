package br.edu.ufcg.dsc.util;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import br.edu.ufcg.dsc.R;

public class AdapterRouteListView extends BaseAdapter {

    private LayoutInflater mInflater;
    private ArrayList<RouteListView> itens;

    public AdapterRouteListView(Context context, ArrayList<RouteListView> itens) {
        this.itens = itens; //Itens que preencheram o listview
        mInflater = LayoutInflater.from(context);  //responsavel por pegar o Layout do item.
    }

    @Override
    public int getCount() {
        return itens.size();
    }

    @Override
    public RouteListView getItem(int position) {
        return itens.get(position);
    }

    @Override
    public long getItemId(int position) {  //Sem implementação
        return position;
    }


    public View getView(int position, View view, ViewGroup parent) {
    	RouteListView item = itens.get(position); //Pega o item de acordo com a posicao.
        view = mInflater.inflate(R.layout.favourite_route, null); //infla o layout para podermos preencher os dados

        //atraves do layout pego pelo LayoutInflater, pegamos cada id relacionado ao item e definimos as informações.
        ((TextView) view.findViewById(R.id.list_routeName)).setText(item.getRoutename() + " - " + item.getColour());
        ((TextView) view.findViewById(R.id.list_num)).setText(R.string.num_onibus + " " + item.getNumBus());
        ((TextView) view.findViewById(R.id.list_dif)).setText(R.string.dif_onibus + " " + item.getDifBetweenBus());
        ((TextView) view.findViewById(R.id.list_h1)).setText(R.string.hora_inicio + " " + item.getStartTime());
        ((TextView) view.findViewById(R.id.list_hf)).setText(R.string.hora_fim + " " + item.getEndTime());
        
        return view;
    }

}
