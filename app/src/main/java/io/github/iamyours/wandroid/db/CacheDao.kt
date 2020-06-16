package io.github.iamyours.wandroid.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.github.iamyours.wandroid.vo.CacheArticleVO

@Dao
interface CacheDao {

    @Query("delete from CacheArticleVO where link = :link")
    fun delete(link: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(item: CacheArticleVO)

    @Query("SELECT 1 FROM CacheArticleVO WHERE link=:link")
    fun hasCache(link: String): Boolean

    @Query("SELECT * FROM CacheArticleVO  ORDER BY timestamp desc limit :size offset :offset")
    fun list(
        offset: Int,
        size: Int
    ): LiveData<List<CacheArticleVO>>
}