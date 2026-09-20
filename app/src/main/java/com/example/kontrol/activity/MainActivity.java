package com.example.kontrol.activity;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kontrol.database.DBUsuarioLogado;
import com.example.kontrol.fragment.FinancasFragment;
import com.example.kontrol.fragment.FolhaPontoFragment;
import com.example.kontrol.fragment.HomeFragment;
import com.example.kontrol.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        boolean verificaUsuarioLogado = new DBUsuarioLogado(getApplication()).recuperaListaUsuarios() != null;
        if (!verificaUsuarioLogado) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        configuraBottomNavigationView();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.viewPager, new HomeFragment()).commit();

    }
    private void configuraBottomNavigationView(){

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setLabelVisibilityMode(
                NavigationBarView.LABEL_VISIBILITY_UNLABELED
        );
        habilitarNavegacao(bottomNavigationView);
    }
    private void habilitarNavegacao(BottomNavigationView bottomNavigationView){
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                if(menuItem.getItemId() == R.id.ic_home){
                    fragmentTransaction.replace(R.id.viewPager, new HomeFragment()).commit();
                    return true;
                }if(menuItem.getItemId() == R.id.ic_financas){
                    fragmentTransaction.replace(R.id.viewPager, new FinancasFragment()).commit();
                    return true;
                }if(menuItem.getItemId() == R.id.ic_ponto){
                    fragmentTransaction.replace(R.id.viewPager, new FolhaPontoFragment()).commit();
                    return true;
                }if(menuItem.getItemId() == R.id.ic_perfil){
                    fragmentTransaction.replace(R.id.viewPager, new FolhaPontoFragment()).commit();
                    return true;
                }
                return false;
            }
        });
    }

}