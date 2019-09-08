package com.duarte.olxapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.blackcat.currencyedittext.CurrencyEditText;
import com.duarte.olxapp.R;
import com.santalu.maskedittext.MaskEditText;

import java.util.Locale;

public class CadastrarAnuncioActivity extends AppCompatActivity {

    private EditText campoTitulo, campoDescricao;
    private MaskEditText campoTelefone;
    private CurrencyEditText campoValor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_anuncio);

        inicializarComponentes();
    }

    public void salvarAnuncio(View view){

        String valor = campoValor.getText().toString();
        Log.d("salvar", "salvarAnuncio" + valor );

    }

    private void inicializarComponentes(){

        campoTitulo = findViewById(R.id.editTitulo);
        campoTelefone = findViewById(R.id.editTelefone);
        campoDescricao = findViewById(R.id.editDescricao);
        campoValor = findViewById(R.id.editTValor);

        //Configura a localidade para pt -> português BR -> Brasil
        Locale locale = new Locale("pt", "BR");
        campoValor.setLocale( locale );

    }
}
