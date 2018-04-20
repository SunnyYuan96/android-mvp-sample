package com.example.yuansunny.imagesearch.view;

import com.example.yuansunny.imagesearch.model.Image;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuansunny on 2018/3/29.
 */

public interface SearchImagesView {
    void updateResult(List<Image> images);
    void setProgressShow(boolean isShow);
}
