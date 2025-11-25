package com.example.sysdesk.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
import com.example.sysdesk.network.RetrofitClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AbrirChamadoForm extends AppCompatActivity {

    private EditText editTitulo;
    private EditText editDescricao;
    private Spinner spinnerCategoria;
    private EditText editOutraCategoria;
    private View btnEnviar;

    private SysDeskApi api;
    private List<Categoria> categorias = new ArrayList<>();
    private ArrayAdapter<String> spinnerAdapter;
    private final String OPCAO_OUTRA = "Outra";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abrir_chamado);

        editTitulo = findViewById(R.id.editTitulo);
        editDescricao = findViewById(R.id.editDescricao);
        spinnerCategoria = findViewById(R.id.spinnerCategoria);
        editOutraCategoria = findViewById(R.id.editOutraCategoria);
        btnEnviar = findViewById(R.id.btnEnviarChamado);

        // Recupera token salvo e configura Retrofit
        String token = getSharedPreferences("sysdesk_prefs", MODE_PRIVATE)
                .getString("TOKEN", null);
        if (token != null) {
            RetrofitClient.setToken(token);
        }

        api = RetrofitClient.getRetrofitInstance().create(SysDeskApi.class);

        setupSpinner();
        carregarCategorias();

        btnEnviar.setOnClickListener(v -> enviarChamado());

        com.example.sysdesk.helpers.NavbarHelper.configurarNavbar(this, AbrirChamadoForm.class);
    }

    private void setupSpinner() {
        spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>());
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoria.setAdapter(spinnerAdapter);

        spinnerCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String sel = spinnerAdapter.getItem(position);
                if (OPCAO_OUTRA.equals(sel)) {
                    editOutraCategoria.setVisibility(View.VISIBLE);
                } else {
                    editOutraCategoria.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
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
                    Toast.makeText(AbrirChamadoForm.this, "Erro: " + response.code(), Toast.LENGTH_SHORT).show();
                    Log.e("API", "Erro ao carregar categorias. Código: " + response.code());
                    try {
                        if (response.errorBody() != null)
                            Log.e("API", "Erro body: " + response.errorBody().string());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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

        if (titulo.isEmpty()) {
            editTitulo.setError("Informe o título");
            editTitulo.requestFocus();
            return;
        }
        if (descricao.isEmpty()) {
            editDescricao.setError("Informe a descrição");
            editDescricao.requestFocus();
            return;
        }
        if (categoriaSelecionada == null) {
            Toast.makeText(this, "Selecione uma categoria", Toast.LENGTH_SHORT).show();
            return;
        }

        if (OPCAO_OUTRA.equals(categoriaSelecionada)) {
            String novaCat = editOutraCategoria.getText().toString().trim();
            if (novaCat.isEmpty()) {
                editOutraCategoria.setError("Digite a nova categoria");
                editOutraCategoria.requestFocus();
                return;
            }
            criarCategoriaECriarChamado(novaCat, titulo, descricao);
        } else {
            int idCategoria = encontrarIdCategoriaPorNome(categoriaSelecionada);
            criarChamadoNoBackend(idCategoria, titulo, descricao);
        }
    }

    private int encontrarIdCategoriaPorNome(String nome) {
        for (Categoria c : categorias) {
            if (nome.equals(c.getNome())) return c.getId();
        }
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
        Chamado chamado = new Chamado();
        chamado.setTitulo(titulo);
        chamado.setDescricao(descricao);
        chamado.setId_categoria(idCategoria);
        chamado.setId_tecnico(0);
        chamado.setStatus("ABERTO");
        chamado.setData_abertura(getDataAtualIso());
        chamado.setId_usuario(0);

        api.criarChamado(chamado).enqueue(new Callback<Chamado>() {
            @Override
            public void onResponse(@NonNull Call<Chamado> call, @NonNull Response<Chamado> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AbrirChamadoForm.this, "Chamado enviado com sucesso", Toast.LENGTH_SHORT).show();
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

    private String getDataAtualIso() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        return sdf.format(new Date());
    }
}
