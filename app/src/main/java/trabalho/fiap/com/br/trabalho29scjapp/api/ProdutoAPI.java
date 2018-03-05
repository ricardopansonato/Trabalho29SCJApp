package trabalho.fiap.com.br.trabalho29scjapp.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import trabalho.fiap.com.br.trabalho29scjapp.model.Produto;
import trabalho.fiap.com.br.trabalho29scjapp.model.Produtos;

public interface ProdutoAPI {

    @GET("/produtos")
    Call<Produtos> buscarTodos(@Header("Authorization") String authorization, @Query("nome") String nome);

    @POST("/produto")
    Call<Void> salvar(@Header("Authorization") String authorization, @Body Produto produto);

    @GET("/produto/{id}")
    Call<Produto> buscar(@Header("Authorization") String authorization, @Path(value = "id") String id);

    @DELETE("/produto/{id}")
    Call<Void> excluir(@Header("Authorization") String authorization, @Path(value = "id") String id);
}
