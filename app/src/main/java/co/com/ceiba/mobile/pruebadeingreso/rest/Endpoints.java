package co.com.ceiba.mobile.pruebadeingreso.rest;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Endpoints {

    public static final String GET_USERS = "/users";
    public static final String GET_POST_USER = "/posts?";

    //Trae todos los usuarios
    @GET(GET_USERS)
    Call<String> users();

    //Trae post por usuario
    @GET(GET_POST_USER)
    Call<String> postUser(@Query("userId") int id);
}
