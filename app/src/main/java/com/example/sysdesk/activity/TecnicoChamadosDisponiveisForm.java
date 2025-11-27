package com.example.sysdesk.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sysdesk.R;
import com.example.sysdesk.adapter.ChamadoDisponivelAdapter;
import com.example.sysdesk.api.SysDeskApi;
import com.example.sysdesk.helpers.NavbarHelper;
import com.example.sysdesk.helpers.NavbarHelperTecnico;
import com.example.sysdesk.model.Chamado;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TecnicoChamadosDisponiveisForm extends AppCompatActivity {

    private RecyclerView recyclerChamados;
    private ChamadoDisponivelAdapter adapter;
    private List<Chamado> listaChamados = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tecnico_disponiveis);

        // Navbar
        NavbarHelperTecnico.configurarNavbar(this, TecnicoMeusChamadosForm.class);

        recyclerChamados = findViewById(R.id.recyclerChamadosDisponiveis);
        recyclerChamados.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ChamadoDisponivelAdapter(this, listaChamados, new ChamadoDisponivelAdapter.OnItemClickListener() {
            @Override
            public void onChamadoAceito(Chamado chamado) {
                Toast.makeText(TecnicoChamadosDisponiveisForm.this, "Chamado aceito!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChamadoFechado(Chamado chamado) {
                Toast.makeText(TecnicoChamadosDisponiveisForm.this, "Chamado fechado!", Toast.LENGTH_SHORT).show();
            }
        });
        recyclerChamados.setAdapter(adapter);

        carregarChamados();
    }

    private void carregarChamados() {
        SysDeskApi api = com.example.sysdesk.network.RetrofitClient.getRetrofitInstance().create(SysDeskApi.class);

        api.getChamados().enqueue(new Callback<List<Chamado>>() {
            @Override
            public void onResponse(Call<List<Chamado>> call, Response<List<Chamado>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter.setChamados(response.body());
                } else {
                    Toast.makeText(TecnicoChamadosDisponiveisForm.this, "Erro ao carregar chamados: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Chamado>> call, Throwable t) {
                Toast.makeText(TecnicoChamadosDisponiveisForm.this, "Falha na conexão", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
