package com.example.yuansunny.imagesearch.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.yuansunny.imagesearch.R;
import com.example.yuansunny.imagesearch.model.Image;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuansunny on 2018/3/30.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageAdapterViewHolder> {
    private static final String TAG = ImageAdapter.class.getSimpleName();
    private final Context mContext;
    private final int mWindowWidth;
    private List<Image> mImages;

    public ImageAdapter(Context context) {
        this.mContext = context;
        mImages = new ArrayList<>();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        mWindowWidth = displayMetrics.widthPixels;
    }

    @Override
    public ImageAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);

        return new ImageAdapterViewHolder(inflater.inflate(R.layout.image_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ImageAdapterViewHolder holder, int position) {
        Image image = mImages.get(position);
        String url = image.getWebformatURL();

        int imageSize = mWindowWidth / 2 - mContext.getResources().getDimensionPixelSize(R.dimen.grid_margin) * 2;
        GradientDrawable placeholder = new GradientDrawable();
        placeholder.setSize(imageSize, imageSize);
        placeholder.setColor(Color.WHITE);
        Glide.with(mContext)
                .load(url)
                .apply(new RequestOptions()
                        .override(imageSize)
                        .placeholder(placeholder))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                Target<Drawable> target, boolean isFirstResource) {
                        Log.e(TAG, "Load failed", e);
                        return false;
                    }
                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mImages.size();
    }

public class ImageAdapterViewHolder extends RecyclerView.ViewHolder {
    public final ImageView imageView;

    public ImageAdapterViewHolder(View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.image_view);
    }
}

    public void setImagesData(List<Image> images) {
        mImages = images;
        notifyDataSetChanged();
    }
}
