package com.example.sysdesk.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sysdesk.R;
import com.example.sysdesk.adapter.ChamadoTecnicoAdapter;
import com.example.sysdesk.api.SysDeskApi;
import com.example.sysdesk.helpers.NavbarHelperTecnico;
import com.example.sysdesk.model.Chamado;
import com.example.sysdesk.model.Usuario;
import com.example.sysdesk.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TecnicoMeusChamadosForm extends AppCompatActivity {

    private RecyclerView recyclerChamados;
    private ChamadoTecnicoAdapter adapter;
    private SysDeskApi api;
    private Usuario tecnicoLogado;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tecnico_meus_chamados);

        recyclerChamados = findViewById(R.id.recyclerChamados);
        recyclerChamados.setLayoutManager(new LinearLayoutManager(this));

        // Carregar usuário logado do SharedPreferences
        SharedPreferences prefs = getSharedPreferences("sysdesk_prefs", MODE_PRIVATE);
        tecnicoLogado = new Usuario();
        tecnicoLogado.setId(prefs.getInt("USER_ID", -1));
        tecnicoLogado.setNome(prefs.getString("NOME_USUARIO", "Desconhecido"));

        api = RetrofitClient.getRetrofitInstance().create(SysDeskApi.class);

        NavbarHelperTecnico.configurarNavbar(this, AbrirChamadoForm.class);

        // Inicializa o adapter vazio para evitar NPE
        adapter = new ChamadoTecnicoAdapter(this, new ArrayList<>(), chamado -> aceitarChamado(chamado));
        recyclerChamados.setAdapter(adapter);

        carregarChamados();
    }

    private void carregarChamados() {
        api.getChamadosPorTecnico(tecnicoLogado.getId()).enqueue(new Callback<List<Chamado>>() {
            @Override
            public void onResponse(Call<List<Chamado>> call, Response<List<Chamado>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter.setChamados(response.body());
                } else {
                    adapter.setChamados(new ArrayList<>());
                    Toast.makeText(TecnicoMeusChamadosForm.this, "Falha ao carregar chamados", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Chamado>> call, Throwable t) {
                adapter.setChamados(new ArrayList<>());
                Toast.makeText(TecnicoMeusChamadosForm.this, "Erro de conexão", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void aceitarChamado(Chamado chamado) {
        api.aceitarChamado(chamado.getId(), tecnicoLogado).enqueue(new Callback<Chamado>() {
            @Override
            public void onResponse(Call<Chamado> call, Response<Chamado> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(TecnicoMeusChamadosForm.this, "Chamado aceito!", Toast.LENGTH_SHORT).show();
                    carregarChamados(); // recarrega a lista
                } else {
                    Toast.makeText(TecnicoMeusChamadosForm.this, "Falha ao aceitar chamado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Chamado> call, Throwable t) {
                Toast.makeText(TecnicoMeusChamadosForm.this, "Erro de conexão", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
