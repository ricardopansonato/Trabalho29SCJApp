package trabalho.fiap.com.br.trabalho29scjapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import trabalho.fiap.com.br.trabalho29scjapp.api.ProdutoAPI;
import trabalho.fiap.com.br.trabalho29scjapp.model.Produto;

public class CreateUpdateActivity extends AppCompatActivity {

    private EditText edtNome;
    private EditText edtDescricao;
    private EditText edtValor;

    private Produto produto;

    private ProgressDialog progressDialog;
    private String authorization;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        edtNome = (EditText) findViewById(R.id.edtNome);
        edtDescricao = (EditText) findViewById(R.id.edtDescricao);
        edtValor = (EditText) findViewById(R.id.edtValor);

        Intent intent = getIntent();
        authorization = intent.getStringExtra("authorization");
        id = intent.getStringExtra("id");

        String nome = intent.getStringExtra("nome");
        if (nome != null) {
            edtNome.setText(nome);
        }

        String descricao = intent.getStringExtra("descricao");
        if (descricao != null) {
            edtDescricao.setText(descricao);
        }

        String valor = intent.getStringExtra("valor");
        if (valor != null) {
            edtValor.setText(valor);
        }

        progressDialog = new ProgressDialog(this);
    }

    private Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("https://trabalho-android-scj29.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_create, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.salvar:
                salvar();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void salvar() {
        showProgress("Produto", "Salvando produto");

        ProdutoAPI service = getRetrofit().create(ProdutoAPI.class);
        if(produto == null) {
            produto = new Produto();
        }

        if (id != null && !id.equals("")) {
            produto.setId(id);
        }

        if (!edtNome.getText().toString().equals("") &&
                !edtDescricao.getText().toString().equals("") &&
                !edtValor.getText().toString().equals("")) {

            produto.setNome(edtNome.getText().toString());
            produto.setDescricao(edtDescricao.getText().toString());
            produto.setValor(Double.parseDouble(edtValor.getText().toString()));

            service.salvar(authorization, produto).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {

                    dismissProgress();
                    Toast.makeText(getApplicationContext(),
                            "Produto gravado", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("authorization", authorization);
                    startActivity(intent);
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    dismissProgress();
                    Toast.makeText(getApplicationContext(),
                            "Deu ruim", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            dismissProgress();
            Toast.makeText(getApplicationContext(),
                    "Dados invalidos", Toast.LENGTH_SHORT).show();
        }
    }

    private void showProgress(String titulo, String mensagem) {
        if(progressDialog == null)
            progressDialog = new ProgressDialog(CreateUpdateActivity.this);

        if(!progressDialog.isShowing()) {
            progressDialog.setMessage(mensagem);
            progressDialog.setTitle(titulo);
            progressDialog.show();
        }
    }

    private void dismissProgress() {
        if(progressDialog != null)
            progressDialog.dismiss();
    }
}
