package com.example.sysdesk;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CadastroActivity extends AppCompatActivity {

    private EditText etNome, etEmail, etSenha, etContato;
    private Button btnCadastrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        etNome = findViewById(R.id.etNome);
        etEmail = findViewById(R.id.etEmail);
        etSenha = findViewById(R.id.etSenha);
        etContato = findViewById(R.id.etContato);
        btnCadastrar = findViewById(R.id.btnCadastrar);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nome = etNome.getText().toString();
                String email = etEmail.getText().toString();
                String senha = etSenha.getText().toString();
                String contato = etContato.getText().toString();

                // Validação simples
                if(nome.isEmpty() || email.isEmpty() || senha.isEmpty() || contato.isEmpty()) {
                    Toast.makeText(CadastroActivity.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CadastroActivity.this, "Cadastro realizado com sucesso", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }
}