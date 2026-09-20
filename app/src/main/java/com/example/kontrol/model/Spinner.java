package com.example.kontrol.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.kontrol.R;

public class Spinner {

    public static void spinner (Context ctx, String[] nomes, AutoCompleteTextView spinner, LayoutInflater getLayoutInflater){
    ArrayAdapter<String> adapter = new ArrayAdapter<String>(ctx, R.layout.item_dropdown, nomes) {

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return createView(position, convertView, parent);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return createView(position, convertView, parent);
        }

        private View createView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater.inflate(R.layout.item_dropdown, parent, false);
            }

            TextView text = convertView.findViewById(R.id.text);
            ImageView icon = convertView.findViewById(R.id.icon);

            text.setText(nomes[position]);
            icon.setVisibility(View.GONE);

            return convertView;
        }
    };

        spinner.setAdapter(adapter);
        spinner.setDropDownBackgroundDrawable(
                ContextCompat.getDrawable(ctx, R.drawable.spinner_adapter_background)
        );

    }

    public static void spinner (Context ctx, String[] nomes, int[] imagens, AutoCompleteTextView spinner, LayoutInflater getLayoutInflater){
    /*int[] imagens = {
                R.drawable.despesa,
                R.drawable.despesas,
                R.drawable.dinheiro,
                R.drawable.ic_almoco_entrada_24,
                R.drawable.ic_almoco_volta_24
        };*/

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ctx, R.layout.item_dropdown, nomes) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                return createView(position, convertView, parent);
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                return createView(position, convertView, parent);
            }

            private View createView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = getLayoutInflater.inflate(R.layout.item_dropdown, parent, false);
                }

                TextView text = convertView.findViewById(R.id.text);
                ImageView icon = convertView.findViewById(R.id.icon);

                text.setText(nomes[position]);
                icon.setImageResource(imagens[position]);

                return convertView;
            }
        };

        spinner.setAdapter(adapter);
        spinner.setDropDownBackgroundDrawable(
                ContextCompat.getDrawable(ctx, R.drawable.spinner_adapter_background)
        );

    }
}
