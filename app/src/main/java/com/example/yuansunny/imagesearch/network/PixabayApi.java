package com.example.yuansunny.imagesearch.network;

import com.example.yuansunny.imagesearch.model.ImageList;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by yuansunny on 2018/4/3.
 */

public interface PixabayApi {
    @GET("api/")
    Observable<ImageList> getImages(@Query("key") String key, @Query("q") String keyword,
                                    @Query("per_page") int per_page, @Query("page") int page);
}
