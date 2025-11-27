package com.example.sysdesk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sysdesk.R;
import com.example.sysdesk.api.SysDeskApi;
import com.example.sysdesk.model.Chamado;
import com.example.sysdesk.model.Usuario;
import com.example.sysdesk.network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChamadoDisponivelAdapter extends RecyclerView.Adapter<ChamadoDisponivelAdapter.ChamadoViewHolder> {

    private Context context;
    private List<Chamado> listaChamados;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onChamadoAceito(Chamado chamado);
        void onChamadoFechado(Chamado chamado);
    }

    public ChamadoDisponivelAdapter(Context context, List<Chamado> listaChamados, OnItemClickListener listener) {
        this.context = context;
        this.listaChamados = listaChamados;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ChamadoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_chamado_tecnico, parent, false);
        return new ChamadoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChamadoViewHolder holder, int position) {
        Chamado chamado = listaChamados.get(position);

        holder.txtTitulo.setText(chamado.getTitulo());
        holder.txtDescricao.setText(chamado.getDescricao());
        holder.txtUsuario.setText("Usuário: " + (chamado.getUsuario() != null ? chamado.getUsuario().getNome() : "Desconhecido"));
        holder.txtTecnico.setText("Técnico: " + (chamado.getTecnico() != null ? chamado.getTecnico().getNome() : "Não atribuído"));
        holder.txtStatus.setText("Status: " + chamado.getStatus());

        // Aceitar chamado
        holder.btnAceitar.setOnClickListener(v -> {
            if (chamado.getTecnico() != null) {
                Toast.makeText(context, "Chamado já foi aceito", Toast.LENGTH_SHORT).show();
                return;
            }

            // Pegar id do técnico logado do SharedPreferences
            int tecnicoId = context.getSharedPreferences("sysdesk_prefs", Context.MODE_PRIVATE)
                    .getInt("USER_ID", 0);

            Usuario tecnico = new Usuario();
            tecnico.setId(tecnicoId);

            SysDeskApi api = RetrofitClient.getRetrofitInstance().create(SysDeskApi.class);
            api.aceitarChamado(chamado.getId(), tecnico).enqueue(new Callback<Chamado>() {
                @Override
                public void onResponse(Call<Chamado> call, Response<Chamado> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        listaChamados.set(position, response.body());
                        notifyItemChanged(position);
                        listener.onChamadoAceito(response.body());
                    } else {
                        Toast.makeText(context, "Erro ao aceitar chamado" + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Chamado> call, Throwable t) {
                    Toast.makeText(context, "Falha na conexão", Toast.LENGTH_SHORT).show();
                }
            });
        });

        // Fechar chamado
        holder.btnFechar.setOnClickListener(v -> {
            SysDeskApi api = RetrofitClient.getRetrofitInstance().create(SysDeskApi.class);
            api.fecharChamado(chamado.getId()).enqueue(new Callback<Chamado>() {
                @Override
                public void onResponse(Call<Chamado> call, Response<Chamado> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(context, "Chamado fechado!", Toast.LENGTH_SHORT).show();
                        listaChamados.remove(position); // corrigido
                        notifyItemRemoved(position);
                    } else {
                        Toast.makeText(context, "Erro ao fechar: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Chamado> call, Throwable t) {
                    Toast.makeText(context, "Falha: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });


    }

    @Override
    public int getItemCount() {
        return listaChamados.size();
    }

    public void setChamados(List<Chamado> novosChamados) {
        listaChamados = novosChamados;
        notifyDataSetChanged();
    }

    static class ChamadoViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitulo, txtDescricao, txtUsuario, txtTecnico, txtStatus;
        Button btnAceitar, btnFechar;

        public ChamadoViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitulo = itemView.findViewById(R.id.txtTitulo);
            txtDescricao = itemView.findViewById(R.id.txtDescricao);
            txtUsuario = itemView.findViewById(R.id.txtUsuario);
            txtTecnico = itemView.findViewById(R.id.txtTecnico);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            btnAceitar = itemView.findViewById(R.id.btnAceitar);
            btnFechar = itemView.findViewById(R.id.btnFechar);
        }
    }
}
