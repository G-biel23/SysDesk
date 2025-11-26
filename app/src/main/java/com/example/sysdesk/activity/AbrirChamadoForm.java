package com.example.sysdesk.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sysdesk.R;
import com.example.sysdesk.api.SysDeskApi;
import com.example.sysdesk.model.Categoria;
import com.example.sysdesk.model.Chamado;
import com.example.sysdesk.model.Usuario;
import com.example.sysdesk.network.RetrofitClient;
import com.example.sysdesk.event.ChamadoAtualizadoEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AbrirChamadoForm extends AppCompatActivity {

    private EditText editTitulo, editDescricao, editOutraCategoria;
    private Spinner spinnerCategoria;
    private View btnEnviar;
    private SysDeskApi api;
    private List<Categoria> categorias = new ArrayList<>();
    private ArrayAdapter<String> spinnerAdapter;
    private final String OPCAO_OUTRA = "Outra";
    private Chamado chamadoEditar = null;

    public static void abrirParaEdicao(Context ctx, Chamado chamado) {
        Intent intent = new Intent(ctx, AbrirChamadoForm.class);
        intent.putExtra("CHAMADO_EDITAR", chamado);
        ctx.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abrir_chamado);

        editTitulo = findViewById(R.id.editTitulo);
        editDescricao = findViewById(R.id.editDescricao);
        editOutraCategoria = findViewById(R.id.editOutraCategoria);
        spinnerCategoria = findViewById(R.id.spinnerCategoria);
        btnEnviar = findViewById(R.id.btnEnviarChamado);

        String token = getSharedPreferences("sysdesk_prefs", MODE_PRIVATE).getString("TOKEN", null);
        if (token != null) RetrofitClient.setToken(token);
        api = RetrofitClient.getRetrofitInstance().create(SysDeskApi.class);

        setupSpinner();
        carregarCategorias();

        // Verifica se é edição
        chamadoEditar = (Chamado) getIntent().getSerializableExtra("CHAMADO_EDITAR");
        if(chamadoEditar != null){
            editTitulo.setText(chamadoEditar.getTitulo());
            editDescricao.setText(chamadoEditar.getDescricao());
        }

        btnEnviar.setOnClickListener(v -> enviarChamado());
    }

    private void setupSpinner() {
        spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>());
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoria.setAdapter(spinnerAdapter);

        spinnerCategoria.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                editOutraCategoria.setVisibility(OPCAO_OUTRA.equals(spinnerAdapter.getItem(position)) ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
                editOutraCategoria.setVisibility(View.GONE);
            }
        });
    }

    private void carregarCategorias() {
        api.getCategorias().enqueue(new Callback<List<Categoria>>() {
            @Override
            public void onResponse(@NonNull Call<List<Categoria>> call, @NonNull Response<List<Categoria>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    categorias.clear();
                    categorias.addAll(response.body());
                    atualizarSpinner();
                } else {
                    Toast.makeText(AbrirChamadoForm.this, "Erro ao carregar categorias", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Categoria>> call, @NonNull Throwable t) {
                Toast.makeText(AbrirChamadoForm.this, "Falha: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void atualizarSpinner() {
        List<String> nomes = new ArrayList<>();
        for (Categoria c : categorias) nomes.add(c.getNome());
        nomes.add(OPCAO_OUTRA);

        spinnerAdapter.clear();
        spinnerAdapter.addAll(nomes);
        spinnerAdapter.notifyDataSetChanged();
    }

    private void enviarChamado() {
        String titulo = editTitulo.getText().toString().trim();
        String descricao = editDescricao.getText().toString().trim();
        String categoriaSelecionada = (String) spinnerCategoria.getSelectedItem();

        if (titulo.isEmpty()) { editTitulo.setError("Informe o título"); return; }
        if (descricao.isEmpty()) { editDescricao.setError("Informe a descrição"); return; }
        if (categoriaSelecionada == null) { Toast.makeText(this, "Selecione uma categoria", Toast.LENGTH_SHORT).show(); return; }

        if (OPCAO_OUTRA.equals(categoriaSelecionada)) {
            String nova = editOutraCategoria.getText().toString().trim();
            if (nova.isEmpty()) { editOutraCategoria.setError("Informe a nova categoria"); return; }
            criarCategoriaECriarChamado(nova, titulo, descricao);
        } else {
            int idCategoria = encontrarIdCategoriaPorNome(categoriaSelecionada);
            criarChamadoNoBackend(idCategoria, titulo, descricao);
        }
    }

    private int encontrarIdCategoriaPorNome(String nome) {
        for (Categoria c : categorias) if (nome.equals(c.getNome())) return c.getId();
        return 0;
    }

    private void criarCategoriaECriarChamado(String nomeCategoria, String titulo, String descricao) {
        Categoria nova = new Categoria();
        nova.setNome(nomeCategoria);

        api.criarCategoria(nova).enqueue(new Callback<Categoria>() {
            @Override
            public void onResponse(@NonNull Call<Categoria> call, @NonNull Response<Categoria> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Categoria criada = response.body();
                    categorias.add(criada);
                    atualizarSpinner();
                    criarChamadoNoBackend(criada.getId(), titulo, descricao);
                } else {
                    Toast.makeText(AbrirChamadoForm.this, "Erro ao criar categoria", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Categoria> call, @NonNull Throwable t) {
                Toast.makeText(AbrirChamadoForm.this, "Falha: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void criarChamadoNoBackend(int idCategoria, String titulo, String descricao) {

        int userId = getSharedPreferences("sysdesk_prefs", MODE_PRIVATE).getInt("USER_ID", 1);

        Usuario usuario = new Usuario();
        usuario.setId(userId);

        Categoria categoria = new Categoria();
        categoria.setId(idCategoria);

        Chamado chamado = chamadoEditar != null ? chamadoEditar : new Chamado();
        chamado.setTitulo(titulo);
        chamado.setDescricao(descricao);
        chamado.setStatus("ABERTO");
        chamado.setUsuario(usuario);
        chamado.setTecnico(null);
        chamado.setCategoria(categoria);

        api.criarChamado(chamado).enqueue(new Callback<Chamado>() {
            @Override
            public void onResponse(@NonNull Call<Chamado> call, @NonNull Response<Chamado> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(AbrirChamadoForm.this, "Chamado enviado com sucesso", Toast.LENGTH_SHORT).show();
                    // 🔹 Evento para atualizar dashboard
                    EventBus.getDefault().post(new ChamadoAtualizadoEvent(response.body()));
                    finish();
                } else {
                    Toast.makeText(AbrirChamadoForm.this, "Erro ao enviar chamado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Chamado> call, @NonNull Throwable t) {
                Toast.makeText(AbrirChamadoForm.this, "Falha: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
