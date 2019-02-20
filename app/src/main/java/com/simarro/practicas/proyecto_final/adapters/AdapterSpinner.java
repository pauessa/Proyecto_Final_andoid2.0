package com.simarro.practicas.proyecto_final.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.simarro.practicas.proyecto_final.pojo.Autor;

import java.util.ArrayList;
import java.util.List;

public class AdapterSpinner extends ArrayAdapter<Autor> {

    // Your sent context
    private Context context;
    // Your custom values for the spinner (User)
    private List<Autor> values;

    public AdapterSpinner(Context context, int textViewResourceId,
                          List<Autor> values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public int getCount(){
        return values.size();
    }

    @Override
    public Autor getItem(int position){
        return values.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setTextSize(18);
        label.setText(values.get(position).getNombre());

        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setTextSize(18);
        label.setPadding(20,20,20,20);
        label.setText(values.get(position).getNombre());

        return label;
    }
}