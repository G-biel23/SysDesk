package com.example.sysdesk.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sysdesk.R;
import com.example.sysdesk.api.SysDeskApi;
import com.example.sysdesk.model.Chamado;
import com.example.sysdesk.network.RetrofitClient;
import com.example.sysdesk.event.ChamadoExcluidoEvent;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChamadoDetalhesActivity extends AppCompatActivity {

    public static final String EXTRA_CHAMADO = "CHAMADO";

    private TextView txtTitulo, txtDescricao, txtStatus, txtCategoria;
    private Button btnEditar, btnExcluir;

    private Chamado chamadoSelecionado;

    public static void abrir(Context ctx, Chamado chamado) {
        android.content.Intent intent = new android.content.Intent(ctx, ChamadoDetalhesActivity.class);
        intent.putExtra(EXTRA_CHAMADO, chamado);
        ctx.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chamado_detalhes);

        txtTitulo = findViewById(R.id.txtTituloDetalhes);
        txtDescricao = findViewById(R.id.txtDescricaoDetalhes);
        txtStatus = findViewById(R.id.txtStatusDetalhes);
        txtCategoria = findViewById(R.id.txtCategoriaDetalhes);

        btnEditar = findViewById(R.id.btnEditarChamado);
        btnExcluir = findViewById(R.id.btnExcluirChamado);

        chamadoSelecionado = (Chamado) getIntent().getSerializableExtra(EXTRA_CHAMADO);
        if (chamadoSelecionado == null) {
            Toast.makeText(this, "Erro ao abrir chamado", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        txtTitulo.setText(chamadoSelecionado.getTitulo());
        txtDescricao.setText(chamadoSelecionado.getDescricao());
        txtStatus.setText(chamadoSelecionado.getStatus());
        txtCategoria.setText(chamadoSelecionado.getCategoria() != null ? chamadoSelecionado.getCategoria().getNome() : "N/A");

        // Botão editar (pode abrir AbrirChamadoForm para edição)
        btnEditar.setOnClickListener(v -> AbrirChamadoForm.abrirParaEdicao(this, chamadoSelecionado));

        // Botão excluir
        btnExcluir.setOnClickListener(v -> new AlertDialog.Builder(this)
                .setTitle("Excluir Chamado")
                .setMessage("Deseja realmente excluir este chamado?")
                .setPositiveButton("Sim", (dialog, which) -> excluirChamado(chamadoSelecionado.getId()))
                .setNegativeButton("Não", null)
                .show());
    }

    private void excluirChamado(int idChamado) {
        SysDeskApi api = RetrofitClient.getRetrofitInstance().create(SysDeskApi.class);
        Call<Void> call = api.excluirChamado(idChamado);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    Toast.makeText(ChamadoDetalhesActivity.this, "Chamado excluído!", Toast.LENGTH_SHORT).show();

                    // 🔹 Dispara evento de exclusão
                    EventBus.getDefault().post(new ChamadoExcluidoEvent(idChamado));
                    finish();
                } else {
                    Toast.makeText(ChamadoDetalhesActivity.this, "Erro ao excluir chamado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ChamadoDetalhesActivity.this, "Falha de conexão", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }
}
