package com.example.kontrol.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.kontrol.R;
import com.example.kontrol.database.DBUsuario;
import com.example.kontrol.database.DBUsuarioLogado;
import com.example.kontrol.model.Usuario;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {
    private TextInputEditText tiedUsuario, tiedEmail, tiedData, tiedSenha;
    private TextInputLayout tilDataCalendario;
    private TextView tvAviso;
    private Button btEntrar;

    TextView tvCadastrar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tiedUsuario = findViewById(R.id.login_tied_usuario);
        tiedSenha = findViewById(R.id.login_tied_senha);
        tvAviso = findViewById(R.id.login_tv_aviso);
        btEntrar = findViewById(R.id.login_bt_entrar);
        tvCadastrar = findViewById(R.id.login_tv_fazer_cadastro);
        tvAviso.setVisibility(View.GONE);

        btEntrar.setOnClickListener(v -> {
            DBUsuario dbUsuario = new DBUsuario(this);
            String login = tiedUsuario.getText() == null ? null : String.valueOf(tiedUsuario.getText()).trim();
            String senha = tiedSenha.getText() == null ? null : String.valueOf(tiedSenha.getText()).trim();
            if (login != null && !login.isEmpty()) {
                if (senha != null && !senha.isEmpty()) {
                    Usuario usuario = dbUsuario.recuperaUsuario(DBUsuario.FLD_USER + " = ? AND " + DBUsuario.FLD_SENHA + " = ?", new String[]{ login, senha });
                    if (usuario != null && usuario.getId() != null) {
                        if (new DBUsuarioLogado(getApplicationContext()).inserir(usuario)) {
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        }
                    } else {
                        tvAviso.setText(getString(R.string.LOGIN_AVISO_INVALIDO));
                        tvAviso.setVisibility(View.VISIBLE);
                    }
                } else {
                    tvAviso.setText(getString(R.string.AVISO_CAMPO_VAZIO, getString(R.string.LOGIN_SENHA)));
                    tvAviso.setVisibility(View.VISIBLE);
                }
            }else {
                tvAviso.setText(getString(R.string.AVISO_CAMPO_VAZIO, getString(R.string.LOGIN_USUARIO)));
                tvAviso.setVisibility(View.VISIBLE);
            }
        });

        tvCadastrar.setOnClickListener(V -> {
            startActivity(new Intent(LoginActivity.this, CadastroActivity.class));
            finish();
        });
    }


}