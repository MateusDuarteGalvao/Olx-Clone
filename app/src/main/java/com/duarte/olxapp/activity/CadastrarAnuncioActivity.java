package com.duarte.olxapp.activity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.blackcat.currencyedittext.CurrencyEditText;
import com.duarte.olxapp.R;
import com.duarte.olxapp.helper.Permissoes;
import com.duarte.olxapp.model.Anuncio;
import com.santalu.maskedittext.MaskEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CadastrarAnuncioActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText campoTitulo, campoDescricao;
    private ImageView imagem1, imagem2, imagem3;
    private Spinner campoEstado, campoCategoria;
    private MaskEditText campoTelefone;
    private CurrencyEditText campoValor;
    private Anuncio anuncio;

    private String[] permissoes = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    private List<String> listaFotosRecuperadas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_anuncio);

        //Validar permissoes
        Permissoes.validarPermissoes(permissoes, this, 1);

        inicializarComponentes();
        carregarDadosSpinner();
    }

    public Anuncio configurarAnuncio(){
        String estado = campoEstado.getSelectedItem().toString();
        String categoria = campoCategoria.getSelectedItem().toString();
        String titulo = campoTitulo.getText().toString();
        String valor = String.valueOf(campoValor.getRawValue());
        String telefone = campoTelefone.getText().toString();
        String descricao = campoDescricao.getText().toString();

        anuncio = new Anuncio();
        anuncio.setEstado( estado );
        anuncio.setCategoria( categoria );
        anuncio.setTitulo( titulo );
        anuncio.setValor( valor );
        anuncio.setTelefone( telefone );
        anuncio.setDescricao( descricao );

        return anuncio;
    }

    public void validarDadosAnuncio(View view) {

        anuncio = configurarAnuncio();
        
        //Realizando validacoes
        if( listaFotosRecuperadas.size() != 0 ){
            if( !anuncio.getEstado().isEmpty() ){
                if( !anuncio.getCategoria().isEmpty() ){
                    if( !anuncio.getTitulo().isEmpty() ){
                        if( !anuncio.getValor().isEmpty()  && !anuncio.getValor().equals("0")){
                            if( !anuncio.getTelefone().isEmpty() && anuncio.getTelefone().length() >=10 ){
                                if( !anuncio.getDescricao().isEmpty() ){
                                    salvarAnuncio();
                                }else{
                                    exibirMensagemErro("Preencha o campo descrição!");
                                }
                            }else{
                                exibirMensagemErro("Preencha o campo telefone, digite ao menos 10 números!");
                            }
                        }else{
                            exibirMensagemErro("Preencha o campo valor!");
                        }
                    }else{
                        exibirMensagemErro("Preencha o campo título");
                    }
                }else {
                    exibirMensagemErro("Selecione a categoria!");
                }
            }else{
                exibirMensagemErro("Selecione o estado!");
            }
        }else{
            exibirMensagemErro("Selecione ao menos uma foto!");
        }

    }

    private void exibirMensagemErro(String mensagem){
        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show();
    }

    public void salvarAnuncio(){

        /*
        * Salvar imagem no storage
        **/
        for (int  i=0; i < listaFotosRecuperadas.size(); i++){
            String urlImagem = listaFotosRecuperadas.get(i);
            int tamanhoLista = listaFotosRecuperadas.size();
            salvarFotoStorage(urlImagem, tamanhoLista, i );
        }

    }

    private void salvarFotoStorage(String urlImagem, int tamanhoLista, int i) {
    }

    @Override
    public void onClick(View v) {

        switch ( v.getId() ){
            case R.id.imageCadastro1 :
                escolherImagem(1);
                break;
            case R.id.imageCadastro2 :
                escolherImagem(2);
                break;
            case R.id.imageCadastro3 :
                escolherImagem(3);
                break;
        }
    }

    public void escolherImagem(int requestCode){
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if( resultCode == Activity.RESULT_OK){

            //Recuperar Imagem
            Uri imagemSelecionada = data.getData();
            String caminhoImagem = imagemSelecionada.toString();

            //Configura imagem no imageView
            if( requestCode == 1 ){
                imagem1.setImageURI( imagemSelecionada );
            }else if( requestCode == 2 ){
                imagem2.setImageURI( imagemSelecionada );
            }else if( requestCode == 3 ){
                imagem3.setImageURI( imagemSelecionada );
            }

            listaFotosRecuperadas.add( caminhoImagem );
        }
    }

    private void carregarDadosSpinner() {

        /*String[] estados = new String[]{
            "SP", "MT"
        };*/
        //Configura spinner dos estados
        String[] estados = getResources().getStringArray(R.array.estados);
        ArrayAdapter<String> adapterEstado = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item,
                estados);
        adapterEstado.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        campoEstado.setAdapter( adapterEstado );

        //Configura spinner das categorias
        String[] categorias = getResources().getStringArray(R.array.categorias);
        ArrayAdapter<String> adapter1Categoria = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item,
                categorias
        );
        adapter1Categoria.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        campoCategoria.setAdapter( adapter1Categoria );

    }


    private void inicializarComponentes(){

        campoTitulo = findViewById(R.id.editTitulo);
        campoTelefone = findViewById(R.id.editTelefone);
        campoDescricao = findViewById(R.id.editDescricao);
        campoValor = findViewById(R.id.editTValor);
        campoEstado = findViewById(R.id.spinnerEstado);
        campoCategoria = findViewById(R.id.spinnerCategoria);
        imagem1 = findViewById(R.id.imageCadastro1);
        imagem2 = findViewById(R.id.imageCadastro2);
        imagem3 = findViewById(R.id.imageCadastro3);
        imagem1.setOnClickListener(this);
        imagem2.setOnClickListener(this);
        imagem3.setOnClickListener(this);

        //Configura a localidade para pt -> português BR -> Brasil
        Locale locale = new Locale("pt", "BR");
        campoValor.setLocale( locale );

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for( int permissaoResultado : grantResults ){
            if( permissaoResultado == PackageManager.PERMISSION_DENIED){
                alertaValidacaoPermissao();
            }
        }
    }

    private void alertaValidacaoPermissao(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissões negadas");
        builder.setMessage("Para utilizar o app é necessário aceitar as permissões");
        builder.setCancelable(false);
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();


    }
}
