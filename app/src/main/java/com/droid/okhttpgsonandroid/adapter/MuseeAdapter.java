package com.droid.okhttpgsonandroid.adapter;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.droid.okhttpgsonandroid.JSonObject;
import com.droid.okhttpgsonandroid.R;

import java.util.List;

/**
 * Created by jean on 11/07/2017.
 */

public class MuseeAdapter extends ArrayAdapter{

    private Context mContext;

    //Constructeur

    public MuseeAdapter(@NonNull Context context, @NonNull List<JSonObject.RecordsBean> musees) {
        super(context, R.layout.single_item,R.id.tvNomMusee,musees);
        mContext=context;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //Construire la view
        LayoutInflater inflater=(LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.single_item,parent,false);


        //récupere le textview qui affiche le nom du musee
        TextView tvNomMusee=(TextView) view.findViewById(R.id.tvNomMusee);

        //Récuperer le RecordsBean à la position "position"
        JSonObject.RecordsBean musee=(JSonObject.RecordsBean) getItem(position);

        //Lire le nom du musée
        String nomDuMusee=musee.getFields().getNom_du_musee();

        //Et l'affecter au textView
        tvNomMusee.setText(nomDuMusee);

        return view;
    }
}
