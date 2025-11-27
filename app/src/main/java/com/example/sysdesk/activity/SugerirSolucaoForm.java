package com.example.sysdesk.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sysdesk.R;
import com.example.sysdesk.api.SysDeskApi;
import com.example.sysdesk.model.Sugestao;
import com.example.sysdesk.model.Usuario;
import com.example.sysdesk.network.RetrofitClient;

import java.time.LocalDateTime;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SugerirSolucaoForm extends AppCompatActivity {

    private TextView txtChamadoId;
    private EditText edtSolucao;
    private Button btnEnviar;
    private SysDeskApi api;
    private int chamadoId;
    private Usuario tecnicoLogado;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sugerir_solucao);

        txtChamadoId = findViewById(R.id.txtChamadoId);
        edtSolucao = findViewById(R.id.edtSolucao);
        btnEnviar = findViewById(R.id.btnEnviarSolucao);

        api = RetrofitClient.getRetrofitInstance().create(SysDeskApi.class);

        // Recebe ID do chamado via Intent
        chamadoId = getIntent().getIntExtra("CHAMADO_ID", -1);
        txtChamadoId.setText("Chamado #" + chamadoId);

        // Carregar usuário logado do SharedPreferences
        SharedPreferences prefs = getSharedPreferences("sysdesk_prefs", MODE_PRIVATE);
        tecnicoLogado = new Usuario();
        tecnicoLogado.setId(prefs.getInt("USER_ID", -1));
        tecnicoLogado.setNome(prefs.getString("NOME_USUARIO", "Desconhecido"));

        btnEnviar.setOnClickListener(v -> enviarSugestao());
    }

    private void enviarSugestao() {
        String descricao = edtSolucao.getText().toString().trim();
        if (descricao.isEmpty()) {
            edtSolucao.setError("Digite uma sugestão");
            return;
        }

        Sugestao sugestao = new Sugestao();
        sugestao.setDescricao(descricao);
        sugestao.setTecnico(tecnicoLogado);
        sugestao.setDataHora(LocalDateTime.now());

        api.sugerirSolucao(chamadoId, sugestao).enqueue(new Callback<Sugestao>() {
            @Override
            public void onResponse(Call<Sugestao> call, Response<Sugestao> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(SugerirSolucaoForm.this, "Sugestão enviada com sucesso!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(SugerirSolucaoForm.this, "Erro ao enviar: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Sugestao> call, Throwable t) {
                Toast.makeText(SugerirSolucaoForm.this, "Falha na conexão: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
