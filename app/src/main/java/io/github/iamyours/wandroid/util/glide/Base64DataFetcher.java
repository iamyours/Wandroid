package io.github.iamyours.wandroid.util.glide;

import android.util.Base64;
import android.util.Log;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.data.DataFetcher;

import java.nio.ByteBuffer;

import io.github.iamyours.wandroid.util.Wget;

public class Base64DataFetcher implements DataFetcher<ByteBuffer> {

    private final String model;

    Base64DataFetcher(String model) {
        this.model = model;
    }

    @Override
    public void loadData(Priority priority,
                         DataCallback<? super ByteBuffer> callback) {
        String base64Section = getBase64SectionOfModel();
        byte[] data = Base64.decode(base64Section, Base64.DEFAULT);
        ByteBuffer byteBuffer = ByteBuffer.wrap(data);
        callback.onDataReady(byteBuffer);
    }

    private String getBase64SectionOfModel() {
        // See https://developer.mozilla
        // .org/en-US/docs/Web/HTTP/Basics_of_HTTP/Data_URIs.
        Log.e("Base64Fetcher","url:"+model);
        String res = Wget.getUnsafe(model);
        int startOfBase64Section = res.indexOf(',');
        return res.substring(startOfBase64Section + 1);
    }

    @Override
    public void cleanup() {
        // Intentionally empty only because we're not opening an InputStream
        // or another I/O resource!
    }

    @Override
    public void cancel() {
        // Intentionally empty.
    }

    @Override
    public Class<ByteBuffer> getDataClass() {
        return ByteBuffer.class;
    }

    @Override
    public DataSource getDataSource() {
        return DataSource.LOCAL;
    }
}