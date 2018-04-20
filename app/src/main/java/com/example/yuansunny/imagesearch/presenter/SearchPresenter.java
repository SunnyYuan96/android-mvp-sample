package com.example.yuansunny.imagesearch.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.example.yuansunny.imagesearch.model.Image;
import com.example.yuansunny.imagesearch.network.PixabayApi;
import com.example.yuansunny.imagesearch.util.BaseSchedulerProvider;
import com.example.yuansunny.imagesearch.view.SearchImagesView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by yuansunny on 2018/3/29.
 */

public class SearchPresenter implements ISearchPresenter {
    private static final String TAG = SearchPresenter.class.getSimpleName();
    private static final boolean DEBUG = false;
    private final String SEARCH_URL = "https://pixabay.com/";
    private final String API_KEY = "8522485-d9e4a9c26e0a3b5a28c13b61a";
    private final int PER_PAGE_NUM = 30;
    private final BaseSchedulerProvider mSchedulerProvider;
    private SearchImagesView view;
    private Retrofit mRetrofit;
    private PixabayApi mPixabayApi;
    private int mPage = 1;
    private List<Image> mImagesList = new ArrayList();
    private boolean mIsLoading = false;


    public SearchPresenter(SearchImagesView view, BaseSchedulerProvider mSchedulerProvider) {
        this.view = view;
        this.mSchedulerProvider = mSchedulerProvider;
    }

    public void init(){
        mRetrofit = new Retrofit.Builder()
                .baseUrl(SEARCH_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        mPixabayApi = mRetrofit.create(PixabayApi.class);
    }

    @Override
    public void searchImages(String keyword) {
        mImagesList.clear();
        mPage = 1;
        loadImagesData(keyword);
    }

    @Override
    public void loadMoreData(String keyword) {
        if (mIsLoading) return;
        mPage++;
        loadImagesData(keyword);
    }

    private void loadImagesData(String keyword) {
        if (TextUtils.isEmpty(keyword)) return;
        view.setProgressShow(true);
        mIsLoading = true;
        if(DEBUG) Log.d(TAG, "Load images data...... ");
        mPixabayApi.getImages(API_KEY, keyword, PER_PAGE_NUM, mPage)
                .subscribeOn(mSchedulerProvider.io())
                .observeOn(mSchedulerProvider.ui())
                .doFinally(() ->{
                    view.setProgressShow(false);
                    mIsLoading = false;
                })
                .subscribe(images -> {
                    mImagesList.addAll(images.getImageList());
                    view.updateResult(mImagesList);
                }, err -> Log.e(TAG, "Get images fail:" + err.toString()));
    }
}