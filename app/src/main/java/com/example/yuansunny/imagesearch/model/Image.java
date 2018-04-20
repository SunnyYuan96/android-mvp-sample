package com.example.yuansunny.imagesearch.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yuansunny on 2018/3/30.
 */

public class Image {
    @SerializedName("webformatURL")
    private String webformatURL;

    public String getWebformatURL() {
        return webformatURL;
    }
    public void setWebformatURL(String webformatURL) {
        this.webformatURL = webformatURL;
    }
}
