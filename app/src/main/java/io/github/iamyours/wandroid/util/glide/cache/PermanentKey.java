package io.github.iamyours.wandroid.util.glide.cache;

import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.signature.EmptySignature;

import java.security.MessageDigest;

public class PermanentKey implements Key {
    private EmptySignature signature;
    private GlideUrl sourceKey;//url地址

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
