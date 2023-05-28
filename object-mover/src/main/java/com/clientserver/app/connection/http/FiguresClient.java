package com.clientserver.app.connection.http;

import retrofit2.Call;
import retrofit2.http.*;

public interface FiguresClient {
    @GET("figures")
    Call<String> getFigures();

    @GET("figures/{id}")
    Call<String> getFigureById(@Path("id") int id);

    @POST("figures")
    Call<Void> postUploadFigure(@Body String shape);

    @GET("figures/count")
    Call<Long> getCountFigures();

    @DELETE("figures")
    Call<Void> deleteFigures();
}
