package com.example.kontrol.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kontrol.R;
import com.example.kontrol.activity.DespesaActivity;
import com.example.kontrol.activity.ReceitaActivity;
import com.github.clans.fab.FloatingActionMenu;
import com.github.clans.fab.FloatingActionButton;

public class FinancasFragment extends Fragment {
    private FloatingActionMenu menu;
    private FloatingActionButton menuReceita, menuDespesa;
    public FinancasFragment() {
        // construtor vazio obrigatório
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_financas, container, false);

        menu = view.findViewById(R.id.menu);
        menuReceita = view.findViewById(R.id.menu_receita);
        menuDespesa = view.findViewById(R.id.menu_despesa);

        // clique menu principal
        menu.setOnClickListener(v -> { });

        menuDespesa.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), DespesaActivity.class));
        });

        menuReceita.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), ReceitaActivity.class));
        });

        return view;
    }
}