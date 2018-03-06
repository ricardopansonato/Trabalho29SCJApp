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
import trabalho.fiap.com.br.trabalho29scjapp.model.Token;
import trabalho.fiap.com.br.trabalho29scjapp.model.Usuario;
import trabalho.fiap.com.br.trabalho29scjapp.api.UsuarioAPI;

public class LoginActivity extends AppCompatActivity {

    private EditText edtUsuario;
    private EditText edtSenha;

    private Usuario usuario;

    private ProgressDialog progressDialog;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUsuario = (EditText) findViewById(R.id.edtUsuario);
        edtSenha = (EditText) findViewById(R.id.edtSenha);

        progressDialog = new ProgressDialog(this);

        context = getApplicationContext();

        final Button button = findViewById(R.id.btnLogin);
        final Button criar = findViewById(R.id.btnCriar);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                login();
            }
        });
        criar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CreateUserActivity.class);
                startActivity(intent);
            }
        });
    }

    private Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("https://trabalho-android-scj29.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private void login() {
        showProgress(context.getString(R.string.login), context.getString(R.string.login_descricao));

        UsuarioAPI service = getRetrofit().create(UsuarioAPI.class);
        if(usuario == null)
            usuario = new Usuario();

        usuario.setUsuario(edtUsuario.getText().toString());
        usuario.setSenha(edtSenha.getText().toString());

        service.login(usuario).enqueue(new Callback<Token>() {

            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                dismissProgress();
                Toast.makeText(getApplicationContext(),
                        context.getString(R.string.login_sucesso), Toast.LENGTH_SHORT).show();
                if (response.body() != null) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("authorization", response.body().getToken());
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(),
                            context.getString(R.string.login_erro), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                dismissProgress();
                Toast.makeText(getApplicationContext(),
                        context.getString(R.string.erro), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showProgress(String titulo, String mensagem) {
        if(progressDialog == null)
            progressDialog = new ProgressDialog(LoginActivity.this);

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
