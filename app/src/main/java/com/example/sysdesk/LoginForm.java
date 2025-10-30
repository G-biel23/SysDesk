package com.example.sysdesk;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

        // Inicializa componentes
        IniciarComponentes();

        // Aplica filtro de letras e números na senha
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


        // Link para tela de cadastro
        text_tela_cadastro.setOnClickListener(v -> {
            Intent intent = new Intent(LoginForm.this, CadastroForm.class);
            startActivity(intent);
        });

        // Botão de login
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

            // Chamada de login via API
            Call<Usuario> call = api.login(usuarioLogin);
            call.enqueue(new Callback<Usuario>() {
                @Override
                public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                    if(response.isSuccessful() && response.body() != null) {
                        Usuario usuarioLogado = response.body();
                        Toast.makeText(LoginForm.this, "Bem-vindo, " + usuarioLogado.getNome(), Toast.LENGTH_SHORT).show();

                        // Redireciona para Activity de acordo com tipo
                        Intent intent;
                        switch(usuarioLogado.getTipo().toLowerCase()) {
                            case "cliente":
                                intent = new Intent(LoginForm.this, ClienteHomeForm.class);
                                break;
                            case "tecnico":
                                intent = new Intent(LoginForm.this, TecnicoHomeForm.class);
                                break;
                            case "admin":
                                intent = new Intent(LoginForm.this, AdminHomeForm.class);
                                break;
                            default:
                                Toast.makeText(LoginForm.this, "Tipo de usuário desconhecido", Toast.LENGTH_SHORT).show();
                                return;
                        }

                        startActivity(intent);
                        finish();

                    } else if(response.code() == 401) {
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
        });
    }

    private void IniciarComponentes() {
        api = RetrofitClient.getRetrofitInstance().create(SysDeskApi.class);
        text_tela_cadastro = findViewById(R.id.text_tela_cadastro);
        etEmail = findViewById(R.id.edit_email);
        etSenha = findViewById(R.id.edit_senha);
        btnLogin = findViewById(R.id.login_button);
    }
}
