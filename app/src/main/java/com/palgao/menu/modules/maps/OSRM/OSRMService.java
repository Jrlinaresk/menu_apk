package com.palgao.menu.modules.maps.OSRM;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface OSRMService {
    @GET("route/v1/driving/{startLong},{startLat};{endLong},{endLat}")
    Call<OSRMResponse> getRoute(
            @Path("startLong") double startLong,
            @Path("startLat") double startLat,
            @Path("endLong") double endLong,
            @Path("endLat") double endLat,
            @Query("overview") String overview,
            @Query("steps") boolean steps
    );
}

