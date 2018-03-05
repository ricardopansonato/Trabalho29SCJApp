package trabalho.fiap.com.br.trabalho29scjapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
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


public class ProdutoHolder extends RecyclerView.ViewHolder {

    String id, authorization;
    TextView lblNome, lblDescricao;
    ImageView imgEdit, imgRemove;
    ProdutoAdapter adapter;

    private Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("https://trabalho-android-scj29.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public ProdutoHolder(ProdutoAdapter value, View itemView) {
        super(itemView);
        this.adapter = value;
        lblNome = itemView.findViewById(R.id.lblNome);
        lblDescricao = itemView.findViewById(R.id.lblDescricao);
        imgEdit = itemView.findViewById(R.id.editar_item);
        imgRemove = itemView.findViewById(R.id.remover_item);

        imgEdit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                adapter.editar(id);
            }
        });

        imgRemove.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ProdutoAPI service = getRetrofit().create(ProdutoAPI.class);
                service.excluir(authorization, id)
                        .enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call,
                                                   Response<Void> response) {
                                adapter.delete(getLayoutPosition());
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {

                            }
                        });
            }
        });
    }
}
