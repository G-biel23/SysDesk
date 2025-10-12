package com.example.sysdesk;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.Editable;





import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class CadastroForm extends AppCompatActivity {

    private Spinner spinnerTipo;
    private boolean isSpinnerTouched = false;
    private EditText editNomeCadastro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_form);

        spinnerTipo = findViewById(R.id.spinner_tipo);
        editNomeCadastro = findViewById(R.id.edit_nomeCadastro);
        EditText editContato = findViewById(R.id.edit_contato);

        editContato.addTextChangedListener(new TextWatcher() {
            private boolean isUpdating = false;
            private final String mask = "(##) #####-####";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isUpdating) {
                    isUpdating = false;
                    return;
                }

                String digits = s.toString().replaceAll("[^0-9]", "");
                StringBuilder masked = new StringBuilder();
                int i = 0;

                for (char m : mask.toCharArray()) {
                    if (m == '#') {
                        if (i < digits.length()) masked.append(digits.charAt(i++));
                        else break;
                    } else {
                        if (i < digits.length()) masked.append(m);
                        else break;
                    }
                }

                isUpdating = true;
                editContato.setText(masked.toString());
                editContato.setSelection(masked.length());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });


        // Adiciona InputFilter para aceitar apenas letras e espaços
        InputFilter letrasFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    if (!Character.isLetter(source.charAt(i)) && !Character.isWhitespace(source.charAt(i))) {
                        return "";
                    }
                }
                return null;
            }
        };
        editNomeCadastro.setFilters(new InputFilter[]{letrasFilter, new InputFilter.LengthFilter(100)});

        // Opções do Spinner
        String[] tipos = {"Cliente", "Técnico", "Admin"};


        // Adapter com layout personalizado
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, tipos) {
            @Override
            public View getView(int position, View convertView, android.view.ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view;
                text.setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.black));
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, android.view.ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView text = (TextView) view;
                text.setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.black));
                text.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), android.R.color.white));
                return view;
            }
        };

        spinnerTipo.setAdapter(adapter);

        // Detecta toque do usuário
        spinnerTipo.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                isSpinnerTouched = true;
            }
            return false;
        });

        // Evento de seleção
        spinnerTipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isSpinnerTouched) {
                    String tipoSelecionado = parent.getItemAtPosition(position).toString();
                    Toast.makeText(CadastroForm.this, tipoSelecionado + " selecionado com sucesso!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Nenhuma ação necessária
            }
        });
    }
}