package com.example.kontrol.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.kontrol.API.ApiService;
import com.example.kontrol.API.RetrofitClient;
import com.example.kontrol.R;
import com.example.kontrol.database.DBUsuario;
import com.example.kontrol.model.Usuario;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CadastroActivity extends AppCompatActivity {

    private TextInputEditText tiedUser, tiedNome, tiedEmail, tiedData, tiedSenha;
    private TextInputLayout tilUser;
    private TextView tvAviso, tvVerificacaoAprovada, tvVerificacaoReprovada;
    private TextInputLayout tilDataCalendario;
    private TextView tvLogin;
    private Button btCadastrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cadastro);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        inic();
        configClique();
        configuraDataNasc();

    }

    public void abrirTelaLogin(){
        Intent intent = new Intent(CadastroActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void inic(){
        tvAviso                = findViewById(R.id.cadastro_tv_aviso);
        tvVerificacaoAprovada  = findViewById(R.id.cadastro_tv_aviso_aceito);
        tvVerificacaoReprovada = findViewById(R.id.cadastro_tv_aviso_erro);
        tilUser                = findViewById(R.id.cadastro_til_usuario);
        tiedUser               = findViewById(R.id.cadastro_tied_usuario);
        tiedNome               = findViewById(R.id.cadastro_tied_nome);
        tiedEmail              = findViewById(R.id.cadastro_tied_email);
        tiedData               = findViewById(R.id.cadastro_tied_data_nasc);
        tiedSenha              = findViewById(R.id.cadastro_tied_senha);
        btCadastrar            = findViewById(R.id.cadastro_bt_cadastrar);
        tvLogin                = findViewById(R.id.cadastro_tv_login);
    }

    private void configClique() {
        tiedUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String texto = String.valueOf(s);
                Usuario usuario = new DBUsuario(getApplicationContext()).recuperaUsuario(DBUsuario.FLD_USER + " = ?", new String[]{texto});
                if (!texto.trim().isEmpty()) {
                    if (usuario == null) {
                        // Aplica Verde (#00FF00) para disponível
                        int corVerde = ContextCompat.getColor(getApplicationContext(), R.color.verde_kontrol);
                        tilUser.setStartIconTintList(ColorStateList.valueOf(corVerde));
                        tilUser.setBoxStrokeColor(corVerde);
                        tilUser.setHintTextColor(ColorStateList.valueOf(corVerde));
                    } else {
                        // Aplica Vermelho (#FF0000) para ocupado
                        int corVermelho = ContextCompat.getColor(getApplicationContext(), R.color.vermelho_kontrol);
                        tilUser.setHintTextColor(ColorStateList.valueOf(corVermelho));
                        tilUser.setStartIconTintList(ColorStateList.valueOf(corVermelho));
                        tilUser.setBoxStrokeColor(corVermelho);
                    }
                    tvVerificacaoAprovada.setVisibility(usuario != null ? View.GONE : View.VISIBLE);
                    tvVerificacaoReprovada.setVisibility(usuario == null ? View.GONE : View.VISIBLE);
                }
            }
        });
        btCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadastrarUsuario();
            }
        });
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirTelaLogin();
            }
        });
    }

    private void configuraDataNasc(){
        tiedData.setInputType(InputType.TYPE_CLASS_NUMBER);
        tiedData.addTextChangedListener(new TextWatcher() {
            private boolean isUpdating;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isUpdating) return;

                isUpdating = true;

                String clean = s.toString().replaceAll("[^\\d]", "");

                if (clean.length() > 8) {
                    clean = clean.substring(0, 8);
                }

                StringBuilder formatted = new StringBuilder();

                for (int i = 0; i < clean.length(); i++) {
                    formatted.append(clean.charAt(i));

                    if ((i == 1 || i == 3) && i != clean.length() - 1) {
                        formatted.append("/");
                    }
                }

                tiedData.setText(formatted.toString());
                tiedData.setSelection(formatted.length());

                isUpdating = false;
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        View.OnClickListener abrirCalendario = v -> {

            Calendar calendar = Calendar.getInstance();

            DatePickerDialog dialog = new DatePickerDialog(
                    CadastroActivity.this,
                    (view, year, month, day) -> {

                        String data = String.format("%02d/%02d/%d", day, month + 1, year);
                        tiedData.setText(data);

                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );

            dialog.show();
        };
        tilDataCalendario = findViewById(R.id.cadastro_til_data_nasc);
        tilDataCalendario.setEndIconOnClickListener(abrirCalendario);
    }

    private String converterData(String dataBr) {
        try {
            String[] partes = dataBr.split("/");
            return partes[2] + "-" + partes[1] + "-" + partes[0];
        } catch (Exception e) {
            return "";
        }
    }

    private void cadastrarUsuario() {
        String user = Objects.requireNonNull(tiedUser.getText()).toString().trim();
        String nome = Objects.requireNonNull(tiedNome.getText()).toString().trim();
        String email = Objects.requireNonNull(tiedEmail.getText()).toString().trim();
        String senha = Objects.requireNonNull(tiedSenha.getText()).toString().trim();
        String dataNasc = Objects.requireNonNull(tiedData.getText()).toString().trim();
        String dataFormatada = converterData(dataNasc);

        if (!user.isEmpty()) {
            if (!nome.isEmpty()) {
                if (!email.isEmpty()) {
                    if (!dataNasc.isEmpty()) {
                        if (!senha.isEmpty()) {
                            Usuario usuario = new Usuario(null, user, nome, email, dataFormatada, senha);
                            if (inserirDBUsuario(usuario)) {
                                startActivity(new Intent(CadastroActivity.this, LoginActivity.class));
                                finish();
                            }
                        } else {
                            verficaAviso(tiedSenha, getString(R.string.CADASTRO_SENHA));
                        }
                    } else {
                        verficaAviso(tiedData, getString(R.string.CADASTRO_DATA_NASC));
                    }
                } else {
                    verficaAviso(tiedEmail, getString(R.string.CADASTRO_EMAIL));
                }
            } else {
                verficaAviso(tiedNome, getString(R.string.CADASTRO_NOME));
            }
        }else {
            verficaAviso(tiedUser, getString(R.string.CADASTRO_USUARIO));
        }
    }

    private void verficaAviso(TextInputEditText tied, String campo) {
        tvAviso.setText(getString(R.string.AVISO_CAMPO_VAZIO, campo));
        tvAviso.setVisibility(View.VISIBLE);

        tied.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvAviso.setVisibility(View.GONE);
            }
        });
    }


    private boolean inserirDBUsuario(Usuario usuario) {
        DBUsuario dbUsuario = new DBUsuario(this);
        //boolean resultado = db.inserirUsuario(nome, email, dataNasc, senha);
        boolean resultado = dbUsuario.inserir(usuario);

        if(resultado) {
            Toast.makeText(CadastroActivity.this,
                    "Usuário cadastrado!",
                    Toast.LENGTH_SHORT).show();
            inserirAPIUsuario(usuario);

        } else {
            Toast.makeText(CadastroActivity.this,
                    "Erro ao cadastrar",
                    Toast.LENGTH_SHORT).show();
        }
        return resultado;
    }

    private void inserirAPIUsuario(Usuario usuario) {
        ApiService api = RetrofitClient.getApi();

        api.criarUsuario(usuario).enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CadastroActivity.this, "Cadastrado com sucesso!", Toast.LENGTH_SHORT).show();

                    tiedNome.setText("");
                    tiedEmail.setText("");
                    tiedData.setText("");
                    tiedSenha.setText("");

                    abrirTelaLogin();
                } else {
                    Toast.makeText(CadastroActivity.this, "Erro ao cadastrar", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Toast.makeText(CadastroActivity.this, "Erro: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}