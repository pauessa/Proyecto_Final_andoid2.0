package com.simarro.practicas.proyecto_final.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.simarro.practicas.proyecto_final.pojo.Editorial;

import java.util.List;

public class AdapterSpinnerEditorial extends ArrayAdapter<Editorial> {

    // Your sent context
    private Context context;
    // Your custom values for the spinner (User)
    private List<Editorial> values;

    public AdapterSpinnerEditorial(Context context, int textViewResourceId,
                                   List<Editorial> values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public int getCount(){
        return values.size();
    }

    @Override
    public Editorial getItem(int position){
        return values.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }


    // And the "magic" goes here
    // This is for the "passive" state of the spinner
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