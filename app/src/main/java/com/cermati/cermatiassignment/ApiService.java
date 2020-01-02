package com.cermati.cermatiassignment;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("search/users")
    Observable<ResponseSearch<Model>> getSearch(@Query("q") String name);
}
