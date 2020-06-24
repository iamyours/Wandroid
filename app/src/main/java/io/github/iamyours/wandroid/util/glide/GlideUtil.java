package io.github.iamyours.wandroid.util.glide;

import android.graphics.Bitmap;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.FutureTarget;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

import io.github.iamyours.wandroid.App;
import io.github.iamyours.wandroid.extension.StringKt;

public class GlideUtil {
    public static byte[] syncLoad(String url, String type) {
        boolean isGif = type.endsWith("gif");
        StringKt.logE("syncLoad:" + type + ", " + url);
        if (isGif) {
            try {
                FutureTarget<byte[]> target = Glide.with(App.instance)
                        .as(byte[].class).decode(GifDrawable.class).submit();
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
