package com.example.yuansunny.imagesearch.util;

import android.support.annotation.NonNull;

import io.reactivex.Scheduler;

/**
 * Created by yuansunny on 2018/4/7.
 */

public interface BaseSchedulerProvider {

    @NonNull
    Scheduler io();

    @NonNull
    Scheduler ui();
}
