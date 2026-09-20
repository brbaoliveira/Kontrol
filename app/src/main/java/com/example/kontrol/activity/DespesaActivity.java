package com.example.kontrol.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.kontrol.R;
import com.example.kontrol.database.DBFormaPagamento;
import com.example.kontrol.model.FormaPagamento;
import com.example.kontrol.model.Spinner;

import java.util.ArrayList;
import java.util.List;

public class DespesaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_despesa);

        AutoCompleteTextView spinner = findViewById(R.id.despesa_actv_forma_pagamento);

        List<FormaPagamento> listaFormaPagamento = new DBFormaPagamento(getApplicationContext()).recuperaListaFormaPagamentos();
        String[] nomes = new String[listaFormaPagamento.size()];
        for (int p = 0; p < listaFormaPagamento.size(); p++) {
            FormaPagamento formaPagamento = listaFormaPagamento.get(p);
            nomes[p] = formaPagamento.getNome();
        }
        Spinner.spinner(this, nomes, spinner, getLayoutInflater());

    }
}