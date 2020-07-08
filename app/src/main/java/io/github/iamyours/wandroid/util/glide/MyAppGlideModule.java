package io.github.iamyours.wandroid.util.glide;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.ByteBuffer;

import io.github.iamyours.wandroid.util.glide.cache.WanDiskCacheFactory;

@GlideModule
public class MyAppGlideModule extends AppGlideModule {
    @Override
    public void registerComponents(Context context, Glide glide,
                                   Registry registry) {
        Log.e("GlideModule", "registerComponents");
        registry.prepend(String.class, ByteBuffer.class,
                new Base64ModelLoaderFactory());
    }

    @Override
    public void applyOptions(@NonNull Context context,
                             @NonNull GlideBuilder builder) {
        super.applyOptions(context, builder);
        builder.setDiskCache(new WanDiskCacheFactory(new WanDiskCacheFactory.CacheDirectoryGetter() {
            @NotNull
            @Override
            public File getCacheDirectory() {
                return new File(context.getCacheDir(), "wandroid_images");
            }

            @NotNull
            @Override
            public File getPermanentDirectory() {
                return new File(context.getFilesDir(), "permanent_images");
            }
        }));
    }
}