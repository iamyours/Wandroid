package io.github.iamyours.wandroid.util.glide;

import android.graphics.Bitmap;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.FutureTarget;

import java.io.ByteArrayOutputStream;
import java.io.File;

import io.github.iamyours.wandroid.App;
import io.github.iamyours.wandroid.extension.StringKt;
import io.github.iamyours.wandroid.util.glide.cache.PermanentKey;
import io.github.iamyours.wandroid.util.glide.cache.WanDiskLruCacheWrapper;

public class GlideUtil {

    public static boolean cacheToPermanent(String url) {
        PermanentKey key = new PermanentKey(url);
        return WanDiskLruCacheWrapper.getInstance().cacheToPermanent(key);
    }

    public static void removePermanent(String url) {
        PermanentKey key = new PermanentKey(url);
        WanDiskLruCacheWrapper.getInstance().removePermanent(key);
    }

    public static File permanentTempName(String url) {
        return WanDiskLruCacheWrapper.getInstance().permanentTempFile(url);
    }

    public static boolean tempToPermanent(String url){
        return WanDiskLruCacheWrapper.getInstance().tempToPermanent(url);
    }

    public static byte[] syncLoad(String url, String type) {
        boolean isGif = type.endsWith("gif");
        if (isGif) {
            try {
                FutureTarget<byte[]> target = Glide.with(App.instance)
                        .as(byte[].class)
                        .load(url)
                        .decode(GifDrawable.class).submit();
                return target.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        FutureTarget<Bitmap> target = Glide.with(App.instance)
                .asBitmap().load(url).submit();
        try {
            Bitmap bitmap = target.get();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
