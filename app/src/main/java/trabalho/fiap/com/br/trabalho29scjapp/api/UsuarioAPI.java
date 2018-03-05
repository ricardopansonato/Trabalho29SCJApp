package trabalho.fiap.com.br.trabalho29scjapp.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import trabalho.fiap.com.br.trabalho29scjapp.model.Token;
import trabalho.fiap.com.br.trabalho29scjapp.model.Usuario;

public interface UsuarioAPI {

    @POST("/auth")
    Call<Token> login(@Body Usuario usuario);
}
