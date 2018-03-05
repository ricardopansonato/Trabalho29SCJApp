package trabalho.fiap.com.br.trabalho29scjapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import trabalho.fiap.com.br.trabalho29scjapp.model.Produto;


public class ProdutoAdapter extends RecyclerView.Adapter<ProdutoHolder> {

    private List<Produto> produtos;
    private String authorization;
    private MainActivity activity;

    public ProdutoAdapter(List<Produto> produtos, String authorization, MainActivity activity) {
        this.produtos = produtos;
        this.authorization = authorization;
        this.activity = activity;
    }

    @Override
    public ProdutoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.text_view, parent, false);
        ProdutoHolder vh = new ProdutoHolder(this, v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ProdutoHolder holder, int position) {
        holder.id = produtos.get(position).getId();
        holder.authorization = authorization;
        holder.lblNome.setText(produtos.get(position).getNome());
        holder.lblDescricao.setText(produtos.get(position).getDescricao());
    }

    @Override
    public int getItemCount() {
        return produtos.size();
    }

    public void delete(int position) {
        produtos.remove(position);
        notifyItemRemoved(position);
    }

    public void editar(String id) {
        activity.editar(id);
    }
}
