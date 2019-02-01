package com.luccas.buscaperto.model.offer;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface OfferInterface {
    @GET("{token}/offer/_bestsellers")
    Call<OfferCatalog> offerCatalog(
            @Path("token") String token,
            @Query("sourceId") String sourceId,
            @Query("size") String size
    );

    @GET("{token}/offer/_store/{storeId}")
    Call<OfferCatalog> storeCatalog(
            @Path("token") String token,
            @Path("storeId") String storeId,
            @Query("sourceId") String sourceId,
            @Query("size") String size
    );

    @GET("{token}/offer/_search")
    Call<OfferCatalog> searchCatalog(
            @Path("token") String token,
            @Query("sourceId") String sourceId,
            @Query("keyword") String keyword
    );

    @GET("{token}/offer/_id/{offerId}")
    Call<Offer> favoriteCatalog(
            @Path("token") String token,
            @Path("offerId") String offerId,
            @Query("sourceId") String sourceId,
            @Query("storeId") String storeId
    );
}
