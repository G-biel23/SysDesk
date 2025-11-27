package com.example.sysdesk.helpers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.example.sysdesk.R;
import com.example.sysdesk.activity.LoginForm;
import com.example.sysdesk.activity.TecnicoChamadosDisponiveisForm;
import com.example.sysdesk.activity.TecnicoMeusChamadosForm;
import com.example.sysdesk.network.RetrofitClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NavbarHelperTecnico {

    public static void configurarNavbar(Activity activity, Class<?> telaDoFab) {

        // ---------------- HISTÓRICO / HOME ----------------
        View btnHistorico = activity.findViewById(R.id.btnHistorico);
        if (btnHistorico != null) {
            btnHistorico.setOnClickListener(v -> {
                Intent i = new Intent(activity, TecnicoChamadosDisponiveisForm.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                activity.startActivity(i);
            });
        }

        // ---------------- Tickets ----------------
        View btnTicket = activity.findViewById(R.id.btnTicket);
        if (btnTicket != null) {
            btnTicket.setOnClickListener(v -> {
                Intent i = new Intent(activity, TecnicoMeusChamadosForm.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                activity.startActivity(i);
            });
        }

        // ---------------- CHAT (EM DESENVOLVIMENTO) ----------------
        View btnChat = activity.findViewById(R.id.btnChat);
        if (btnChat != null) {
            btnChat.setOnClickListener(v -> mostrarDialogoEmDesenvolvimento(activity));
        }

        // ---------------- NOTIFICAÇÕES (EM DESENVOLVIMENTO) ----------------
        View btnNotificacoes = activity.findViewById(R.id.btnNotificacoes);
        if (btnNotificacoes != null) {
            btnNotificacoes.setOnClickListener(v -> mostrarDialogoEmDesenvolvimento(activity));
        }

        // ---------------- LOGOUT ----------------
        View btnLogout = activity.findViewById(R.id.btnConfig);
        if (btnLogout != null) {
            btnLogout.setOnClickListener(v -> realizarLogout(activity));
        }


    }

    private static void mostrarDialogoEmDesenvolvimento(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View dialogView = activity.getLayoutInflater().inflate(R.layout.dialog_em_desenvolvimento, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        Button btnOk = dialogView.findViewById(R.id.btnDialogOk);
        btnOk.setOnClickListener(view -> dialog.dismiss());

        dialog.show();
    }

    private static void realizarLogout(Activity activity) {


        // Limpar SharedPreferences
        activity.getSharedPreferences("sysdesk_prefs", Activity.MODE_PRIVATE)
                .edit()
                .clear()
                .apply();

        // Redirecionar para login
        Intent intent = new Intent(activity, LoginForm.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);

        activity.finish();
    }
}
