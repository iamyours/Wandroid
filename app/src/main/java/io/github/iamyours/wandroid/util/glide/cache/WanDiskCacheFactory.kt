package io.github.iamyours.wandroid.util.glide.cache

import com.bumptech.glide.load.engine.cache.DiskCache
import java.io.File

/**
 * 玩Android图片缓存,webview离线缓存图片，永久图片
 */
class WanDiskCacheFactory(var cacheDirectoryGetter: CacheDirectoryGetter) :
    DiskCache.Factory {

    interface CacheDirectoryGetter {
        val cacheDirectory: File
        val permanentDirectory: File
    }

    override fun build(): DiskCache? {
        val cacheDir: File =
            cacheDirectoryGetter.cacheDirectory
        val permanentDirectory = cacheDirectoryGetter.permanentDirectory
        cacheDir.mkdirs()
        permanentDirectory.mkdirs()

        return if ((!cacheDir.exists()
                    || !cacheDir.isDirectory
                    || !permanentDirectory.exists()
                    || !permanentDirectory.isDirectory)
        ) {
            null
        } else WanDiskLruCacheWrapper.create(
            permanentDirectory,
            cacheDir,
            20 * 1024 * 1024//262144000L(250M) for cache
        )

    }
}