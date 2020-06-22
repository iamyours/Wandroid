package io.github.iamyours.wandroid.util.glide;

import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import java.nio.ByteBuffer;

public class Base64ModelLoaderFactory implements ModelLoaderFactory<String, ByteBuffer> {

  @Override
  public ModelLoader<String, ByteBuffer> build(MultiModelLoaderFactory multiFactory) {
    return new Base64ModelLoader();
  }

  @Override
  public void teardown() {
    // Do nothing.
  }
}