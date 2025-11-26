package com.example.sysdesk.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sysdesk.R;
import com.example.sysdesk.adapter.ChamadoDashboardAdapter;
import com.example.sysdesk.api.SysDeskApi;
import com.example.sysdesk.helpers.NavbarHelper;
import com.example.sysdesk.model.Chamado;
import com.example.sysdesk.network.RetrofitClient;
import com.example.sysdesk.event.ChamadoAtualizadoEvent;
import com.example.sysdesk.event.ChamadoExcluidoEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClienteHomeForm extends AppCompatActivity {

    private RecyclerView recyclerDashboard;
    private ChamadoDashboardAdapter adapter;
    private List<Chamado> listaChamados = new ArrayList<>();

    private Button filtroTodos, filtroAbertos, filtroAndamento, filtroFinalizados;
    private int idUsuarioLogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_home_form);

        // ==========================
        // PEGAR ID DO USUÁRIO LOGADO
        // ==========================
        SharedPreferences prefs = getSharedPreferences("sysdesk_prefs", MODE_PRIVATE);
        idUsuarioLogado = prefs.getInt("USER_ID", 0);

        Log.d("DEBUG_CLIENTE_HOME", "ID Usuário logado: " + idUsuarioLogado);

        if (idUsuarioLogado <= 0) {
            Toast.makeText(this, "Usuário não logado. Por favor, faça login novamente.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // ==========================
        // NAVBAR
        // ==========================
        NavbarHelper.configurarNavbar(this, AbrirChamadoForm.class);

        // ==========================
        // DASHBOARD
        // ==========================
        recyclerDashboard = findViewById(R.id.recyclerChamados);
        recyclerDashboard.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ChamadoDashboardAdapter(this, listaChamados);
        recyclerDashboard.setAdapter(adapter);

        // ==========================
        // FILTROS
        // ==========================
        filtroTodos = findViewById(R.id.filtroTodos);
        filtroAbertos = findViewById(R.id.filtroAbertos);
        filtroAndamento = findViewById(R.id.filtroAndamento);
        filtroFinalizados = findViewById(R.id.filtroFinalizados);

        filtroTodos.setOnClickListener(v -> adapter.filtrar("TODOS"));
        filtroAbertos.setOnClickListener(v -> adapter.filtrar("ABERTO"));
        filtroAndamento.setOnClickListener(v -> adapter.filtrar("EM ANDAMENTO"));
        filtroFinalizados.setOnClickListener(v -> adapter.filtrar("FINALIZADO"));

        // ==========================
        // CARREGAR CHAMADOS DO USUÁRIO LOGADO
        // ==========================
        carregarChamadosAbertosUsuario(idUsuarioLogado);

        // ==========================
        // REGISTRAR EVENTBUS
        // ==========================
        EventBus.getDefault().register(this);

        // ==========================
        // CLIQUE NOS ITENS
        // ==========================
        adapter.setOnItemClickListener(chamado -> {
            ChamadoDetalhesActivity.abrir(this, chamado);
        });
    }

    // ==========================
    // MÉTODO PARA CARREGAR CHAMADOS
    // ==========================
    private void carregarChamadosAbertosUsuario(int idUsuario) {
        SysDeskApi apiService = RetrofitClient.getRetrofitInstance().create(SysDeskApi.class);
        Call<List<Chamado>> call = apiService.getChamadosAbertosDoUsuario(idUsuario);

        call.enqueue(new Callback<List<Chamado>>() {
            @Override
            public void onResponse(Call<List<Chamado>> call, Response<List<Chamado>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter.setChamados(response.body());
                } else {
                    Toast.makeText(ClienteHomeForm.this, "Erro ao carregar chamados", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Chamado>> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(ClienteHomeForm.this, "Falha de conexão", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ==========================
    // EVENTOS DO EVENTBUS
    // ==========================
    @Subscribe
    public void onChamadoAtualizado(ChamadoAtualizadoEvent event) {
        carregarChamadosAbertosUsuario(idUsuarioLogado);
    }

    @Subscribe
    public void onChamadoExcluido(ChamadoExcluidoEvent event) {
        carregarChamadosAbertosUsuario(idUsuarioLogado);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
