package com.sito.evpro.inspection.utils;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.target.ViewTarget;
import com.sito.evpro.inspection.R;


/**
 * 自定义的GlideMode 配置
 * Created by zhangan on 2016/6/17.
 */
public class MyGlideModule implements com.bumptech.glide.module.GlideModule {
    private static int maxCache = 100 * 1024 * 1000;//100M
    private static MyGlideModule glideMode;

    public MyGlideModule() {

    }

    /**
     * 获取对象
     *
     * @return MyGlideModule
     */
    public static MyGlideModule getGlideMode() {
        if (glideMode == null) {
            glideMode = new MyGlideModule();
        }
        return glideMode;
    }



    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
//        builder.setDiskCache(new DiskLruCacheFactory(context.getExternalCacheDir().getName(), maxCache));
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
        ViewTarget.setTagId(R.id.image_tag);
    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}
