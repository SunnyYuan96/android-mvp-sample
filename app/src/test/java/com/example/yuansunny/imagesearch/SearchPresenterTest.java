package com.example.yuansunny.imagesearch;

import com.example.yuansunny.imagesearch.model.Image;
import com.example.yuansunny.imagesearch.model.ImageList;
import com.example.yuansunny.imagesearch.network.PixabayApi;
import com.example.yuansunny.imagesearch.presenter.SearchPresenter;
import com.example.yuansunny.imagesearch.util.BaseSchedulerProvider;
import com.example.yuansunny.imagesearch.util.ImmediateSchedulerProvider;
import com.example.yuansunny.imagesearch.view.SearchImagesView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Created by yuansunny on 2018/4/4.
 */

public class SearchPresenterTest {
    private static ImageList IMAGES;

    @Mock
    private SearchImagesView mView;

    @Mock
    private PixabayApi mPixabayApi;

    private SearchPresenter mSearchPresenter;
    private BaseSchedulerProvider mSchedulerProvider;


    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        mSchedulerProvider = new ImmediateSchedulerProvider();
        mSearchPresenter = new SearchPresenter(mView, mSchedulerProvider);
        mSearchPresenter.init();

        IMAGES = new ImageList();
        List<Image> images = new ArrayList<>();
        Image image = new Image();
        image.setWebformatURL("https://pixabay.com/get/1");
        images.add(image);
        Image image2 = new Image();
        image.setWebformatURL("https://pixabay.com/get/2");
        images.add(image2);

        IMAGES.setImageList(images);
    }

    @Test
    public void loadImages() {
        when(mPixabayApi.getImages(anyString(), anyString(), anyInt(), anyInt()))
                .thenReturn(Observable.just(IMAGES));
        mSearchPresenter.searchImages(any(String.class));
        InOrder inOrder = Mockito.inOrder(mView);
        inOrder.verify(mView).setProgressShow(true);
        inOrder.verify(mView).updateResult(anyList());
        inOrder.verify(mView).setProgressShow(false);
    }
}
