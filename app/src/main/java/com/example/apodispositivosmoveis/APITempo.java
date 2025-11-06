package com.example.apodispositivosmoveis;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APITempo {
    @GET("weather")
    Call<WeatherResponse> getTempo(
            @Query("q") String cityName,
            @Query("appid") String apiKey,
            @Query("units") String units
    );
}
