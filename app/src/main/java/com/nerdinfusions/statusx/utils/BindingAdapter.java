package com.nerdinfusions.statusx.utils;

import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class BindingAdapter {

    @androidx.databinding.BindingAdapter({"imagePath"})
    public static void loadImage(ImageView view, String path) {
        Glide.with(view.getContext())
                .load(path)
                .thumbnail(0.05f)
                .into(view);
    }


}
