package trabalho.fiap.com.br.trabalho29scjapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import trabalho.fiap.com.br.trabalho29scjapp.api.ProdutoAPI;
import trabalho.fiap.com.br.trabalho29scjapp.model.Produto;
import trabalho.fiap.com.br.trabalho29scjapp.model.Produtos;

public class MainActivity extends AppCompatActivity {

    private EditText edtFiltro;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    private ProgressDialog progressDialog;
    private String authorization;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtFiltro = findViewById(R.id.filtro);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        Intent intent = getIntent();
        authorization = intent.getStringExtra("authorization");

        carregarTela();
    }

    @Override
    public void onResume() {
        super.onResume();
        carregarTela();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_recycler, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.criar:
                criar();
                return true;
            case R.id.pesquisar:
                buscar();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("https://trabalho-android-scj29.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private void showProgress(String titulo, String mensagem) {
        if(progressDialog == null)
            progressDialog = new ProgressDialog(MainActivity.this);

        if(!progressDialog.isShowing()) {
            progressDialog.setMessage(mensagem);
            progressDialog.setTitle(titulo);
            progressDialog.show();
        }
    }

    private void dismissProgress() {
        if(progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    private void criar() {
        Intent intent = getIntent();
        String authorization = intent.getStringExtra("authorization");
        Intent intentCreate = new Intent(getApplicationContext(), CreateUpdateActivity.class);
        intentCreate.putExtra("authorization", authorization);
        startActivity(intentCreate);
    }

    public void editar(String id) {
        showProgress("Produto", "Carregando os dados");
        ProdutoAPI service = getRetrofit().create(ProdutoAPI.class);
        service.buscar(authorization, id)
                .enqueue(new Callback<Produto>() {
                    @Override
                    public void onResponse(Call<Produto> call,
                                           Response<Produto> response) {

                        if(response.body() != null) {
                            Intent intentCreate = new Intent(getApplicationContext(), CreateUpdateActivity.class);
                            intentCreate.putExtra("authorization", authorization);
                            intentCreate.putExtra("id", response.body().getId());
                            intentCreate.putExtra("nome", response.body().getNome());
                            intentCreate.putExtra("descricao", response.body().getDescricao());
                            intentCreate.putExtra("valor", response.body().getValor().toString());
                            startActivity(intentCreate);
                        }
                        dismissProgress();
                    }

                    @Override
                    public void onFailure(Call<Produto> call, Throwable t) {
                        dismissProgress();
                    }
                });
    }

    private void carregarTela() {
        showProgress("Produto", "Carregando os dados");
        ProdutoAPI service = getRetrofit().create(ProdutoAPI.class);
        Intent intent = getIntent();
        final String authorization = intent.getStringExtra("authorization");
        service.buscarTodos(authorization, null)
                .enqueue(new Callback<Produtos>() {
                    @Override
                    public void onResponse(Call<Produtos> call,
                                           Response<Produtos> response) {

                        if(response.body() != null) {
                            if(response.body().getItems().size() > 0) {
                                recyclerView.setAdapter(new ProdutoAdapter(response.body().getItems(), authorization, MainActivity.this));
                            } else {
                                Toast.makeText(getApplicationContext(),
                                        "Deu ruim", Toast.LENGTH_SHORT).show();
                            }
                        }
                        dismissProgress();
                    }

                    @Override
                    public void onFailure(Call<Produtos> call, Throwable t) {
                        Toast.makeText(getApplicationContext(),
                                "Deu ruim", Toast.LENGTH_SHORT).show();
                        dismissProgress();
                    }
                });
    }

    private void buscar() {
        showProgress("Produto", "Carregando os dados");
        ProdutoAPI service = getRetrofit().create(ProdutoAPI.class);
        Intent intent = getIntent();
        final String authorization = intent.getStringExtra("authorization");
        service.buscarTodos(authorization, edtFiltro.getText().toString())
                .enqueue(new Callback<Produtos>() {
                    @Override
                    public void onResponse(Call<Produtos> call,
                                           Response<Produtos> response) {

                        if(response.body() != null) {
                            if(response.body().getItems().size() > 0) {
                                recyclerView.setAdapter(new ProdutoAdapter(response.body().getItems(), authorization, MainActivity.this));
                            } else {
                                Toast.makeText(getApplicationContext(),
                                        "Deu ruim", Toast.LENGTH_SHORT).show();
                            }
                        }
                        dismissProgress();
                    }

                    @Override
                    public void onFailure(Call<Produtos> call, Throwable t) {
                        Toast.makeText(getApplicationContext(),
                                "Deu ruim", Toast.LENGTH_SHORT).show();
                        dismissProgress();
                    }
                });
    }
}
