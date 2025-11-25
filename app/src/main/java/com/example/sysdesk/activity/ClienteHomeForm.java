package com.example.sysdesk.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sysdesk.R;
import com.example.sysdesk.adapter.ChamadoDashboardAdapter;
import com.example.sysdesk.api.SysDeskApi;
import com.example.sysdesk.helpers.NavbarHelper;
import com.example.sysdesk.model.Chamado;
import com.example.sysdesk.network.RetrofitClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_home_form);

        // ==========================
        // NAVBAR CENTRALIZADO
        // ==========================
      com.example.sysdesk.helpers.NavbarHelper.configurarNavbar(this, AbrirChamadoForm.class);



        // ==========================
        // DASHBOARD
        // ==========================
        recyclerDashboard = findViewById(R.id.recyclerDashboard);
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

        carregarChamadosDashboard();
    }

    private void carregarChamadosDashboard() {
        SysDeskApi apiService = RetrofitClient.getRetrofitInstance().create(SysDeskApi.class);
        Call<List<Chamado>> call = apiService.getChamados();

        call.enqueue(new Callback<List<Chamado>>() {
            @Override
            public void onResponse(Call<List<Chamado>> call, Response<List<Chamado>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaChamados.clear();
                    listaChamados.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Chamado>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
