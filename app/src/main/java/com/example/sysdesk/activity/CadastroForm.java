package com.example.sysdesk.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.InputType;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sysdesk.R;
import com.example.sysdesk.api.SysDeskApi;
import com.example.sysdesk.model.Usuario;
import com.example.sysdesk.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CadastroForm extends AppCompatActivity {

    private EditText editNomeCadastro, editEmailCadastro, editSenhaCadastro, editContato;
    private Button btnCadastrar;
    private SysDeskApi api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_form);

        // Inicializa campos e botão
        editNomeCadastro = findViewById(R.id.edit_nomeCadastro);
        editEmailCadastro = findViewById(R.id.edit_emailCadastro);
        editSenhaCadastro = findViewById(R.id.edit_senhaCadastro);
        editContato = findViewById(R.id.edit_contato);
        btnCadastrar = findViewById(R.id.cadastro_button);

        // Inicializa API
        api = RetrofitClient.getRetrofitInstance().create(SysDeskApi.class);

        // Máscara de telefone
        editContato.addTextChangedListener(new TextWatcher() {
            private boolean isUpdating = false;
            private final String mask = "(##) #####-####";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isUpdating) { isUpdating = false; return; }

                String digits = s.toString().replaceAll("[^0-9]", "");
                StringBuilder masked = new StringBuilder();
                int i = 0;
                for (char m : mask.toCharArray()) {
                    if (m == '#') {
                        if (i < digits.length()) masked.append(digits.charAt(i++));
                        else break;
                    } else {
                        if (i < digits.length()) masked.append(m);
                        else break;
                    }
                }
                isUpdating = true;
                editContato.setText(masked.toString());
                editContato.setSelection(masked.length());
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Filtro: apenas letras e espaços no nome
        InputFilter letrasFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    if (!Character.isLetter(source.charAt(i)) && !Character.isWhitespace(source.charAt(i))) {
                        return "";
                    }
                }
                return null;
            }
        };
        editNomeCadastro.setFilters(new InputFilter[]{letrasFilter, new InputFilter.LengthFilter(100)});

        // Filtro: apenas letras e números na senha
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
        editSenhaCadastro.setFilters(new InputFilter[]{senhaFilter, new InputFilter.LengthFilter(25)});

        // 👁 Mostrar/ocultar senha
        editSenhaCadastro.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (editSenhaCadastro.getRight() -
                        editSenhaCadastro.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    if (editSenhaCadastro.getInputType() ==
                            (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                        editSenhaCadastro.setInputType(
                                InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    } else {
                        editSenhaCadastro.setInputType(
                                InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    }
                    editSenhaCadastro.setSelection(editSenhaCadastro.getText().length());
                    return true;
                }
            }
            return false;
        });

        // Botão de cadastro
        btnCadastrar.setOnClickListener(v -> {
            String nome = editNomeCadastro.getText().toString().trim();
            String email = editEmailCadastro.getText().toString().trim();
            String senha = editSenhaCadastro.getText().toString().trim();
            String contato = editContato.getText().toString().trim();

            if (nome.isEmpty() || email.isEmpty() || senha.isEmpty() || contato.isEmpty()) {
                Toast.makeText(CadastroForm.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                return;
            }

            Usuario novoUsuario = new Usuario();
            novoUsuario.setNome(nome);
            novoUsuario.setEmail(email);
            novoUsuario.setSenha(senha);
            novoUsuario.setContato(contato);
            novoUsuario.setTipo("cliente"); // Todos os cadastros são clientes

            Call<Usuario> call = api.criarUsuario(novoUsuario);
            call.enqueue(new Callback<Usuario>() {
                @Override
                public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(CadastroForm.this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();

                        // 🔐 Faz login automático
                        Usuario usuarioLogin = new Usuario();
                        usuarioLogin.setEmail(email);
                        usuarioLogin.setSenha(senha);

                        Call<Usuario> callLogin = api.login(usuarioLogin);
                        callLogin.enqueue(new Callback<Usuario>() {
                            @Override
                            public void onResponse(Call<Usuario> call, Response<Usuario> responseLogin) {
                                if (responseLogin.isSuccessful() && responseLogin.body() != null) {
                                    Usuario usuarioLogado = responseLogin.body();
                                    Toast.makeText(CadastroForm.this, "Bem-vindo, " + usuarioLogado.getNome(), Toast.LENGTH_SHORT).show();

                                    // 🔄 Redireciona conforme o tipo do usuário
                                    Intent intent;
                                    switch (usuarioLogado.getTipo().toLowerCase()) {
                                        case "cliente":
                                            intent = new Intent(CadastroForm.this, ClienteHomeForm.class);
                                            break;
                                        case "tecnico":
                                            intent = new Intent(CadastroForm.this, TecnicoMeusChamadosForm.class);
                                            break;
                                        case "admin":
                                            intent = new Intent(CadastroForm.this, AdminHomeForm.class);
                                            break;
                                        default:
                                            Toast.makeText(CadastroForm.this, "Tipo de usuário desconhecido", Toast.LENGTH_SHORT).show();
                                            return;
                                    }

                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(CadastroForm.this, "Erro no login automático: " + responseLogin.code(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Usuario> call, Throwable t) {
                                Toast.makeText(CadastroForm.this, "Falha no login automático: " + t.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });

                    } else {
                        Toast.makeText(CadastroForm.this, "Erro no cadastro: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Usuario> call, Throwable t) {
                    Toast.makeText(CadastroForm.this, "Falha na conexão: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
