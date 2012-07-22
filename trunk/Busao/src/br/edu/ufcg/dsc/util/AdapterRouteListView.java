package br.edu.ufcg.dsc.util;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import br.edu.ufcg.dsc.R;

public class AdapterRouteListView extends BaseAdapter {

    private LayoutInflater mInflater;
    private ArrayList<RouteListView> itens;
    private ImageView removeRoute, mapsRoute;

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
        TextView nomeRota = ((TextView) view.findViewById(R.id.list_routeName));
        nomeRota.setText(item.getRoutename());
        
        TextView numOnibus = ((TextView) view.findViewById(R.id.list_num));
        numOnibus.setText(numOnibus.getText() + " " + item.getNumBus());
        
        TextView difOnibus = ((TextView) view.findViewById(R.id.list_dif));
        difOnibus.setText(difOnibus.getText() + " " + item.getDifBetweenBus() + " min");
        
        TextView horaInicio = ((TextView) view.findViewById(R.id.list_h1));
        if (item.getStartTime().length() > 0){
        	horaInicio.setText(horaInicio.getText() + " " + item.getStartTime().substring(0, item.getStartTime().length() - 3) + "h");
        } else {
        	horaInicio.setText(horaInicio.getText() + " " + item.getStartTime());        	
        }
        
        TextView horaFim = ((TextView) view.findViewById(R.id.list_hf));
        if (item.getEndTime().length() > 0){
        	horaFim.setText(horaFim.getText() + " " + item.getEndTime().substring(0, item.getEndTime().length() - 3) + "h");
        } else {
        	horaFim.setText(horaFim.getText() + " " + item.getEndTime());
        }

        return view;
    }

}
