package com.example.sysdesk.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Base64;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sysdesk.R;
import com.example.sysdesk.api.SysDeskApi;
import com.example.sysdesk.model.Usuario;
import com.example.sysdesk.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginForm extends AppCompatActivity {

    private TextView text_tela_cadastro;
    private EditText etEmail, etSenha;
    private Button btnLogin;
    private SysDeskApi api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_form);

        IniciarComponentes();
        aplicarFiltroSenha();
        configurarEventos();
    }

    private void IniciarComponentes() {
        api = RetrofitClient.getRetrofitInstance().create(SysDeskApi.class);
        text_tela_cadastro = findViewById(R.id.text_tela_cadastro);
        etEmail = findViewById(R.id.edit_email);
        etSenha = findViewById(R.id.edit_senha);
        btnLogin = findViewById(R.id.login_button);
    }

    private void aplicarFiltroSenha() {
        InputFilter senhaFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    char c = source.charAt(i);
                    if (!Character.isLetterOrDigit(c)) return "";
                }
                return null;
            }
        };
        etSenha.setFilters(new InputFilter[]{senhaFilter, new InputFilter.LengthFilter(25)});

        etSenha.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2;
            if(event.getAction() == MotionEvent.ACTION_UP) {
                if(event.getRawX() >= (etSenha.getRight() - etSenha.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    if(etSenha.getInputType() == (android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                        etSenha.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    } else {
                        etSenha.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    }
                    etSenha.setSelection(etSenha.getText().length());
                    return true;
                }
            }
            return false;
        });
    }

    private void configurarEventos() {
        text_tela_cadastro.setOnClickListener(v -> {
            Intent intent = new Intent(LoginForm.this, CadastroForm.class);
            startActivity(intent);
        });

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String senha = etSenha.getText().toString().trim();

            if(email.isEmpty() || senha.isEmpty()) {
                Toast.makeText(LoginForm.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                return;
            }

            Usuario usuarioLogin = new Usuario();
            usuarioLogin.setEmail(email);
            usuarioLogin.setSenha(senha);

            realizarLogin(usuarioLogin);
        });
    }

    private void realizarLogin(Usuario usuarioLogin) {

        Call<Usuario> call = api.login(usuarioLogin);
        call.enqueue(new Callback<Usuario>() {

            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful() && response.body() != null) {

                    Usuario usuarioLogado = response.body();

                    // ===============================
                    //   GERAR E SALVAR BASIC TOKEN
                    // ===============================

                    String rawCredentials = usuarioLogin.getEmail() + ":" + usuarioLogin.getSenha();
                    String basicToken = Base64.encodeToString(rawCredentials.getBytes(), Base64.NO_WRAP);

                    // Salvar no Retrofit
                    RetrofitClient.setToken(basicToken);

                    // Salvar localmente para manter login
                    getSharedPreferences("sysdesk_prefs", MODE_PRIVATE)
                            .edit()
                            .putString("TOKEN", basicToken)
                            .apply();

                    Toast.makeText(LoginForm.this, "Bem-vindo, " + usuarioLogado.getNome(), Toast.LENGTH_SHORT).show();

                    // Redirecionamento
                    redirecionarPorTipo(usuarioLogado.getTipo());

                } else if (response.code() == 401) {
                    Toast.makeText(LoginForm.this, "Email ou senha inválidos", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginForm.this, "Erro: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Toast.makeText(LoginForm.this, "Falha na conexão: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void redirecionarPorTipo(String tipo) {
        Intent intent;
        switch (tipo.toLowerCase()) {
            case "cliente":
                intent = new Intent(this, ClienteHomeForm.class);
                break;
            case "tecnico":
                intent = new Intent(this, TecnicoHomeForm.class);
                break;
            case "admin":
                intent = new Intent(this, AdminHomeForm.class);
                break;
            default:
                Toast.makeText(this, "Tipo de usuário desconhecido", Toast.LENGTH_SHORT).show();
                return;
        }
        startActivity(intent);
        finish();
    }
}
