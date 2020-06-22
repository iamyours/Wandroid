package io.github.iamyours.wandroid.util.glide;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.signature.ObjectKey;

import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * Loads an {@link InputStream} from a Base 64 encoded String.
 */
public final class Base64ModelLoader implements ModelLoader<String,
        ByteBuffer> {

    @Override
    public LoadData<ByteBuffer> buildLoadData(String model, int width,
                                              int height, Options options) {
        return new LoadData<>(new ObjectKey(model),
                new Base64DataFetcher(model));
    }

    @Override
    public boolean handles(String model) {
        return model.endsWith(".html");
    }
}