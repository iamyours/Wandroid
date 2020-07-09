package io.github.iamyours.wandroid.util.glide.cache

import com.bumptech.glide.load.Key
import com.bumptech.glide.load.engine.cache.DiskCache
import com.bumptech.glide.load.engine.cache.SafeKeyGenerator
import io.github.iamyours.wandroid.extension.logE
import java.io.File
import java.io.IOException

class WanDiskLruCacheWrapper(//常驻图片文件
    private var permanentDirectory: File,
    directory: File,
    private var maxSize: Long
) : DiskCache {

    private val APP_VERSION = 1
    private val VALUE_COUNT = 1

    private var safeKeyGenerator: SafeKeyGenerator? = null
    private var directory: File? = directory
    private val writeLocker = DiskCacheWriteLocker()
    private var diskLruCache: WanDiskLruCache? =
        null

    companion object {
        @JvmStatic
        var instance: WanDiskLruCacheWrapper? = null
        fun create(
            permanentDirectory: File,
            cacheDirectory: File,
            maxSize: Long
        ): WanDiskLruCacheWrapper {
            return instance ?: WanDiskLruCacheWrapper(
                permanentDirectory,
                cacheDirectory,
                maxSize
            ).apply {
                instance = this
            }
        }
    }


    init {
        safeKeyGenerator = SafeKeyGenerator()
    }

    @Synchronized
    @Throws(IOException::class)
    private fun getDiskCache(): WanDiskLruCache? {
        if (diskLruCache == null) {
            diskLruCache = WanDiskLruCache.open(
                permanentDirectory,
                directory,
                APP_VERSION,
                VALUE_COUNT,
                maxSize
            )
        }
        return diskLruCache
    }

    override fun get(key: Key): File? {
        val safeKey = safeKeyGenerator!!.getSafeKey(key)
        var result: File? = null
        try {
            // It is possible that the there will be a put in between these two gets. If so that shouldn't
            // be a problem because we will always put the same value at the same key so our input streams
            // will still represent the same data.
            val value = getDiskCache()?.get(safeKey)
            result = value?.getFile(0)
        } catch (e: IOException) {

        }
        return result
    }

    /**
     * 将缓存中的文件移动到permanent
     */
    fun cacheToPermanent(key: Key): Boolean {
        val safeKey = safeKeyGenerator!!.getSafeKey(key)
        "safeKey:$safeKey".logE()
        try {
            return getDiskCache()!!.cacheToPermanent(safeKey)
        } catch (e: IOException) {
        }
        return false
    }

    fun removePermanent(key: Key) {
        val safeKey = safeKeyGenerator!!.getSafeKey(key)
        try {
            getDiskCache()!!.removePermanent(safeKey)
        } catch (e: IOException) {
        }
    }

    override fun put(
        key: Key,
        writer: DiskCache.Writer
    ) {
        // We want to make sure that puts block so that data is available when put completes. We may
        // actually not write any data if we find that data is written by the time we acquire the lock.
        val safeKey = safeKeyGenerator!!.getSafeKey(key)
        writeLocker.acquire(safeKey)
        try {

            try {
                // We assume we only need to put once, so if data was written while we were trying to get
                // the lock, we can simply abort.
                val diskCache = getDiskCache()
                val current =
                    diskCache!![safeKey]
                if (current != null) {
                    return
                }
                val editor =
                    diskCache.edit(safeKey)
                        ?: throw IllegalStateException("Had two simultaneous puts for: $safeKey")
                try {
                    val file = editor.getFile(0)
                    if (writer.write(file)) {
                        editor.commit()
                    }
                } finally {
                    editor.abortUnlessCommitted()
                }
            } catch (e: IOException) {

            }
        } finally {
            writeLocker.release(safeKey)
        }
    }

    override fun delete(key: Key?) {
        val safeKey = safeKeyGenerator!!.getSafeKey(key)
        try {
            getDiskCache()!!.remove(safeKey)
        } catch (e: IOException) {

        }
    }

    @Synchronized
    override fun clear() {
        try {
            getDiskCache()!!.delete()
        } catch (e: IOException) {

        } finally {
            // Delete can close the cache but still throw. If we don't null out the disk cache here, every
            // subsequent request will try to act on a closed disk cache and fail. By nulling out the disk
            // cache we at least allow for attempts to open the cache in the future. See #2465.
            resetDiskCache()
        }
    }

    @Synchronized
    private fun resetDiskCache() {
        diskLruCache = null
    }

    fun permanentTempFile(url: String): File {
        val key = PermanentKey(url)
        val safeKey = safeKeyGenerator!!.getSafeKey(key)
        return File(permanentDirectory, "$safeKey.tmp")
    }

    /**
     * 将永久区中tmp重命名
     */
    fun tempToPermanent(url: String): Boolean {
        try {
            val key = PermanentKey(url)
            val safeKey = safeKeyGenerator!!.getSafeKey(key)
            return getDiskCache()!!.tempToPermanent(
                permanentTempFile(url),
                safeKey
            )
        } catch (e: IOException) {

        }
        return false
    }
}