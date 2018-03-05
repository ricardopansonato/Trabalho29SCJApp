package trabalho.fiap.com.br.trabalho29scjapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import trabalho.fiap.com.br.trabalho29scjapp.api.ProdutoAPI;

public class AboutActivity extends AppCompatActivity {

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        button = findViewById(R.id.voltar);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = getIntent();
                String authorization = intent.getStringExtra("authorization");
                Intent intentAbout = new Intent(getApplicationContext(), MainActivity.class);
                intentAbout.putExtra("authorization", authorization);
                startActivity(intentAbout);
            }
        });
    }
}
