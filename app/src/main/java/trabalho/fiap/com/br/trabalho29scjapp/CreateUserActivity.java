package trabalho.fiap.com.br.trabalho29scjapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import trabalho.fiap.com.br.trabalho29scjapp.api.UsuarioAPI;
import trabalho.fiap.com.br.trabalho29scjapp.model.Token;
import trabalho.fiap.com.br.trabalho29scjapp.model.Usuario;

public class CreateUserActivity extends AppCompatActivity {

    private EditText edtUsuario;
    private EditText edtSenha;
    private Button button;

    private ProgressDialog progressDialog;
    private Usuario usuario;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        edtUsuario = (EditText) findViewById(R.id.edtUsuario);
        edtSenha = (EditText) findViewById(R.id.edtSenha);

        progressDialog = new ProgressDialog(this);

        button = findViewById(R.id.criar_usuario);
        context = getApplicationContext();

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showProgress(context.getString(R.string.usuario), context.getString(R.string.usuario_descricao));

                UsuarioAPI service = getRetrofit().create(UsuarioAPI.class);
                if(usuario == null)
                    usuario = new Usuario();

                usuario.setUsuario(edtUsuario.getText().toString());
                usuario.setSenha(edtSenha.getText().toString());

                service.salvar(usuario).enqueue(new Callback<Usuario>() {

                    @Override
                    public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                        dismissProgress();
                        if (response.body() != null) {
                            Toast.makeText(getApplicationContext(),
                                    context.getString(R.string.usuario_sucesso), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    context.getString(R.string.usuario_erro), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Usuario> call, Throwable t) {
                        dismissProgress();
                        Toast.makeText(getApplicationContext(),
                                context.getString(R.string.erro), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("https://trabalho-android-scj29.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private void showProgress(String titulo, String mensagem) {
        if(progressDialog == null)
            progressDialog = new ProgressDialog(CreateUserActivity.this);

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
}
