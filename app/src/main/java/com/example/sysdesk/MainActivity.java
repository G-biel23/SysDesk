package com.example.sysdesk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
public class MainActivity extends AppCompatActivity {

    private EditText etEmail, etSenha;
    private Button btnLogin, btnCadastro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // liga o XML

        // Conectando elementos do XML
        etEmail = findViewById(R.id.etEmail);
        etSenha = findViewById(R.id.etSenha);
        btnLogin = findViewById(R.id.btnLogin);
        btnCadastro = findViewById(R.id.btnCadastro);

        // Clique no botão Entrar
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etEmail.getText().toString();
                String senha = etSenha.getText().toString();

                // Validação simples por enquanto
                if(email.equals("gustavogabrielsf@gmail.com") && senha.equals("2308")) {
                    Toast.makeText(MainActivity.this, "Login Efetuado com Sucesso", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Usuário ou senha incorretos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Clique no botão Cadastrar-se
        btnCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CadastroActivity.class);
                startActivity(intent);
            }
        });
    }
}