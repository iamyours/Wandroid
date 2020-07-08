package io.github.iamyours.wandroid.util.glide.cache;

import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.signature.EmptySignature;
import com.bumptech.glide.util.LruCache;

import java.security.MessageDigest;

public class PermanentKey implements Key {
    private static final LruCache<Class<?>, byte[]> RESOURCE_CLASS_BYTES =
            new LruCache<>(50);
    private final Key sourceKey;
    private final Key signature;

    public PermanentKey(String url) {
        sourceKey = new GlideUrl(url);
        signature = EmptySignature.obtain();
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {
        sourceKey.updateDiskCacheKey(messageDigest);
        signature.updateDiskCacheKey(messageDigest);
    }

    @Override
    public int hashCode() {
        int result = sourceKey.hashCode();
        result = 31 * result + signature.hashCode();
        return result;
    }
}
