package com.example.yuansunny.imagesearch.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by yuansunny on 2018/4/3.
 */

public class ImageList {

    @SerializedName("hits")
    private List<Image> imageList;
    public List<Image> getImageList() {
        return imageList;
    }

    public void setImageList(List<Image> imageList) {
        this.imageList = imageList;
    }

}
