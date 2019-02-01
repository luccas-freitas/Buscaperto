package com.luccas.buscaperto.model.store;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface StoreInterface {

    @GET("{token}/store/_all")
    Call<StoresCatalog> storeCatalog(
            @Path("token") String token,
            @Query("sourceId") String sourceId,
            @Query("hasOffer") boolean hasOffer
    );
}
